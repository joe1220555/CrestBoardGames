package tw.crestnetwork.boardgames.modules.mahjong;

import tw.crestnetwork.boardgames.api.BoardGameModule;
import tw.crestnetwork.boardgames.api.GameCreationContext;
import tw.crestnetwork.boardgames.api.GameDefinition;
import tw.crestnetwork.boardgames.api.GameSession;
import tw.crestnetwork.boardgames.api.ModuleContext;
import tw.crestnetwork.boardgames.api.ModuleDescription;

public final class CrestMahjongModule implements BoardGameModule {
    private ModuleContext context;

    @Override
    public ModuleDescription description() {
        return new ModuleDescription("mahjong", "CrestMahjong", "台灣十六張麻將", "0.1.0-SNAPSHOT", 1,
                getClass().getName());
    }

    @Override
    public void onLoad(ModuleContext context) {
        this.context = context;
    }

    @Override
    public void onEnable() {
        context.api().registerGame(new GameDefinition("mahjong", "台灣十六張麻將", 4, 4, true, true));
    }

    @Override
    public void onDisable() {
    }

    @Override
    public GameSession createSession(GameCreationContext creationContext) {
        throw new UnsupportedOperationException("台灣麻將對局狀態機尚未接上實體牌桌");
    }
}
