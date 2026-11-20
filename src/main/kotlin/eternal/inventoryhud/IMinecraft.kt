package eternal.inventoryhud

import net.minecraft.client.Minecraft

interface IMinecraft {

    val client: Minecraft
        get() = Minecraft.getInstance()
}
