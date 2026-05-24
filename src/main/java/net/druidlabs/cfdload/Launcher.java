package net.druidlabs.cfdload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.druidlabs.cfdload.api.Download;
import net.druidlabs.cfdload.api.Request;
import net.druidlabs.cfdload.api.requests.GetModFileRequest;
import net.druidlabs.cfdload.api.requests.RequestException;
import net.druidlabs.cfdload.api.requests.SearchModRequest;
import net.druidlabs.cfdload.api.response.FileIndex;
import net.druidlabs.cfdload.api.response.ResponseHandler;
import net.druidlabs.cfdload.errorhandling.ErrorLogger;
import net.druidlabs.cfdload.io.Paths;
import net.druidlabs.cfdload.mapping.Mapping;
import net.druidlabs.cfdload.mods.Mod;
import net.druidlabs.cfdload.mods.ModLoader;
import net.druidlabs.cfdload.util.SlugExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static net.druidlabs.cfdload.Constants.MINECRAFT_GAME_ID_PARAM;

public class Launcher {

    private static final Set<Mapping> mappingsToPersist = ConcurrentHashMap.newKeySet();

    private static final Type MAPPING_CONFIG_TYPE = new TypeToken<Set<Mapping>>() {
    }.getType();

    public static void main(String[] args) throws InterruptedException {
        ErrorLogger.initiate();

        try {
            Set<Mapping> localMappings = loadMappings();

            if (!localMappings.isEmpty()) {
                mappingsToPersist.addAll(localMappings);
            }



            //At the end, persist mappings if any changes were made
            if (!mappingsToPersist.isEmpty() && !mappingsToPersist.equals(localMappings)) {
                persistMappings();
            }

        } catch (Throwable e) {
            ErrorLogger.logError(e);
        }

        Thread.sleep(2000);
    }

    private static @NotNull @Unmodifiable List<FileIndex> requestForMod(String url, String version, ModLoader loader) {
        String slug = SlugExtractor.getSlug(url);

        ResponseHandler searchResponseHandler;

        try {
            Request request = SearchModRequest.searchMod(MINECRAFT_GAME_ID_PARAM, "slug=".concat(slug));

            if (request.getResponseCode() == 200) {

                searchResponseHandler = ResponseHandler.handleSearchModResponse(request.getResponse(), 0);

            } else {
                throw new RequestException("Couldn't get response from server: " + request.getResponseCode());
            }

        } catch (IOException e) {
            throw new RequestException(e, "Encountered error getting info for mod at \"" + url + "\"");
        }

        List<JsonElement> indexElements = searchResponseHandler.asJsonArray(FileIndex.FILE_INDEXES_MEMBER_NAME).asList();

        return indexElements.stream()
                .map(element -> new Gson().fromJson(element, FileIndex.class))
                .filter(index -> {
                    boolean matchesVersion = index.getGameVersion().equals(version);
                    boolean matchesLoader = index.getModLoaderId() == loader.getLoaderId();

                    return matchesLoader && matchesVersion;
                })
                .toList();

    }

    private static void downloadMod(int projectId, int fileId, String tempFilename, String modSlug, String modName) throws IOException {
        Request request = GetModFileRequest.getModFileUrl(projectId, fileId);

        String downloadUrl = ResponseHandler.getFileUrlResponse(request.getResponse());

        Path savePath = Paths.APP_DIRECTORY.resolve(tempFilename).resolve(".cfd");

        new Download(downloadUrl, savePath);

        Mod mod = Mod.getInfo(savePath, true);

        Mapping mapping = new Mapping(modName, modSlug, mod.getModId(), projectId);

        mappingsToPersist.add(mapping);

        String filename = mod.getModId().concat("-")
                .concat(mod.getModVersion()).concat("-")
                .concat(mod.getModLoaderName()).concat(".jar");

        Files.copy(savePath, Paths.MINECRAFT_MODS_FOLDER.resolve(filename));
    }

    private static Set<Mapping> loadMappings() {
        Path saveFile = Paths.APP_DIRECTORY.resolve(Mapping.MAPPINGS_FILENAME);

        if (Files.notExists(saveFile)) {
            return Collections.emptySet();
        }

        try (BufferedReader reader = Files.newBufferedReader(saveFile, UTF_8)) {

            return new Gson().fromJson(reader, MAPPING_CONFIG_TYPE);

        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    public static void persistMappings() throws IOException {
        if (mappingsToPersist.isEmpty()) {

            return;
        }

        Path configFolder = Paths.APP_CONFIG_FOLDER;

        if (Files.notExists(configFolder)) {
            Files.createDirectories(configFolder);
        }

        Path savePath = configFolder.resolve(Mapping.MAPPINGS_FILENAME);

        try (BufferedWriter writer = Files.newBufferedWriter(savePath, UTF_8, CREATE, TRUNCATE_EXISTING)) {

            new Gson().toJson(mappingsToPersist, MAPPING_CONFIG_TYPE, writer);
        }
    }

}
