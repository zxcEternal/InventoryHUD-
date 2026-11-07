package eternal.inventory.mixin.gui;

import eternal.inventory.client.render.ArmorRenderer;
import eternal.inventory.client.render.PotionRenderer;
import eternal.inventory.client.render.TotemRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Hud.class)
public abstract class HudMixin {

    @Shadow @Final private static Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE;
    @Shadow @Final private static Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE;

    @Unique private ArmorRenderer armorRenderer;
    @Unique private PotionRenderer potionRenderer;
    @Unique private TotemRenderer totemRenderer;

    @Shadow protected abstract void extractSlot(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        this.armorRenderer = new ArmorRenderer(this::extractSlot);
        this.potionRenderer = new PotionRenderer();
        this.totemRenderer = new TotemRenderer();
    }

    @Inject(method = "extractItemHotbar", at = @At("TAIL"))
    private void extractItemHotbar(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        this.armorRenderer.extract(graphics, deltaTracker);
        this.totemRenderer.extract(graphics);
    }

    @Inject(method = "extractEffects", at = @At("HEAD"), cancellable = true)
    private void extractEffects(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        this.potionRenderer.extract(graphics);
        ci.cancel();
    }

    @ModifyArgs(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void modifyAttackIndicatorBackgroundX(Args args) {
        if (HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE.equals(args.get(1))) {
            args.set(2, this.armorRenderer.getAttackIndicatorX(args.get(2)));
        }
    }

    @ModifyArgs(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V"))
    private void modifyAttackIndicatorProgressX(Args args) {
        if (HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE.equals(args.get(1))) {
            args.set(6, this.armorRenderer.getAttackIndicatorX(args.get(6)));
        }
    }
}
