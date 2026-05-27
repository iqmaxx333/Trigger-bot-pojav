package com.viewmodelmod.animation;

import com.viewmodelmod.config.ViewmodelConfig;

/**
 * Provides eased progress values and utilities for the viewmodel swing animation.
 */
public class SwingAnimationHelper {

    /**
     * Apply a custom easing to vanilla's raw swing progress [0..1].
     * Produces a punchy "ease-out-back" feel.
     */
    public static float easeOutBack(float t) {
        final float c1 = 1.70158f;
        final float c3 = c1 + 1f;
        // standard ease-out-back formula
        return 1f + c3 * (float) Math.pow(t - 1f, 3) + c1 * (float) Math.pow(t - 1f, 2);
    }

    /**
     * Smooth ease-in-out cubic.
     */
    public static float easeInOutCubic(float t) {
        return t < 0.5f ? 4f * t * t * t : 1f - (float) Math.pow(-2f * t + 2f, 3) / 2f;
    }

    /**
     * Applies swing speed scaling: accelerates or decelerates the swing progress.
     * Values > 1 are faster, < 1 are slower.
     */
    public static float applySwingSpeed(float rawProgress) {
        float speed = ViewmodelConfig.INSTANCE.swingSpeed;
        if (speed == 1f) return rawProgress;
        // scale t and clamp to [0,1]
        return Math.min(1f, Math.max(0f, rawProgress * speed));
    }

    /**
     * Returns the final eased swing value used by the mixin.
     */
    public static float computeSwing(float rawProgress) {
        if (!ViewmodelConfig.INSTANCE.enableCustomAnimations) return rawProgress;
        float t = applySwingSpeed(rawProgress);
        return easeOutBack(t) * ViewmodelConfig.INSTANCE.swingIntensity;
    }

    /**
     * Sine-wave bob offset scaled by config intensity.
     */
    public static float computeBobOffset(float ticks, float speed) {
        ViewmodelConfig cfg = ViewmodelConfig.INSTANCE;
        if (!cfg.enableBob) return 0f;
        return (float) Math.sin(ticks * speed * cfg.bobSpeedMultiplier) * cfg.bobIntensity;
    }
}
