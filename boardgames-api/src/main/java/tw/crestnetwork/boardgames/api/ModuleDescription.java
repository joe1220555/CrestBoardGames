package tw.crestnetwork.boardgames.api;

import java.util.Objects;

public record ModuleDescription(
        String id,
        String name,
        String displayName,
        String version,
        int apiVersion,
        String mainClass
) {
    public ModuleDescription {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(version, "version");
        Objects.requireNonNull(mainClass, "mainClass");
    }
}
