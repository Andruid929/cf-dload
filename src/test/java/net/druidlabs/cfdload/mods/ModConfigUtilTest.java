package net.druidlabs.cfdload.mods;

import net.druidlabs.cfdload.io.Paths;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ModConfigUtilTest {

    private final ModConfigUtil TEST_CONFIG;

    {
        Path pathToMod = Paths.createPath("src", "test", "resources", "moreitems-1.9.1-1.21.11.jar");

        try {
            TEST_CONFIG = ModConfigUtil.readConfig(pathToMod);
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