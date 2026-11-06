package eternal.inventoryhud.client.service;

import net.minecraft.client.Minecraft;

public interface IMinecraft {

    Minecraft client = Minecraft.getInstance();

    static boolean nullCheck() {
        return client.player == null || client.level == null;
    }
}
