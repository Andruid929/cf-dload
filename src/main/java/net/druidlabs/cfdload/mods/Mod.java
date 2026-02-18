package net.druidlabs.cfdload.mods;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.druidlabs.cfdload.errorhandling.ErrorLogger;
import net.druidlabs.cfdload.io.InOut;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.StringJoiner;

public class Mod {

    public static final String DEFAULT_ID = "MOD-ID";
    public static final String DEFAULT_NAME = "MOD-ID";
    public static final String DEFAULT_VERSION = "MOD-ID";
    public static final ModLoader DEFAULT_LOADER = ModLoader.NO_MOD_LOADER;

    private final String modFilename;
    private final String modId;
    private final String modVersion;
    private final String modName;
    private ModLoader modLoader;

    private Mod(String modFilename, String modId, String modVersion, String modName, ModLoader modLoader) {
        this.modFilename = modFilename;
        this.modId = modId;
        this.modVersion = modVersion;
        this.modName = modName;
        this.modLoader = modLoader;
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

    public void setModLoader(ModLoader modLoader) {
        this.modLoader = modLoader;
    }

    public ModLoader getModLoader() {
        return modLoader;
    }

    @Contract("_ -> new")
    public static @NotNull Mod getInfo(@NotNull Path path) {
        String fileName = path.getFileName().toString();

        if (!fileName.endsWith(".jar")) {
            IllegalArgumentException exception = new IllegalArgumentException("\"" + fileName + "\" is not a mod file");

            ErrorLogger.logError(exception);

            throw exception;
        }

        String id;
        String version;
        String name;
        ModLoader loader;

        try {
            Object[] modInfo = InOut.readModConfig(path);

            String fabricModJsonContents = modInfo[0].toString();

            loader = (ModLoader) modInfo[1];

            String[] modDetails = collectModInfo(loader.getLoaderId(), fabricModJsonContents);

            id = modDetails[0];
            version = modDetails[1];
            name = modDetails[2];

        } catch (IOException | JsonSyntaxException e) {
            ErrorLogger.logError(e);

            return new Mod(DEFAULT_ID, DEFAULT_VERSION, DEFAULT_NAME, fileName, DEFAULT_LOADER);
        }

        return new Mod(id, version, name, fileName, loader);
    }

    @Contract("_, _ -> new")
    private static String @NotNull [] collectModInfo(int loaderId, String modConfig) {
        String id;
        String version;
        String name;

        switch (loaderId) {
            case 3 -> { //Loader is forge,
                id = DEFAULT_ID;
                version = DEFAULT_VERSION;
                name = DEFAULT_NAME + "ss";
            }

            case 4 -> {
                JsonObject gson = JsonParser.parseString(modConfig)
                        .getAsJsonObject();

                id = gson.get("id").getAsString();
                version = gson.get("version").getAsString();
                name = gson.get("name").getAsString();
            }

            case 5 -> {
                id = DEFAULT_ID + " ";
                version = DEFAULT_VERSION;
                name = DEFAULT_NAME;
            }

            case 6 -> {
                id = DEFAULT_ID;
                version = DEFAULT_VERSION;
                name = DEFAULT_NAME + " ";
            }
            default -> {
                id = DEFAULT_ID;
                version = DEFAULT_VERSION;
                name = DEFAULT_NAME;
            }
        }

        return new String[]{id, version, name};
    }
}
