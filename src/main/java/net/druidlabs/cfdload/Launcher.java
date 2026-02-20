package net.druidlabs.cfdload;

import net.druidlabs.cfdload.errorhandling.ErrorLogger;
import net.druidlabs.cfdload.io.Paths;
import net.druidlabs.cfdload.mods.ModConfigUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Launcher {

    public static void main(String[] args) throws InterruptedException {
        ErrorLogger.initiate();

        try {
            Path resourcesPath = Paths.createPath("src", "test", "resources");

            try (Stream<Path> resources = Files.walk(resourcesPath).filter(path -> path.toString().endsWith(".jar"))) {

                AtomicInteger count = new AtomicInteger(2);

                resources.forEach(path -> {
                    try {
                        ModConfigUtil util = ModConfigUtil.readConfig(path);

                        System.out.println("Details for mod");
                        System.out.println("Name: " + util.getModName());
                        System.out.println("ID: " + util.getModId());
                        System.out.println("Version: " + util.getModVersion());
                        System.out.println("Loader ID: " + util.getModLoaderId());

                        if (count.getAndDecrement() != 0) {
                            System.out.println();
                        }

                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                });
            }
        } catch (Exception e) {
            ErrorLogger.logError(e);
        }

        Thread.sleep(2000);
    }

}
