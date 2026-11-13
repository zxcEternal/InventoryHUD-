package eternal.inventoryhud.client

import net.minecraft.client.Minecraft

interface IMinecraft {

    val client: Minecraft get() = Minecraft.getInstance()

    fun nullCheck(): Boolean {
        return client.player != null || client.level != null
    }
}