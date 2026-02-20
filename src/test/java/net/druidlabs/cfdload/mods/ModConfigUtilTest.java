package net.druidlabs.cfdload.mods;

import net.druidlabs.cfdload.TestConstants;
import net.druidlabs.cfdload.errorhandling.ErrorLogger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModConfigUtilTest {

    private final ModConfigUtil TEST_CONFIG;

    {
        try {
            TEST_CONFIG = ModConfigUtil.readConfig(TestConstants.PATH_TO_TEST_RESOURCES.resolve("moreitems-1.9.1-1.21.11.jar"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getModName() {
        assertEquals("Andruid's items", TEST_CONFIG.getModName());
    }

    @Test
    void getModId() {
        assertEquals("moreitems", TEST_CONFIG.getModId());
    }

    @Test
    void getModVersion() {
        assertEquals("1.9.1-1.21.11", TEST_CONFIG.getModVersion());
    }

    @Test
    void getModLoaderId() {
        assertEquals(4, TEST_CONFIG.getModLoaderId());
    }
}