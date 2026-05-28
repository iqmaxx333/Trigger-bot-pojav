package com.viewmodelmod.mixin;

import com.viewmodelmod.animation.SwingAnimationHelper;
import com.viewmodelmod.config.ViewmodelConfig;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Inject(
        method = "renderFirstPersonItem",
        at = @At("HEAD")
    )
    private void onRenderFirstPersonItem(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (!ViewmodelConfig.INSTANCE.enableCustomAnimations) return;

        ViewmodelConfig cfg = ViewmodelConfig.INSTANCE;
        boolean isRight = (hand == Hand.MAIN_HAND);

        float x    = isRight ? cfg.rightHandX    : cfg.leftHandX;
        float y    = isRight ? cfg.rightHandY    : cfg.leftHandY;
        float z    = isRight ? cfg.rightHandZ    : cfg.leftHandZ;
        float rotX = isRight ? cfg.rightHandRotX : cfg.leftHandRotX;
        float rotY = isRight ? cfg.rightHandRotY : cfg.leftHandRotY;
        float rotZ = isRight ? cfg.rightHandRotZ : cfg.leftHandRotZ;
        float scale= isRight ? cfg.rightHandScale: cfg.leftHandScale;

        matrices.translate(x, y, z);

        if (rotX != 0f) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotX));
        if (rotY != 0f) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotY));
        if (rotZ != 0f) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotZ));

        if (scale != 1f) matrices.scale(scale, scale, scale);
    }

    @Inject(
        method = "applySwingOffset",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onApplySwingOffset(MatrixStack matrices, Hand hand, float swingProgress, CallbackInfo ci) {
        if (!ViewmodelConfig.INSTANCE.enableCustomAnimations) return;

        ViewmodelConfig cfg = ViewmodelConfig.INSTANCE;
        float eased = SwingAnimationHelper.computeSwing(swingProgress);

        ci.cancel();

        float dir = (hand == Hand.MAIN_HAND) ? 1f : -1f;
        float sq  = eased * eased;

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80f * cfg.swingRotX * eased));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(dir * 20f * cfg.swingRotY * sq));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(dir * -20f * cfg.swingRotZ * eased));
    }
}
