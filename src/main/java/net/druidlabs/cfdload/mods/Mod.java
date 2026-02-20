package net.druidlabs.cfdload.mods;

import net.druidlabs.cfdload.errorhandling.ErrorLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public class Mod {

    public static final String DEFAULT_ID = "MOD-ID";
    public static final String DEFAULT_NAME = "MOD-NAME";
    public static final String DEFAULT_VERSION = "MOD-VERSION";

    private final String modFilename;
    private final String modId;
    private final String modVersion;
    private final String modName;
    private final int modLoaderId;

    private Mod(String modFilename, String modId, String modVersion, String modName, int modLoaderId) {
        this.modFilename = modFilename;
        this.modId = modId;
        this.modVersion = modVersion;
        this.modName = modName;
        this.modLoaderId = modLoaderId;
    }

    public String getModName() {
        return modName;
    }

    public String getModFilename() {
        return modFilename;
    }

    public String getModId() {
        return modId;
    }

    public String getModVersion() {
        return modVersion;
    }

    public int getModLoaderId() {
        return modLoaderId;
    }

    public ModLoader getModLoader() {
        for (ModLoader loader : ModLoader.values()) {
            if (loader.getLoaderId() == this.modLoaderId) {
                return loader;
            }
        }

        throw new IllegalStateException("No mod loader under ID " + this.modLoaderId);
    }

    @Contract("_ -> new")
    public static @NotNull Mod getInfo(@NotNull Path path) {
        String filename = path.getFileName().toString();

        if (!filename.endsWith(".jar")) {
            IllegalArgumentException exception = new IllegalArgumentException("\"" + filename + "\" is not a mod file");

            ErrorLogger.logError(exception);

            throw exception;
        }

        try {
            var modDetails = ModConfigUtil.readConfig(path);

            String id = modDetails.getModId();
            String version = modDetails.getModVersion();
            String name = modDetails.getModName();
            int loaderId = modDetails.getModLoaderId();

            return new Mod(filename, id, version, name, loaderId);

        } catch (IOException e) {
            ErrorLogger.logError(e);

            return new Mod(filename, DEFAULT_ID, DEFAULT_VERSION, DEFAULT_NAME, 0);
        }
    }
}
