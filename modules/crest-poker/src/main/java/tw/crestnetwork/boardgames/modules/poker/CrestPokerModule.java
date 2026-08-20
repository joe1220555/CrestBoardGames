package tw.crestnetwork.boardgames.modules.poker;

import tw.crestnetwork.boardgames.api.BoardGameModule;
import tw.crestnetwork.boardgames.api.GameCreationContext;
import tw.crestnetwork.boardgames.api.GameDefinition;
import tw.crestnetwork.boardgames.api.GameSession;
import tw.crestnetwork.boardgames.api.ModuleContext;
import tw.crestnetwork.boardgames.api.ModuleDescription;

public final class CrestPokerModule implements BoardGameModule {
    private ModuleContext context;

    @Override
    public ModuleDescription description() {
        return new ModuleDescription("poker", "CrestPoker", "撲克牌", "0.1.0-SNAPSHOT", 1,
                getClass().getName());
    }

    @Override
    public void onLoad(ModuleContext context) {
        this.context = context;
    }

    @Override
    public void onEnable() {
        context.api().registerGame(new GameDefinition("poker", "撲克牌", 2, 9, true, true));
    }

    @Override
    public void onDisable() {
    }

    @Override
    public GameSession createSession(GameCreationContext creationContext) {
        throw new UnsupportedOperationException("撲克牌規則引擎尚未實作");
    }
}
