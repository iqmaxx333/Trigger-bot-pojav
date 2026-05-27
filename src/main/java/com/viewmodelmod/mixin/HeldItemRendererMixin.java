package com.viewmodelmod.mixin;

import com.viewmodelmod.animation.SwingAnimationHelper;
import com.viewmodelmod.config.ViewmodelConfig;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    // ─── HAND POSITION ────────────────────────────────────────────────────────

    /**
     * Intercept renderFirstPersonItem and apply custom hand offsets + rotations
     * on top of whatever vanilla has already set up.
     */
    @Inject(
        method = "renderFirstPersonItem",
        at = @At("HEAD")
    )
    private void onRenderFirstPersonItem(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Arm arm,
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
        boolean isRight = (arm == Arm.RIGHT);

        float x    = isRight ? cfg.rightHandX    : cfg.leftHandX;
        float y    = isRight ? cfg.rightHandY    : cfg.leftHandY;
        float z    = isRight ? cfg.rightHandZ    : cfg.leftHandZ;
        float rotX = isRight ? cfg.rightHandRotX : cfg.leftHandRotX;
        float rotY = isRight ? cfg.rightHandRotY : cfg.leftHandRotY;
        float rotZ = isRight ? cfg.rightHandRotZ : cfg.leftHandRotZ;
        float scale= isRight ? cfg.rightHandScale: cfg.leftHandScale;

        // Translation
        matrices.translate(x, y, z);

        // Rotation
        if (rotX != 0f) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotX));
        if (rotY != 0f) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotY));
        if (rotZ != 0f) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotZ));

        // Scale
        if (scale != 1f) matrices.scale(scale, scale, scale);
    }

    // ─── SWING ANIMATION ──────────────────────────────────────────────────────

    /**
     * Modify the swingProgress argument passed into the actual arm rendering method
     * so our eased swing value replaces vanilla's linear one.
     */
    @Inject(
        method = "renderFirstPersonItem",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Arm;)V",
                 shift = At.Shift.BEFORE),
        cancellable = false
    )
    private void beforeRenderArm(
            AbstractClientPlayerEntity player,
            float tickDelta, float pitch, Arm arm,
            float swingProgress, ItemStack item, float equipProgress,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            CallbackInfo ci
    ) {
        // swingProgress is a local; we handle rotation in the swing mixin below
    }

    /**
     * Apply custom swing rotation axes.
     * Called from applySwingOffset (private) during hand rendering.
     */
    @Inject(
        method = "applySwingOffset",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onApplySwingOffset(MatrixStack matrices, Arm arm, float swingProgress, CallbackInfo ci) {
        if (!ViewmodelConfig.INSTANCE.enableCustomAnimations) return;

        ViewmodelConfig cfg = ViewmodelConfig.INSTANCE;
        float eased = SwingAnimationHelper.computeSwing(swingProgress);

        // Cancel vanilla and apply our own
        ci.cancel();

        // Direction multiplier: right arm swings +Z, left -Z
        float dir = (arm == Arm.RIGHT) ? 1f : -1f;

        float sq  = eased * eased;

        // X: downward punch
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-80f * cfg.swingRotX * eased));
        // Y: sideways snap
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(dir * 20f * cfg.swingRotY * sq));
        // Z: roll / twist
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(dir * -20f * cfg.swingRotZ * eased));
    }
}
