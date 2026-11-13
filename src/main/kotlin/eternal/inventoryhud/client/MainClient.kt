package eternal.inventoryhud.client

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier

class MainClient : ClientModInitializer {

    companion object {
        lateinit var instance: MainClient private set
    }

    lateinit var configurable: Configurable
    lateinit var menu: Menu
    lateinit var key: KeyMapping

    override fun onInitializeClient() {
        instance = this

        this.configurable = Configurable()
        this.menu = Menu(null, this.configurable)
        this.key = KeyMappingHelper.registerKeyMapping(
            KeyMapping("key.inventoryhud-.open_config", InputConstants.Type.KEYBOARD, InputConstants.KEY_H,
            KeyMapping.Category(Identifier.fromNamespaceAndPath("inventoryhud-", "main")))
        )

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client ->
            while (key.consumeClick()) client.setScreenAndShow(menu)
        })
    }
}
