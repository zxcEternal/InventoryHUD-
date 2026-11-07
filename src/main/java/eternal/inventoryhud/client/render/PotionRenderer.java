package eternal.inventoryhud.client.render;

import eternal.inventoryhud.client.IMinecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Comparator;
import java.util.Locale;

public class PotionRenderer implements IMinecraft {

    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath("inventoryhud-", "textures/potion_background.png");

    private static final int WIDTH = 36;
    private static final int HEIGHT = 38;

    public void extract(GuiGraphicsExtractor graphics) {
        if (client.player == null) return;
        if (client.gui.screen() != null && client.gui.screen().showsActiveEffects()) return;

        int beneficial = 0;
        int harmful = 0;
        int top = client.isDemo() ? 16 : 1;
        for (MobEffectInstance effect : client.player.getActiveEffects().stream().filter(MobEffectInstance::showIcon).sorted(Comparator.reverseOrder()).toList()) {
            boolean positive = effect.getEffect().value().isBeneficial();
            int column = positive ? beneficial++ : harmful++;
            int x = graphics.guiWidth() - WIDTH - 1 - column * (WIDTH + 2);
            int y = top + (positive ? 0 : HEIGHT + 2);
            extractEffect(graphics, effect, x, y);
        }
    }

    private void extractEffect(GuiGraphicsExtractor graphics, MobEffectInstance effect, int x, int y) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, x, y, 0.0f, 0.0f, WIDTH, HEIGHT, WIDTH, HEIGHT);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Hud.getMobEffectSprite(effect.getEffect()), x + 9, y + 3, 18, 18);

        String level = Integer.toString(effect.getAmplifier() + 1);
        graphics.text(client.font, level, x + WIDTH - 8 - client.font.width(level), y + 14, 0xFFFFFFFF, true);

        String duration = formatDuration(effect);
        graphics.text(client.font, duration, x + (WIDTH - client.font.width(duration)) / 2, y + 25, 0xFFFFFFFF, true);
    }

    private static String formatDuration(MobEffectInstance effect) {
        if (effect.isInfiniteDuration()) return "**:**";

        int seconds = Math.max(0, effect.getDuration() / 20);
        return String.format(Locale.ROOT, "%02d:%02d", seconds / 60, seconds % 60);
    }
}

