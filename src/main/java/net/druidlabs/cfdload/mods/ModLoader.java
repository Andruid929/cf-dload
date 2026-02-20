package net.druidlabs.cfdload.mods;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum ModLoader {

    NO_MOD_LOADER("None", "non.existent", 0),
    FORGE("Forge", "META-INF/mods.toml", 3),
    FABRIC("Fabric", "fabric.mod.json", 4),
    QUILT("Quilt", "quilt.mod.json", 5),
    NEO_FORGE("NeoForge", "META-INF/neoforge.mods.toml", 6);

    private final String name;
    private final String modConfigFilename;
    private final int loaderId;

    ModLoader(String name, String modConfigFilename, int loaderId) {
        this.name = name;
        this.modConfigFilename = modConfigFilename;
        this.loaderId = loaderId;
    }

    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public @NotNull String getModConfigFilename() {
        return modConfigFilename;
    }

    public int getLoaderId() {
        return loaderId;
    }
}
