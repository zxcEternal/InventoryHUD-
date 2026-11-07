package eternal.inventoryhud.mixin.gui;

import eternal.inventoryhud.client.render.CooldownRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {

    @Unique private final CooldownRenderer cooldownRenderer = new CooldownRenderer();

    @Inject(method = "itemCooldown(Lnet/minecraft/world/item/ItemStack;II)V", at = @At("TAIL"))
    private void itemCooldown(final ItemStack itemStack, final int x, final int y, final CallbackInfo ci) {
        this.cooldownRenderer.extract((GuiGraphicsExtractor) (Object) this, itemStack, x, y);
    }
}
