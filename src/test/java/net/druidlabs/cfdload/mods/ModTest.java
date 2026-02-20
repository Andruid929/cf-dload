package net.druidlabs.cfdload.mods;

import net.druidlabs.cfdload.TestConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModTest {

    private final Mod mod = Mod.getInfo(TestConstants.PATH_TO_TEST_RESOURCES.resolve("moreitems-1.9.1-1.21.11.jar"));

    @Test
    void getModName() {
        assertEquals("Andruid's items", mod.getModName());
    }

    @Test
    void getModFilename() {
        assertEquals("moreitems-1.9.1-1.21.11.jar", mod.getModFilename());
    }

    @Test
    void getModId() {
        assertEquals("moreitems", mod.getModId());
    }

    @Test
    void getModVersion() {
        assertEquals("1.9.1-1.21.11", mod.getModVersion());
    }

    @Test
    void getModLoader() {
        assertEquals(ModLoader.FABRIC, mod.getModLoader());
        assertEquals(4, mod.getModLoaderId());
    }
}