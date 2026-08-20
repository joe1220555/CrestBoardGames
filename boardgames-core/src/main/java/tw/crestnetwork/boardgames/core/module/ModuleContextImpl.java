package tw.crestnetwork.boardgames.core.module;

import tw.crestnetwork.boardgames.api.BoardGamesApi;
import tw.crestnetwork.boardgames.api.ModuleContext;

import java.nio.file.Path;
import java.util.logging.Logger;

record ModuleContextImpl(BoardGamesApi api, Path dataDirectory, Logger logger) implements ModuleContext {
}
