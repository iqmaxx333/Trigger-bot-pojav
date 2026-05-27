package com.viewmodelmod;

import com.viewmodelmod.config.ViewmodelConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class ViewmodelModClient implements ClientModInitializer {

    public static final String MOD_ID = "viewmodelmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static KeyBinding openConfigKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Viewmodel+ Swing Animation mod loaded!");

        // Load config
        ViewmodelConfig.load();

        // Register keybinding to open config screen (default: V key)
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.viewmodelmod.open_config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.viewmodelmod.keys"
        ));

        // Register tick event for keybinding
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openConfigKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new com.viewmodelmod.gui.ViewmodelConfigScreen(null));
                }
            }
        });

        LOGGER.info("Keybinding registered: Press V to open Viewmodel Config");
    }
}
