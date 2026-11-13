package eternal.inventoryhud.mixin;

import eternal.inventoryhud.client.MainClient;
import eternal.inventoryhud.client.render.CooldownTimer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {

    @Inject(method = "itemCooldown(Lnet/minecraft/world/item/ItemStack;II)V", at = @At("TAIL"))
    private void itemCooldown(ItemStack itemStack, int x, int y, CallbackInfo ci) {
        if (MainClient.Companion.getInstance().configurable.isCooldownTimerEnabled()) CooldownTimer.extract((GuiGraphicsExtractor) (Object) this, itemStack, x, y);
    }
}
