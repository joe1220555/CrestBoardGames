package tw.crestnetwork.boardgames.modules.chinesechess;

import tw.crestnetwork.boardgames.api.BoardGameModule;
import tw.crestnetwork.boardgames.api.GameCreationContext;
import tw.crestnetwork.boardgames.api.GameDefinition;
import tw.crestnetwork.boardgames.api.GameSession;
import tw.crestnetwork.boardgames.api.ModuleContext;
import tw.crestnetwork.boardgames.api.ModuleDescription;

public final class CrestChineseChessModule implements BoardGameModule {
    private ModuleContext context;

    @Override
    public ModuleDescription description() {
        return new ModuleDescription("chinese_chess", "CrestChineseChess", "中國象棋", "0.1.0-SNAPSHOT", 1,
                getClass().getName());
    }

    @Override
    public void onLoad(ModuleContext context) {
        this.context = context;
    }

    @Override
    public void onEnable() {
        context.api().registerGame(new GameDefinition("chinese_chess", "中國象棋", 2, 2, true, true));
    }

    @Override
    public void onDisable() {
    }

    @Override
    public GameSession createSession(GameCreationContext creationContext) {
        throw new UnsupportedOperationException("中國象棋規則引擎尚未實作");
    }
}
