package net.druidlabs.cfdload.api.response;

public final class FileIndex {

    public static final String FILE_INDEXES_MEMBER_NAME = "latestFilesIndexes";

    private final String gameVersion;
    private final String filename;
    private final int fileId;
    private final int releaseType;
    private final int gameVersionTypeId;
    private final int modLoaderId;

    private FileIndex(String gameVersion, int fileId, String filename, int releaseType, int gameVersionTypeId, int modLoaderId) {
        this.gameVersion = gameVersion;
        this.fileId = fileId;
        this.filename = filename;
        this.releaseType = releaseType;
        this.gameVersionTypeId = gameVersionTypeId;
        this.modLoaderId = modLoaderId;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getFilename() {
        return filename;
    }

    public int getFileId() {
        return fileId;
    }

    public int getReleaseType() {
        return releaseType;
    }

    public int getGameVersionTypeId() {
        return gameVersionTypeId;
    }

    public int getModLoaderId() {
        return modLoaderId;
    }
}
