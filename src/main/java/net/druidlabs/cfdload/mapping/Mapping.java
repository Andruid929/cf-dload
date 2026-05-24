package net.druidlabs.cfdload.mapping;

public record Mapping(String modName, String modSlug, String modId, int projectId) {

    public static final String MAPPINGS_FILENAME = "Mappings.json";

}
