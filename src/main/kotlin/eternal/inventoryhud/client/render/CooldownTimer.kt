package eternal.inventoryhud.client.render

import eternal.inventoryhud.client.IMinecraft
import eternal.inventoryhud.mixin.CooldownInstanceAccessor
import eternal.inventoryhud.mixin.ItemCooldownsAccessor
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.world.item.ItemStack
import java.util.*
import kotlin.math.ceil

object CooldownTimer : IMinecraft {

    @JvmStatic
    fun extract(graphics: GuiGraphicsExtractor, itemStack: ItemStack, x: Int, y: Int) {
        val player = client.player ?: return

        val cooldowns = player.cooldowns.takeIf { it.isOnCooldown(itemStack) } as? ItemCooldownsAccessor ?: return
        val entry = cooldowns.cooldowns()[player.cooldowns.getCooldownGroup(itemStack)] as? CooldownInstanceAccessor ?: return
        val time = format((entry.endTime - cooldowns.tickCount()) / 20.0f)

        graphics.text(client.font, time, x + 17 - client.font.width(time), y, -0x1, true)
    }

    private fun format(seconds: Float): String {
        if (seconds < 1.0f) return String.format(Locale.ROOT, "%.1f", seconds)

        return ceil(seconds.toDouble()).toInt().toString()
    }
}
