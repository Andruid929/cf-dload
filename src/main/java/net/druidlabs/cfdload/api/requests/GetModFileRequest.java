package net.druidlabs.cfdload.api.requests;

import net.druidlabs.cfdload.api.ForgeURL;
import net.druidlabs.cfdload.api.Request;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.http.HttpResponse;

public final class GetModFileRequest extends Request {

    private final int requestedProjectId;
    private final int requestedFileId;

    private GetModFileRequest(@NotNull ForgeURL requestURL, int requestedProjectId, int requestedFileId) {
        super(requestURL);

        this.requestedFileId = requestedFileId;
        this.requestedProjectId = requestedProjectId;
    }

    @Override
    protected void processResponse(@NotNull HttpResponse<String> serverResponse) {
        try {
            responseCode = serverResponse.statusCode();

            if (responseCode == 200) {
                response = serverResponse.body();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getRequestedProjectId() {
        return requestedProjectId;
    }

    public int getRequestedFileId() {
        return requestedFileId;
    }

    public static @NotNull GetModFileRequest getModFileUrl(int projectId, int fileId) throws IOException {
        ForgeURL url = ForgeURL.newBuilder("v1", "mods", String.valueOf(projectId),
                "files", String.valueOf(fileId), "download-url").build();

        return new GetModFileRequest(url, projectId, fileId);
    }
}
