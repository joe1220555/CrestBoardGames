package tw.crestnetwork.boardgames.core.module;

import tw.crestnetwork.boardgames.api.BoardGameModule;
import tw.crestnetwork.boardgames.api.ModuleDescription;

import java.net.URLClassLoader;

record LoadedModule(ModuleDescription description, BoardGameModule instance, URLClassLoader classLoader) {
}
