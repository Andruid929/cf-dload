package net.druidlabs.cfdload.mods;

public enum ModLoader {

    NO_MOD_LOADER("None", "", 0),
    FORGE("Forge", "mods.toml", 3),
    FABRIC("Fabric", "fabric.mod.json", 4),
    QUILT("Quilt", "", 5),
    NEO_FORGE("NeoForge", "", 6);

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

    public String getModConfigFilename() {
        return modConfigFilename;
    }

    public int getLoaderId() {
        return loaderId;
    }
}
