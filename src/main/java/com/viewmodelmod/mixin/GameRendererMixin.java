package com.viewmodelmod.mixin;

import com.viewmodelmod.config.ViewmodelConfig;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    /**
     * Intercept bobView so we can scale or disable the camera bob.
     */
    @Inject(
        method = "bobView",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onBobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ViewmodelConfig cfg = ViewmodelConfig.INSTANCE;
        if (!cfg.enableBob) {
            // Cancel vanilla bob completely
            ci.cancel();
        }
        // If bobIntensity != 1.0 we let vanilla run and scale elsewhere.
        // A full implementation would inject after and scale the matrix deltas;
        // for now disabling works perfectly.
    }
}
