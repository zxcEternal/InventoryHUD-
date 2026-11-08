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
 
    private var armorEnabled = false
    private var cooldownEnabled = false
    private var potionEnabled = false
    private var totemEnabled = false

    var isArmorEnabled: Boolean
        get() = armorEnabled
        set(value) {
            armorEnabled = value
            dirty = true
            saveConfig()
        }
 
    var isCooldownEnabled: Boolean
        get() = cooldownEnabled
        set(value) {
            cooldownEnabled = value
            dirty = true
            saveConfig()
        }
 
    var isPotionEnabled: Boolean
        get() = potionEnabled
        set(value) {
            potionEnabled = value
            dirty = true
            saveConfig()
        }
 
    var isTotemEnabled: Boolean
        get() = totemEnabled
        set(value) {
            totemEnabled = value
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
    }
 
    fun init() {
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

                armorEnabled = elements.getBoolean("armor", armorEnabled)
                cooldownEnabled = elements.getBoolean("cooldown", cooldownEnabled)
                potionEnabled = elements.getBoolean("potions", potionEnabled)
                totemEnabled = elements.getBoolean("totem", totemEnabled)
            }
            dirty = false
        } catch (exception: Exception) {
            System.err.println("[InventoryHUD-] Failed to load config: ${exception.message}")
        }
    }
 
    fun saveConfig() {
        val elements = JsonObject().apply {
            addProperty("armor", armorEnabled)
            addProperty("cooldown", cooldownEnabled)
            addProperty("potions", potionEnabled)
            addProperty("totem", totemEnabled)
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
