package eternal.inventoryhud.client;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Configurable implements AutoCloseable {
    private static final ConfigRenderOptions RENDER_OPTIONS = ConfigRenderOptions.defaults()
            .setOriginComments(false)
            .setComments(true)
            .setFormatted(true)
            .setJson(false);

    private final Path configFile;
    private boolean dirty;

    private boolean armorEnabled = false;
    private boolean cooldownEnabled = false;
    private boolean potionEnabled = false;
    private boolean totemEnabled = false;

    public Configurable() {
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve("inventoryhud-");
        this.configFile = configDir.resolve("default.conf");
        try {
            Files.createDirectories(configDir);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create InventoryHUD config directory", exception);
        }
    }

    public void init() {
        if (!Files.exists(configFile)) {
            saveConfig();
            return;
        }

        try {
            Config config = ConfigFactory.parseFile(configFile.toFile());
            armorEnabled = getBoolean(config, "elements.armor", armorEnabled);
            cooldownEnabled = getBoolean(config, "elements.cooldown", cooldownEnabled);
            potionEnabled = getBoolean(config, "elements.potions", potionEnabled);
            totemEnabled = getBoolean(config, "elements.totem", totemEnabled);
            dirty = false;
        } catch (Exception exception) {
            System.err.println("[InventoryHUD-] Failed to load config: " + exception.getMessage());
        }
    }

    private static boolean getBoolean(Config config, String path, boolean fallback) {
        return config.hasPath(path) ? config.getBoolean(path) : fallback;
    }

    public boolean isArmorEnabled() {
        return armorEnabled;
    }
    public boolean isCooldownEnabled() {
        return cooldownEnabled;
    }
    public boolean isPotionEnabled() {
        return potionEnabled;
    }
    public boolean isTotemEnabled() {
        return totemEnabled;
    }

    public void setArmorEnabled(boolean enabled) {
        armorEnabled = enabled;
        dirty = true;
        saveConfig();
    }
    public void setCooldownEnabled(boolean enabled) {
        cooldownEnabled = enabled;
        dirty = true;
        saveConfig();
    }
    public void setPotionEnabled(boolean enabled) {
        potionEnabled = enabled;
        dirty = true;
        saveConfig();
    }
    public void setTotemEnabled(boolean enabled) {
        totemEnabled = enabled;
        dirty = true;
        saveConfig();
    }

    public void saveConfig() {
        Map<String, Object> elements = new LinkedHashMap<>();

        elements.put("armor", armorEnabled);
        elements.put("cooldown", cooldownEnabled);
        elements.put("potions", potionEnabled);
        elements.put("totem", totemEnabled);

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("elements", elements);

        try {
            Config config = ConfigFactory.parseMap(root);
            Path temporary = configFile.resolveSibling(configFile.getFileName() + ".tmp");

            Files.writeString(temporary, config.root().render(RENDER_OPTIONS), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(temporary, configFile, StandardCopyOption.REPLACE_EXISTING);

            dirty = false;
        } catch (IOException exception) {
            System.err.println("[InventoryHUD-] Failed to save config: " + exception.getMessage());
        }
    }

    @Override
    public void close() {
        if (dirty) saveConfig();
    }
}
