package net.druidlabs.cfdload.mods;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.druidlabs.cfdload.io.InOut;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ModConfigUtil {

    private final String modId;
    private final String modVersion;
    private final String modName;
    private final int modLoaderId;

    private ModConfigUtil(String modId, String modVersion, String modName, int modLoaderId) {
        this.modId = modId;
        this.modVersion = modVersion;
        this.modName = modName;
        this.modLoaderId = modLoaderId;
    }

    public String getModName() {
        return modName;
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

    @Contract(value = "_ -> new", pure = true)
    private static String @NotNull [] parseForgeConfig(String fileContents) {
        return new String[]{};
    }

    private static String @NotNull [] parseFabricConfig(String fileContents) {
        JsonObject gson = JsonParser.parseString(fileContents)
                .getAsJsonObject();

        String id = gson.get("id").getAsString();
        String version = gson.get("version").getAsString();
        String name = gson.get("name").getAsString();

        return new String[]{id, version, name};
    }

    @Contract(value = "_ -> new", pure = true)
    private static String @NotNull [] parseNeoForgeConfig(String fileContents) {
        return new String[]{};
    }

    @Contract(value = "_ -> new", pure = true)
    private static String @NotNull [] parseQuiltConfig(String fileContents) {
        return new String[]{};
    }

    public static @NotNull ModConfigUtil readConfig(Path path) throws IOException {

        try(JarFile modFile = new JarFile(path.toFile());) {
            for (ModLoader loader : ModLoader.values()) {

                JarEntry configEntry = modFile.getJarEntry(loader.getModConfigFilename());

                if (configEntry != null) {
                    int modLoaderId = loader.getLoaderId();

                    InputStream stream = modFile.getInputStream(configEntry);

                    String fileContents = InOut.readModConfig(stream);

                    stream.close();

                    String[] mD = switch (modLoaderId) {
                        case 3 -> parseForgeConfig(fileContents);
                        case 4 -> parseFabricConfig(fileContents);
                        case 5 -> parseQuiltConfig(fileContents);
                        case 6 -> parseNeoForgeConfig(fileContents);
                        default -> throw new IllegalStateException("Cannot find config file in mod \"" + path.getFileName() + "\"");
                    };

                    return new ModConfigUtil(mD[0], mD[1], mD[2], modLoaderId);
                }
            }

        }

        return new ModConfigUtil(Mod.DEFAULT_ID, Mod.DEFAULT_VERSION, Mod.DEFAULT_NAME, 0);
    }

    public static @NotNull ModConfigUtil readConfig(String path) throws IOException {
        return readConfig(Path.of(path));
    }
}
