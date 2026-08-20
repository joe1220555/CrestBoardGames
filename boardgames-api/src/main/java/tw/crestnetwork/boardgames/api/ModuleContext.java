package tw.crestnetwork.boardgames.api;

import java.nio.file.Path;
import java.util.logging.Logger;

public interface ModuleContext {
    BoardGamesApi api();

    Path dataDirectory();

    Logger logger();
}
