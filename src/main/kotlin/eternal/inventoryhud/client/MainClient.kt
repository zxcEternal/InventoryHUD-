package eternal.inventoryhud.client

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier

class MainClient : ClientModInitializer {

    override fun onInitializeClient() {
        config = Configurable()
        config.init()

        val category = KeyMapping.Category(Identifier.fromNamespaceAndPath("inventoryhud-", "main"))
        openConfigKey = KeyMappingHelper.registerKeyMapping(KeyMapping("key.inventoryhud-.open_config", InputConstants.Type.KEYBOARD, InputConstants.KEY_H, category))

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client ->
            while (openConfigKey.consumeClick()) client.setScreenAndShow(HudMenu(null, config))
        })
    }

    companion object {
        @JvmStatic
        lateinit var config: Configurable
        private lateinit var openConfigKey: KeyMapping
    }
}
