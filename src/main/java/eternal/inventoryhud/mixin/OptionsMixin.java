package eternal.inventoryhud.mixin;

import eternal.inventoryhud.config.Config;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {

	@Inject(method = "processOptions", at = @At("TAIL"))
	private void processOptions(Options.FieldAccess access, CallbackInfo ci) {
		Config.INSTANCE.process(access);
	}
}
