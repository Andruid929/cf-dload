package net.druidlabs.cfdload.api.requests;

import net.druidlabs.cfdload.Constants;
import net.druidlabs.cfdload.api.Request;
import net.druidlabs.cfdload.util.SlugExtractor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class Requests {

    public static @NotNull String searchSlugResponse(String url) throws IOException {
        String slug = "slug=".concat(SlugExtractor.getSlug(url));

        Request request = SearchModRequest.searchMod(Constants.MINECRAFT_GAME_ID_PARAM, slug);

        return request.getResponse();
    }

    public static @NotNull String getModResponse(int projectId) throws IOException {
        Request request = GetModRequest.requestForMod(projectId);

        return request.getResponse();
    }

}
