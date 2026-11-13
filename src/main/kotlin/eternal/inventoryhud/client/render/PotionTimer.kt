package eternal.inventoryhud.client.render

import eternal.inventoryhud.client.IMinecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.Hud
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.effect.MobEffectInstance
import java.util.*
import kotlin.math.max

object PotionTimer : IMinecraft {

    private val EFFECT_BACKGROUND_SPRITE = Identifier.fromNamespaceAndPath("inventoryhud-", "textures/effect_background.png")

    @JvmStatic
    fun extract(graphics: GuiGraphicsExtractor) {
        if (client.gui.screen() != null && client.gui.screen()!!.showsActiveEffects()) return
        val player = client.player ?: return

        var good = 0
        var bad = 0
        val top = if (client.isDemo) 16 else 1
        for (effect in player.activeEffects.filter { it.showIcon() }.sortedDescending()) {
            val beneficial = effect.effect.value().isBeneficial
            val column = if (beneficial) good++ else bad++

            val x = graphics.guiWidth() - 37 - column * 38
            val y = top + (if (beneficial) 0 else 40)

            val level = (effect.amplifier + 1).toString()
            val duration = format(effect)

            graphics.blit(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SPRITE, x, y, 0.0f, 0.0f, 36, 38, 36, 38)
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Hud.getMobEffectSprite(effect.effect), x + 9, y + 3, 18, 18)
            graphics.text(client.font, level, x + 28 - client.font.width(level), y + 14, -0x1, true)
            graphics.text(client.font, duration, x + (36 - client.font.width(duration)) / 2, y + 25, -0x1, true)
        }
    }

    private fun format(effect: MobEffectInstance): String {
        if (effect.isInfiniteDuration) {
            return "**:**"
        }
        val seconds = max(0, effect.duration / 20)

        return String.format(Locale.ROOT, "%02d:%02d", seconds / 60, seconds % 60)
    }
}
