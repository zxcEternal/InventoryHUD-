package eternal.inventoryhud.injection.mixin.gui;

import com.llamalad7.mixinextras.sugar.Local;
import eternal.inventoryhud.client.service.render.HudRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Hud.class)
public abstract class HudMixin {

    @Shadow
    @Final
    private static Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE;

    @Shadow
    @Final
    private static Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE;

    @Unique
    private HudRenderer hudRenderer;

    @Shadow
    protected abstract void extractSlot(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(final CallbackInfo ci) {
        this.hudRenderer = new HudRenderer(this::extractSlot);
    }

    @Inject(method = "extractItemHotbar", at = @At("TAIL"))
    private void extractArmorHud(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker, final CallbackInfo ci) {
        this.hudRenderer.extractRenderState(graphics, deltaTracker);
    }

    @Inject(method = "extractEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V", shift = At.Shift.AFTER))
    private void extractEffectLevel(final GuiGraphicsExtractor graphics, final DeltaTracker deltaTracker, final CallbackInfo ci,
                                    @Local(name = "instance") final MobEffectInstance effect,
                                    @Local(name = "x") final int x, @Local(name = "y") final int y) {
        this.hudRenderer.extractEffectLevel(graphics, effect, x, y);
    }

    @ModifyArgs(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void modifyAttackIndicatorBackgroundX(final Args args) {
        if (HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE.equals(args.get(1))) {
            args.set(2, this.hudRenderer.getAttackIndicatorX(args.get(2)));
        }
    }

    @ModifyArgs(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V"))
    private void modifyAttackIndicatorProgressX(final Args args) {
        if (HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE.equals(args.get(1))) {
            args.set(6, this.hudRenderer.getAttackIndicatorX(args.get(6)));
        }
    }
}
