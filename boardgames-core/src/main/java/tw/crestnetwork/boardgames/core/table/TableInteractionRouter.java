package tw.crestnetwork.boardgames.core.table;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TableInteractionRouter implements Listener {
    private final Map<UUID, Binding> bindings = new HashMap<>();

    public void bind(Entity interaction, PhysicalTableImpl table, String pieceId) {
        bindings.put(interaction.getUniqueId(), new Binding(table, pieceId));
    }

    public void unbind(Entity interaction) {
        bindings.remove(interaction.getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Binding binding = bindings.get(event.getRightClicked().getUniqueId());
        if (binding == null) {
            return;
        }
        event.setCancelled(true);
        binding.table().click(event.getPlayer().getUniqueId(), binding.pieceId());
    }

    private record Binding(PhysicalTableImpl table, String pieceId) {
    }
}
