package net.druidlabs.cfdload.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlugExtractorTest {

    @Test
    void testSlugger() {
        assertEquals("le-druid", SlugExtractor.getSlug("https://curseforge.com/mc-mods/counting/le-druid"));
        assertEquals("le\\druid", SlugExtractor.getSlug("https://curseforge.com/mc-mods/counting/le\\druid"));
        assertEquals("druid", SlugExtractor.getSlug("https://curseforge.com/mc-mods/counting/le/druid"));
    }

}