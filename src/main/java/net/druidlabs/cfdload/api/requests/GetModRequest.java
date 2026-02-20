package net.druidlabs.cfdload.api.requests;

import net.druidlabs.cfdload.api.ForgeURL;
import net.druidlabs.cfdload.api.Request;
import net.druidlabs.cfdload.errorhandling.ErrorLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.http.HttpResponse;

public final class GetModRequest extends Request {

    private final int modProjectId;

    private GetModRequest(ForgeURL requestURL, int modProjectId) {
        super(requestURL);
        this.modProjectId = modProjectId;
    }

    @Override
    protected void processResponse(@NotNull HttpResponse<String> serverResponse) {
        responseCode = serverResponse.statusCode();

        try {

            if (responseCode == 200) {
                response = serverResponse.body();
            }

        } catch (Exception e) {
            ErrorLogger.logError(e);
        }
    }

    public static @NotNull GetModRequest requestForMod(int modProjectId) throws IOException {
        ForgeURL requestURL = new ForgeURL.Builder("v1", "mods", String.valueOf(modProjectId)).build();

        return new GetModRequest(requestURL, modProjectId);
    }
}
