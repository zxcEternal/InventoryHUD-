package eternal.inventoryhud.client

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.fabricmc.loader.api.FabricLoader
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

class Configurable : AutoCloseable {

    private val configFile: Path
    private var dirty = false

    private var armorBarEnabled = false
    private var cooldownTimerEnabled = false
    private var potionTimerEnabled = false
    private var totemCounterEnabled = false

    var isArmorBarEnabled: Boolean
        get() = armorBarEnabled
        set(value) {
            armorBarEnabled = value
            dirty = true
            saveConfig()
        }

    var isCooldownTimerEnabled: Boolean
        get() = cooldownTimerEnabled
        set(value) {
            cooldownTimerEnabled = value
            dirty = true
            saveConfig()
        }

    var isPotionTimerEnabled: Boolean
        get() = potionTimerEnabled
        set(value) {
            potionTimerEnabled = value
            dirty = true
            saveConfig()
        }

    var isTotemCounterEnabled: Boolean
        get() = totemCounterEnabled
        set(value) {
            totemCounterEnabled = value
            dirty = true
            saveConfig()
        }

    init {
        val configDir = FabricLoader.getInstance().configDir.resolve("inventoryhud-")
        configFile = configDir.resolve("default.json")

        try {
            Files.createDirectories(configDir)
        } catch (exception: IOException) {
            throw IllegalStateException("Unable to create InventoryHUD config directory", exception)
        }

        loadConfig()
    }

    private fun loadConfig() {
        if (!Files.exists(configFile)) {
            saveConfig()
            return
        }

        try {
            val jsonContent = Files.readString(configFile, StandardCharsets.UTF_8)
            val parsed = JsonParser.parseString(jsonContent)

            if (parsed.isJsonObject) {
                val root = parsed.asJsonObject
                val elements = if (root.has("elements") && root.get("elements").isJsonObject) {
                    root.getAsJsonObject("elements")
                } else {
                    JsonObject()
                }

                armorBarEnabled = elements.getBoolean("armor_bar", elements.getBoolean("armor", armorBarEnabled))
                cooldownTimerEnabled = elements.getBoolean("cooldown_timer", elements.getBoolean("cooldown", cooldownTimerEnabled))
                potionTimerEnabled = elements.getBoolean("potion_timer", elements.getBoolean("potions", potionTimerEnabled))
                totemCounterEnabled = elements.getBoolean("totem_counter", elements.getBoolean("totem", totemCounterEnabled))
            }

            dirty = false
        } catch (exception: Exception) {
            System.err.println("[InventoryHUD-] Failed to load config: ${exception.message}")
        }
    }

    private fun saveConfig() {
        val elements = JsonObject().apply {
            addProperty("armor_bar", armorBarEnabled)
            addProperty("cooldown_timer", cooldownTimerEnabled)
            addProperty("potion_timer", potionTimerEnabled)
            addProperty("totem_counter", totemCounterEnabled)
        }

        val root = JsonObject().apply {
            add("elements", elements)
        }

        try {
            val temporary = configFile.resolveSibling(configFile.fileName.toString() + ".tmp")

            Files.writeString(
                temporary,
                GSON.toJson(root),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
            Files.move(temporary, configFile, StandardCopyOption.REPLACE_EXISTING)

            dirty = false
        } catch (exception: IOException) {
            System.err.println("[InventoryHUD-] Failed to save config: ${exception.message}")
        }
    }

    override fun close() {
        if (dirty) saveConfig()
    }

    private fun JsonObject.getBoolean(key: String, fallback: Boolean): Boolean {
        val element = get(key)

        if (element != null && element.isJsonPrimitive && element.asJsonPrimitive.isBoolean) {
            return element.asBoolean
        }
        return fallback
    }

    private companion object {
        val GSON: Gson = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
    }
}
