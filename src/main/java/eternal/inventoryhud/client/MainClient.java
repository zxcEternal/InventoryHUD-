package eternal.inventoryhud.client;

import net.fabricmc.api.ClientModInitializer;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class MainClient implements ClientModInitializer {
    public static Configurable config;
    private static KeyMapping openConfigKey;

    @Override
    public void onInitializeClient() {
        config = new Configurable();
        config.init();

        KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath("inventoryhud-", "main"));
        openConfigKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.inventoryhud-.open_config", InputConstants.Type.KEYBOARD, InputConstants.KEY_H, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKey.consumeClick()) {
                client.setScreenAndShow(new HudMenu(null, config));
            }
        });
    }
}
