package com.viewmodelmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class ViewmodelConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("viewmodelmod.json");

    public static ViewmodelConfig INSTANCE = new ViewmodelConfig();

    // ─── RIGHT HAND POSITION ───────────────────────────────────────────────────
    public float rightHandX        =  0.56f;   // horizontal offset (positive = right)
    public float rightHandY        = -0.52f;   // vertical offset   (negative = lower)
    public float rightHandZ        = -0.72f;   // depth             (negative = closer)
    public float rightHandRotX     =  0.0f;    // pitch rotation (degrees)
    public float rightHandRotY     =  0.0f;    // yaw rotation   (degrees)
    public float rightHandRotZ     =  0.0f;    // roll rotation  (degrees)
    public float rightHandScale    =  1.0f;    // scale

    // ─── LEFT HAND POSITION ────────────────────────────────────────────────────
    public float leftHandX         = -0.56f;
    public float leftHandY         = -0.52f;
    public float leftHandZ         = -0.72f;
    public float leftHandRotX      =  0.0f;
    public float leftHandRotY      =  0.0f;
    public float leftHandRotZ      =  0.0f;
    public float leftHandScale     =  1.0f;

    // ─── SWING ANIMATION ──────────────────────────────────────────────────────
    public float swingSpeed        =  1.0f;    // swing speed multiplier (1.0 = vanilla)
    public float swingIntensity    =  1.0f;    // swing arc intensity    (1.0 = vanilla)
    public float swingRotX         =  1.0f;    // X-axis rotation scale
    public float swingRotY         =  0.4f;    // Y-axis rotation scale
    public float swingRotZ         =  0.4f;    // Z-axis rotation scale

    // ─── VIEWMODEL BOB ────────────────────────────────────────────────────────
    public boolean enableBob       = true;
    public float bobIntensity      = 1.0f;     // 0.0 = no bob, 1.0 = vanilla
    public float bobSpeedMultiplier = 1.0f;

    // ─── GENERAL ──────────────────────────────────────────────────────────────
    public boolean enableCustomAnimations = true;
    public String preset              = "custom"; // vanilla / custom / fps / rpg

    // ──────────────────────────────────────────────────────────────────────────

    public static void load() {
        if (CONFIG_PATH.toFile().exists()) {
            try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                INSTANCE = GSON.fromJson(reader, ViewmodelConfig.class);
                if (INSTANCE == null) INSTANCE = new ViewmodelConfig();
            } catch (IOException e) {
                INSTANCE = new ViewmodelConfig();
            }
        } else {
            INSTANCE = new ViewmodelConfig();
            save();
        }
    }

    public static void save() {
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Apply a named preset. */
    public void applyPreset(String name) {
        preset = name;
        switch (name) {
            case "fps" -> {
                rightHandX = 0.50f; rightHandY = -0.45f; rightHandZ = -0.60f;
                leftHandX  = -0.50f; leftHandY = -0.45f; leftHandZ = -0.60f;
                swingSpeed = 1.4f; swingIntensity = 0.7f;
                bobIntensity = 0.3f;
            }
            case "rpg" -> {
                rightHandX = 0.62f; rightHandY = -0.60f; rightHandZ = -0.80f;
                leftHandX  = -0.62f; leftHandY = -0.60f; leftHandZ = -0.80f;
                swingSpeed = 0.8f; swingIntensity = 1.4f;
                bobIntensity = 1.3f;
            }
            case "vanilla" -> {
                rightHandX = 0.56f; rightHandY = -0.52f; rightHandZ = -0.72f;
                leftHandX  = -0.56f; leftHandY = -0.52f; leftHandZ = -0.72f;
                swingSpeed = 1.0f; swingIntensity = 1.0f;
                bobIntensity = 1.0f;
                rightHandRotX = 0; rightHandRotY = 0; rightHandRotZ = 0;
                leftHandRotX  = 0; leftHandRotY  = 0; leftHandRotZ  = 0;
            }
        }
    }
}
