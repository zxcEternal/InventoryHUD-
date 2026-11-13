package eternal.inventoryhud.client

import eternal.inventoryhud.client.config.Config
import net.fabricmc.api.ClientModInitializer

class MainClient : ClientModInitializer {

    override fun onInitializeClient() {
        Config.load()
    }
}
