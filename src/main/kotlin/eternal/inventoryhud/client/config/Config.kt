package eternal.inventoryhud.client.config

import com.google.gson.GsonBuilder
import java.nio.file.Files
import java.nio.file.Path
import net.fabricmc.loader.api.FabricLoader

object Config {
    var armorBar = true
    var cooldownTimer = true
    var potionTimer = true
    var totemCounter = true

    private val file: Path
        get() = FabricLoader.getInstance().configDir.resolve("inventoryhud-.json")

    fun load() {
        if (!Files.exists(file)) return

        runCatching {
            Files.newBufferedReader(file).use { reader ->
                Gson.fromJson(reader, Data::class.java)?.let {
                    armorBar = it.armorBar
                    cooldownTimer = it.cooldownTimer
                    potionTimer = it.potionTimer
                    totemCounter = it.totemCounter
                }
            }
        }
    }

    fun save() {
        runCatching {
            Files.createDirectories(file.parent)
            Files.newBufferedWriter(file).use { writer ->
                Gson.toJson(Data(armorBar, cooldownTimer, potionTimer, totemCounter), writer)
            }
        }
    }

    private data class Data(
        val armorBar: Boolean = true,
        val cooldownTimer: Boolean = true,
        val potionTimer: Boolean = true,
        val totemCounter: Boolean = true
    )

    private val Gson = GsonBuilder().setPrettyPrinting().create()
}
