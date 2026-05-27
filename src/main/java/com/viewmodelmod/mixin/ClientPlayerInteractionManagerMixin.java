package com.viewmodelmod.mixin;

import com.viewmodelmod.config.ViewmodelConfig;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Optionally tweaks the hand swing duration on the client player,
 * which controls how many ticks one full swing lasts.
 *
 * Vanilla handSwingDuration = 6 ticks.
 * We multiply it by (1 / swingSpeed) so a higher speed = fewer ticks = faster.
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Inject(method = "interactItem", at = @At("HEAD"))
    private void beforeInteractItem(CallbackInfo ci) {
        // Nothing needed here — swing duration is handled via the HeldItemRenderer mixin
    }
}
