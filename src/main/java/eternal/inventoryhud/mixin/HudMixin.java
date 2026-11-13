package eternal.inventoryhud.mixin;

import eternal.inventoryhud.client.render.ArmorBar;
import eternal.inventoryhud.client.render.PotionTimer;
import eternal.inventoryhud.client.render.TotemCounter;
import eternal.inventoryhud.client.config.Config;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
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

    @Shadow
    protected abstract void extractSlot(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed);

    @Inject(method = "extractItemHotbar", at = @At("TAIL"))
    private void extractItemHotbar(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (Config.INSTANCE.getArmorBar()) ArmorBar.extract(graphics, deltaTracker, this::extractSlot);
        if (Config.INSTANCE.getTotemCounter()) TotemCounter.extract(graphics);
    }

    @Inject(method = "extractEffects", at = @At("HEAD"), cancellable = true)
    private void extractEffects(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (Config.INSTANCE.getPotionTimer()) {
            ci.cancel();
            PotionTimer.extract(graphics);
        }
    }

    @ModifyArgs(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void modifyAttackIndicatorBackgroundX(Args args) {
        if (Config.INSTANCE.getArmorBar() && HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE.equals(args.get(1))) {
            args.set(2, ArmorBar.attackIndicatorX(args.get(2)));
        }
    }

    @ModifyArgs(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V"))
    private void modifyAttackIndicatorProgressX(Args args) {
        if (Config.INSTANCE.getArmorBar() && HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE.equals(args.get(1))) {
            args.set(6, ArmorBar.attackIndicatorX(args.get(6)));
        }
    }
}
