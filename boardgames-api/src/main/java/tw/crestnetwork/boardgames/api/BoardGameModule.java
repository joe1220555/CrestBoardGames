package tw.crestnetwork.boardgames.api;

public interface BoardGameModule {
    ModuleDescription description();

    void onLoad(ModuleContext context) throws Exception;

    void onEnable() throws Exception;

    void onDisable() throws Exception;

    GameSession createSession(GameCreationContext context);
}
