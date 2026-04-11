package net.novaclient.ui.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.novaclient.ui.widgets.ToggleButtonWidget;

public class MainMenuScreen extends Screen {
    private final Screen parent;
    private float animationProgress = 0.0f;
    private long startTime;

    public MainMenuScreen(Screen parent) {
        super(Text.of("NovaClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        startTime = System.currentTimeMillis();

        // Main buttons
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Singleplayer"), button -> {
            assert this.client != null;
            this.client.setScreen(parent);
        }).dimensions(centerX - 100, centerY + 10, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Multiplayer"), button -> {
            assert this.client != null;
            this.client.setScreen(parent);
        }).dimensions(centerX - 100, centerY + 35, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Options"), button -> {
            assert this.client != null;
            this.client.setScreen(parent);
        }).dimensions(centerX - 100, centerY + 60, 200, 20).build());

        // NovaClient buttons
        this.addDrawableChild(new ToggleButtonWidget(
            centerX - 100, centerY - 40, 200, 20,
            Text.literal("Modules"),
            button -> {
                assert this.client != null;
                this.client.setScreen(new ModulesScreen(this));
            }
        ));

        this.addDrawableChild(new ToggleButtonWidget(
            centerX - 100, centerY - 15, 200, 20,
            Text.literal("Config"),
            button -> {
                assert this.client != null;
                this.client.setScreen(new ConfigScreen(this));
            }
        ));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Animate background
        long elapsed = System.currentTimeMillis() - startTime;
        animationProgress = Math.min(1.0f, elapsed / 1000.0f);

        // Gradient background with animation
        int topColor = lerpColor(0xFF1a1a2e, 0xFF0f0f1a, animationProgress);
        int bottomColor = lerpColor(0xFF16213e, 0xFF1a1a2e, animationProgress);
        
        context.fillGradient(0, 0, width, height, topColor, bottomColor);

        // Draw title with shadow
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 30, 0xFFFFFF);
        
        // Draw subtitle
        String subtitle = "Legitimate Utility Client v1.0";
        context.drawCenteredTextWithShadow(this.textRenderer, subtitle, this.width / 2, 50, 0x8888FF);

        super.render(context, mouseX, mouseY, delta);
    }

    private int lerpColor(int color1, int color2, float progress) {
        int a1 = (color1 >> 24) & 0xFF, r1 = (color1 >> 16) & 0xFF, g1 = (color1 >> 8) & 0xFF, b1 = color1 & 0xFF;
        int a2 = (color2 >> 24) & 0xFF, r2 = (color2 >> 16) & 0xFF, g2 = (color2 >> 8) & 0xFF, b2 = color2 & 0xFF;
        
        int a = (int) (a1 + (a2 - a1) * progress);
        int r = (int) (r1 + (r2 - r1) * progress);
        int g = (int) (g1 + (g2 - g1) * progress);
        int b = (int) (b1 + (b2 - b1) * progress);
        
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
