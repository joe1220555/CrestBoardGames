package tw.crestnetwork.boardgames.core.table;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import tw.crestnetwork.boardgames.api.PhysicalPiece;
import tw.crestnetwork.boardgames.api.PhysicalTable;
import tw.crestnetwork.boardgames.core.CrestBoardGamesPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public final class PhysicalTableImpl implements PhysicalTable {
    private final CrestBoardGamesPlugin plugin;
    private final TableInteractionRouter router;
    private final UUID roomId;
    private final Location origin;
    private final Map<String, RenderedPiece> rendered = new HashMap<>();
    private final List<BlockDisplay> surface = new ArrayList<>();
    private BiConsumer<UUID, String> clickHandler = (playerId, pieceId) -> { };

    public PhysicalTableImpl(CrestBoardGamesPlugin plugin, TableInteractionRouter router, UUID roomId, Location origin) {
        this.plugin = plugin;
        this.router = router;
        this.roomId = roomId;
        this.origin = origin.clone();
        spawnSurface();
    }

    private void spawnSurface() {
        World world = origin.getWorld();
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Location location = origin.clone().add(x, -0.35, z);
                BlockDisplay block = world.spawn(location, BlockDisplay.class);
                block.setBlock(Bukkit.createBlockData(Material.DARK_OAK_SLAB));
                block.setPersistent(false);
                surface.add(block);
            }
        }
    }

    @Override
    public UUID roomId() {
        return roomId;
    }

    @Override
    public String worldName() {
        return origin.getWorld().getName();
    }

    @Override
    public void putPiece(PhysicalPiece piece) {
        removePiece(piece.id());
        Location location = origin.clone().add(piece.position().x(), piece.position().y(), piece.position().z());
        Display display;
        if (Boolean.parseBoolean(String.valueOf(piece.publicState().getOrDefault("item", false)))) {
            ItemDisplay itemDisplay = origin.getWorld().spawn(location, ItemDisplay.class);
            itemDisplay.setItemStack(oraxenItem(piece.modelKey()));
            itemDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
            itemDisplay.setBillboard(Display.Billboard.FIXED);
            display = itemDisplay;
        } else {
            TextDisplay textDisplay = origin.getWorld().spawn(location, TextDisplay.class);
            textDisplay.setText(String.valueOf(piece.publicState().getOrDefault("text", piece.modelKey())));
            textDisplay.setBillboard(Display.Billboard.VERTICAL);
            textDisplay.setSeeThrough(false);
            textDisplay.setShadowed(true);
            textDisplay.setBackgroundColor(Color.fromARGB(180, 28, 24, 20));
            display = textDisplay;
        }
        display.setPersistent(false);
        display.setRotation(piece.position().yaw(), piece.position().pitch());
        var transformation = display.getTransformation();
        transformation.getScale().set(piece.position().scale());
        display.setTransformation(transformation);

        Interaction interaction = origin.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionWidth(number(piece.publicState().get("width"), 0.8f));
        interaction.setInteractionHeight(number(piece.publicState().get("height"), 0.55f));
        interaction.setResponsive(true);
        interaction.setPersistent(false);
        router.bind(interaction, this, piece.id());

        RenderedPiece result = new RenderedPiece(piece, display, interaction);
        rendered.put(piece.id(), result);
        for (Player player : Bukkit.getOnlinePlayers()) {
            applyVisibility(result, player);
        }
    }

    private ItemStack oraxenItem(String itemId) {
        try {
            Class<?> items = Class.forName("io.th0rgal.oraxen.api.OraxenItems");
            Object builder = items.getMethod("getItemById", String.class).invoke(null, itemId);
            if (builder == null) {
                throw new IllegalArgumentException("Oraxen 找不到物品：" + itemId);
            }
            return (ItemStack) builder.getClass().getMethod("build").invoke(builder);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("無法從 Oraxen 建立物品 " + itemId, exception);
        }
    }

    private float number(Object value, float fallback) {
        return value instanceof Number number ? number.floatValue() : fallback;
    }

    @Override
    public void removePiece(String pieceId) {
        RenderedPiece previous = rendered.remove(pieceId);
        if (previous == null) {
            return;
        }
        router.unbind(previous.interaction());
        previous.display().remove();
        previous.interaction().remove();
    }

    @Override
    public Optional<PhysicalPiece> piece(String pieceId) {
        RenderedPiece value = rendered.get(pieceId);
        return value == null ? Optional.empty() : Optional.of(value.piece());
    }

    @Override
    public Collection<PhysicalPiece> pieces() {
        return rendered.values().stream().map(RenderedPiece::piece).toList();
    }

    @Override
    public void highlight(String pieceId, boolean highlighted) {
        RenderedPiece piece = rendered.get(pieceId);
        if (piece != null) {
            piece.display().setGlowing(highlighted);
            piece.interaction().setGlowing(highlighted);
        }
    }

    public void setClickHandler(BiConsumer<UUID, String> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public void click(UUID playerId, String pieceId) {
        RenderedPiece piece = rendered.get(pieceId);
        if (piece == null || !mayInteract(piece.piece(), playerId)) {
            return;
        }
        clickHandler.accept(playerId, pieceId);
    }

    public void refreshVisibility(Player player) {
        rendered.values().forEach(piece -> applyVisibility(piece, player));
    }

    private void applyVisibility(RenderedPiece renderedPiece, Player viewer) {
        boolean visible = mayView(renderedPiece.piece(), viewer.getUniqueId());
        if (visible) {
            viewer.showEntity(plugin, renderedPiece.display());
            viewer.showEntity(plugin, renderedPiece.interaction());
        } else {
            viewer.hideEntity(plugin, renderedPiece.display());
            viewer.hideEntity(plugin, renderedPiece.interaction());
        }
    }

    private boolean mayView(PhysicalPiece piece, UUID playerId) {
        Object privateValue = piece.publicState().get("private");
        if (!Boolean.parseBoolean(String.valueOf(privateValue))) {
            return true;
        }
        return String.valueOf(piece.publicState().get("owner")).equals(playerId.toString());
    }

    private boolean mayInteract(PhysicalPiece piece, UUID playerId) {
        Object owner = piece.publicState().get("owner");
        return owner == null || owner.toString().isBlank() || owner.toString().equals(playerId.toString());
    }

    @Override
    public void clear() {
        List.copyOf(rendered.keySet()).forEach(this::removePiece);
    }

    public void close() {
        clear();
        surface.forEach(BlockDisplay::remove);
        surface.clear();
    }

    private record RenderedPiece(PhysicalPiece piece, Display display, Interaction interaction) {
    }
}
