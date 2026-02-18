package net.druidlabs.cfdload.api.requests;

import net.druidlabs.cfdload.api.ForgeURL;
import net.druidlabs.cfdload.api.Request;
import net.druidlabs.cfdload.errorhandling.ErrorLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.http.HttpResponse;

public final class SearchModRequest extends Request {

    private SearchModRequest(ForgeURL requestURL) {
        super(requestURL);
    }

    @Override
    protected void processResponse(@NotNull HttpResponse<String> serverResponse) {
        try {
            responseCode = serverResponse.statusCode();

            if (responseCode == 200) {
                response = serverResponse.body();

            } else if (responseCode == 400) {
                throw new RuntimeException(response);

            } else if (responseCode == 500) {
                throw new RuntimeException(response);
            }

        } catch (Exception e) {
            response = ERROR_RESPONSE;

            ErrorLogger.logError(e);
        }
    }

    public static @NotNull SearchModRequest searchMod(String @NotNull ... searchParams) throws IOException {
        ForgeURL requestURL = new ForgeURL.Builder("v1", "mods", "search")
                .appendParams(searchParams).build();

        return new SearchModRequest(requestURL);
    }
}
