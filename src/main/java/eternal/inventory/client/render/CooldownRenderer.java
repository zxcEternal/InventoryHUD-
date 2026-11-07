package eternal.inventory.client.render;

import eternal.inventory.client.IMinecraft;
import eternal.inventory.mixin.item.CooldownInstanceAccessor;
import eternal.inventory.mixin.item.ItemCooldownsAccessor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class CooldownRenderer implements IMinecraft {

    public void extract(GuiGraphicsExtractor graphics, ItemStack itemStack, int x, int y) {
        if (client.player == null || itemStack.isEmpty()) return;

        ItemCooldowns cooldowns = client.player.getCooldowns();
        if (!cooldowns.isOnCooldown(itemStack) || !(cooldowns instanceof ItemCooldownsAccessor accessor)) return;

        Identifier group = cooldowns.getCooldownGroup(itemStack);
        if (!(accessor.cooldowns().get(group) instanceof CooldownInstanceAccessor entry)) return;

        String time = format((entry.getEndTime() - accessor.tickCount()) / 20.0f);
        graphics.text(client.font, time, x + 17 - client.font.width(time), y, 0xFFFFFFFF, true);
    }

    private static String format(float seconds) {
        if (seconds < 1.0f) {
            return String.format(Locale.ROOT, "%.1f", seconds);
        }
        return String.valueOf((int) Math.ceil(seconds));
    }
}
