package net.druidlabs.cfdload.mods;

import com.google.gson.JsonSyntaxException;
import net.druidlabs.cfdload.errorhandling.ErrorLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public class Mod {

    public static final String DEFAULT_ID = "MOD-ID";
    public static final String DEFAULT_NAME = "MOD-ID";
    public static final String DEFAULT_VERSION = "MOD-ID";
    public static final ModLoader DEFAULT_LOADER = ModLoader.NO_MOD_LOADER;

    private final String modFilename;
    private final String modId;
    private final String modVersion;
    private final String modName;
    private int modLoaderId;

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

    public void setModLoader(int modLoaderId) {
        this.modLoaderId = modLoaderId;
    }

    public ModLoader getModLoader() {
        return ModLoader.NO_MOD_LOADER;
    }

    @Contract("_ -> new")
    public static @NotNull Mod getInfo(@NotNull Path path) {
        String fileName = path.getFileName().toString();

        if (!fileName.endsWith(".jar")) {
            IllegalArgumentException exception = new IllegalArgumentException("\"" + fileName + "\" is not a mod file");

            ErrorLogger.logError(exception);

            throw exception;
        }

        String id = "";
        String version = "";
        String name = "";
        ModLoader loader = ModLoader.FABRIC;

        return new Mod(id, version, name, fileName, loader.getLoaderId());
    }
}
