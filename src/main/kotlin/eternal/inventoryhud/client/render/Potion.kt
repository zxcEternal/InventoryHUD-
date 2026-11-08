package eternal.inventoryhud.client.render

import eternal.inventoryhud.client.IMinecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.Hud
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.effect.MobEffectInstance
import java.util.*
import kotlin.math.max

object Potion : IMinecraft {

    private val EFFECT_BACKGROUND_SPRITE = Identifier.fromNamespaceAndPath("inventoryhud-", "textures/effect_background.png")

    private const val WIDTH = 36
    private const val HEIGHT = 38

    @JvmStatic
    fun extract(graphics: GuiGraphicsExtractor) {
        if (client.player == null) return
        if (client.gui.screen() != null && client.gui.screen()!!.showsActiveEffects()) return

        var good = 0
        var bad = 0
        val top = if (client.isDemo) 16 else 1
        for (effect in client.player!!.activeEffects.filter { it.showIcon() }.sortedDescending()) {
            val beneficial = effect.effect.value().isBeneficial
            val column = if (beneficial) good++ else bad++

            val x = graphics.guiWidth() - WIDTH - 1 - column * (WIDTH + 2)
            val y = top + (if (beneficial) 0 else HEIGHT + 2)

            val level = (effect.amplifier + 1).toString()
            val duration = format(effect)

            graphics.blit(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SPRITE, x, y, 0.0f, 0.0f, WIDTH, HEIGHT, WIDTH, HEIGHT)
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Hud.getMobEffectSprite(effect.effect), x + 9, y + 3, 18, 18)
            graphics.text(client.font, level, x + WIDTH - 8 - client.font.width(level), y + 14, -0x1, true)
            graphics.text(client.font, duration, x + (WIDTH - client.font.width(duration)) / 2, y + 25, -0x1, true)
        }
    }

    private fun format(effect: MobEffectInstance): String {
        if (effect.isInfiniteDuration) return "**:**"
        val seconds = max(0, effect.duration / 20)
        return String.format(Locale.ROOT, "%02d:%02d", seconds / 60, seconds % 60)
    }
}
