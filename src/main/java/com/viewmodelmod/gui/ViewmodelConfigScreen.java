package com.viewmodelmod.gui;

import com.viewmodelmod.config.ViewmodelConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

/**
 * In-game configuration screen for Viewmodel+ Swing Animation.
 * Open with the V key (configurable).
 *
 * Layout (scrollable):
 *  [Title]
 *  Section: Right Hand  |  Section: Left Hand
 *  Section: Swing Animation
 *  Section: Bob
 *  [Presets row]   [Save & Close]
 */
public class ViewmodelConfigScreen extends Screen {

    private final Screen parent;
    private int scrollOffset = 0;
    private static final int SLIDER_WIDTH = 160;
    private static final int SLIDER_HEIGHT = 20;
    private static final int GAP = 4;

    public ViewmodelConfigScreen(Screen parent) {
        super(Text.literal("§6Viewmodel+ §fConfig"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = 10;
        int y = 30;

        // ── Presets ──────────────────────────────────────────────────────────
        addButton(x, y, "§9Preset: Vanilla", btn -> applyPreset("vanilla"));
        addButton(x + 110, y, "§aPreset: FPS",    btn -> applyPreset("fps"));
        addButton(x + 210, y, "§dPreset: RPG",    btn -> applyPreset("rpg"));
        y += 26;

        // ── Right hand ───────────────────────────────────────────────────────
        y = addSectionTitle("§e── Right Hand ──", x, y);
        y = addSlider(x, y, "R.Hand X",   -1f, 1f,  ViewmodelConfig.INSTANCE.rightHandX,    v -> ViewmodelConfig.INSTANCE.rightHandX    = v);
        y = addSlider(x, y, "R.Hand Y",   -1f, 1f,  ViewmodelConfig.INSTANCE.rightHandY,    v -> ViewmodelConfig.INSTANCE.rightHandY    = v);
        y = addSlider(x, y, "R.Hand Z",   -1f, 1f,  ViewmodelConfig.INSTANCE.rightHandZ,    v -> ViewmodelConfig.INSTANCE.rightHandZ    = v);
        y = addSlider(x, y, "R.Rot X", -180f, 180f, ViewmodelConfig.INSTANCE.rightHandRotX, v -> ViewmodelConfig.INSTANCE.rightHandRotX = v);
        y = addSlider(x, y, "R.Rot Y", -180f, 180f, ViewmodelConfig.INSTANCE.rightHandRotY, v -> ViewmodelConfig.INSTANCE.rightHandRotY = v);
        y = addSlider(x, y, "R.Rot Z", -180f, 180f, ViewmodelConfig.INSTANCE.rightHandRotZ, v -> ViewmodelConfig.INSTANCE.rightHandRotZ = v);
        y = addSlider(x, y, "R.Scale",   0.1f, 2f,  ViewmodelConfig.INSTANCE.rightHandScale,v -> ViewmodelConfig.INSTANCE.rightHandScale = v);

        // Left hand (offset to the right)
        int x2 = this.width / 2 + 10;
        int y2 = 56;
        y2 = addSectionTitle("§e── Left Hand ──", x2, y2);
        y2 = addSlider(x2, y2, "L.Hand X",   -1f, 1f,  ViewmodelConfig.INSTANCE.leftHandX,    v -> ViewmodelConfig.INSTANCE.leftHandX    = v);
        y2 = addSlider(x2, y2, "L.Hand Y",   -1f, 1f,  ViewmodelConfig.INSTANCE.leftHandY,    v -> ViewmodelConfig.INSTANCE.leftHandY    = v);
        y2 = addSlider(x2, y2, "L.Hand Z",   -1f, 1f,  ViewmodelConfig.INSTANCE.leftHandZ,    v -> ViewmodelConfig.INSTANCE.leftHandZ    = v);
        y2 = addSlider(x2, y2, "L.Rot X", -180f, 180f, ViewmodelConfig.INSTANCE.leftHandRotX, v -> ViewmodelConfig.INSTANCE.leftHandRotX = v);
        y2 = addSlider(x2, y2, "L.Rot Y", -180f, 180f, ViewmodelConfig.INSTANCE.leftHandRotY, v -> ViewmodelConfig.INSTANCE.leftHandRotY = v);
        y2 = addSlider(x2, y2, "L.Rot Z", -180f, 180f, ViewmodelConfig.INSTANCE.leftHandRotZ, v -> ViewmodelConfig.INSTANCE.leftHandRotZ = v);
        y2 = addSlider(x2, y2, "L.Scale",  0.1f, 2f,   ViewmodelConfig.INSTANCE.leftHandScale,v -> ViewmodelConfig.INSTANCE.leftHandScale = v);

        y = Math.max(y, y2) + 8;

        // ── Swing ────────────────────────────────────────────────────────────
        y = addSectionTitle("§b── Swing Animation ──", x, y);
        y = addSlider(x, y, "Swing Speed",     0.1f, 3f,   ViewmodelConfig.INSTANCE.swingSpeed,     v -> ViewmodelConfig.INSTANCE.swingSpeed     = v);
        y = addSlider(x, y, "Swing Intensity", 0f,   3f,   ViewmodelConfig.INSTANCE.swingIntensity, v -> ViewmodelConfig.INSTANCE.swingIntensity = v);
        y = addSlider(x, y, "Swing Rot X",     0f,   3f,   ViewmodelConfig.INSTANCE.swingRotX,      v -> ViewmodelConfig.INSTANCE.swingRotX      = v);
        y = addSlider(x, y, "Swing Rot Y",     0f,   3f,   ViewmodelConfig.INSTANCE.swingRotY,      v -> ViewmodelConfig.INSTANCE.swingRotY      = v);
        y = addSlider(x, y, "Swing Rot Z",     0f,   3f,   ViewmodelConfig.INSTANCE.swingRotZ,      v -> ViewmodelConfig.INSTANCE.swingRotZ      = v);

        // ── Bob ──────────────────────────────────────────────────────────────
        y = addSectionTitle("§c── Camera Bob ──", x, y);
        addButton(x, y, ViewmodelConfig.INSTANCE.enableBob ? "§aBob: ON" : "§cBob: OFF",
                btn -> {
                    ViewmodelConfig.INSTANCE.enableBob = !ViewmodelConfig.INSTANCE.enableBob;
                    btn.setMessage(Text.literal(ViewmodelConfig.INSTANCE.enableBob ? "§aBob: ON" : "§cBob: OFF"));
                });
        y += 26;
        y = addSlider(x, y, "Bob Intensity",  0f, 2f, ViewmodelConfig.INSTANCE.bobIntensity,      v -> ViewmodelConfig.INSTANCE.bobIntensity      = v);
        y = addSlider(x, y, "Bob Speed",      0f, 3f, ViewmodelConfig.INSTANCE.bobSpeedMultiplier, v -> ViewmodelConfig.INSTANCE.bobSpeedMultiplier = v);

        // ── Save button ──────────────────────────────────────────────────────
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§a✔ Save & Close"),
                btn -> {
                    ViewmodelConfig.save();
                    this.close();
                })
                .dimensions(this.width / 2 - 75, this.height - 28, 150, 20)
                .build());
    }

    private void addButton(int x, int y, String label, ButtonWidget.PressAction action) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal(label), action)
                .dimensions(x, y, 100, 20)
                .build());
    }

    /** Returns the next Y below the title. */
    private int addSectionTitle(String text, int x, int y) {
        // Titles are just drawn in render(); reserve space
        return y + 14;
    }

    /** Generic float slider factory. Returns next Y. */
    private int addSlider(int x, int y, String label, float min, float max, float initial,
                          java.util.function.Consumer<Float> onChange) {
        float range = max - min;
        double initNorm = (initial - min) / range;
        this.addDrawableChild(new SliderWidget(x, y, SLIDER_WIDTH, SLIDER_HEIGHT,
                Text.literal(label + ": " + String.format("%.2f", initial)), initNorm) {
            @Override
            protected void updateMessage() {
                float v = min + (float) this.value * range;
                setMessage(Text.literal(label + ": " + String.format("%.2f", v)));
            }
            @Override
            protected void applyValue() {
                float v = min + (float) this.value * range;
                onChange.accept(v);
            }
        });
        return y + SLIDER_HEIGHT + GAP;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        ViewmodelConfig.save();
        this.client.setScreen(parent);
    }
}
