package eternal.inventoryhud.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public final class Configurable implements AutoCloseable {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private final Path configFile;
    private boolean dirty;

    private boolean armorEnabled = false;
    private boolean cooldownEnabled = false;
    private boolean potionEnabled = false;
    private boolean totemEnabled = false;

    public Configurable() {
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve("inventoryhud-");
        this.configFile = configDir.resolve("default.json");
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
            String jsonContent = Files.readString(configFile, StandardCharsets.UTF_8);
            JsonElement parsed = JsonParser.parseString(jsonContent);

            if (parsed.isJsonObject()) {
                JsonObject root = parsed.getAsJsonObject();
                JsonObject elements = root.has("elements") && root.get("elements").isJsonObject()
                        ? root.getAsJsonObject("elements")
                        : new JsonObject();

                armorEnabled = getBoolean(elements, "armor", armorEnabled);
                cooldownEnabled = getBoolean(elements, "cooldown", cooldownEnabled);
                potionEnabled = getBoolean(elements, "potions", potionEnabled);
                totemEnabled = getBoolean(elements, "totem", totemEnabled);
            }
            dirty = false;
        } catch (Exception exception) {
            System.err.println("[InventoryHUD-] Failed to load config: " + exception.getMessage());
        }
    }

    private static boolean getBoolean(JsonObject object, String key, boolean fallback) {
        if (object.has(key) && object.get(key).isJsonPrimitive() && object.get(key).getAsJsonPrimitive().isBoolean()) {
            return object.get(key).getAsBoolean();
        }
        return fallback;
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
        JsonObject elements = new JsonObject();
        elements.addProperty("armor", armorEnabled);
        elements.addProperty("cooldown", cooldownEnabled);
        elements.addProperty("potions", potionEnabled);
        elements.addProperty("totem", totemEnabled);

        JsonObject root = new JsonObject();
        root.add("elements", elements);

        try {
            Path temporary = configFile.resolveSibling(configFile.getFileName() + ".tmp");

            Files.writeString(
                    temporary,
                    GSON.toJson(root),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
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
