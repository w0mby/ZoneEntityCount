package net.mobcount.infrastructure;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Path;

public class ConfigManager {

    public static final String SUBDIRECTORY = "fabric-mobcount";
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve(SUBDIRECTORY);
    public static final boolean MustDrawOverlay = false;
}