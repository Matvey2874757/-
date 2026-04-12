package com.novaclient.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CustomMainMenuScreen extends Screen {
    private float anim;
    private final Screen parent;
    private final List<Comet> comets = new ArrayList<>();
    private final List<Star> stars = new ArrayList<>();
    private final Random random = new Random();

    private static class Comet {
        float x, y, speedX, speedY, size, alpha;
        
        Comet(int width, int height) {
            reset(width, height);
        }
        
        void reset(int width, int height) {
            x = random.nextFloat() * width;
            y = random.nextFloat() * height;
            speedX = 0.5f + random.nextFloat() * 2f;
            speedY = 0.3f + random.nextFloat() * 1.5f;
            size = 2f + random.nextFloat() * 4f;
            alpha = 0.3f + random.nextFloat() * 0.5f;
        }
        
        void update(int width, int height) {
            x += speedX;
            y += speedY;
            if (x > width + 50 || y > height + 50) {
                reset(width, height);
                x = -50;
                y = -50;
            }
        }
    }
    
    private static class Star {
        float x, y, size, twinkleSpeed, twinkleOffset;
        
        Star(int width, int height, Random random) {
            x = random.nextFloat() * width;
            y = random.nextFloat() * height;
            size = 0.5f + random.nextFloat() * 1.5f;
            twinkleSpeed = 0.02f + random.nextFloat() * 0.03f;
            twinkleOffset = random.nextFloat() * (float)Math.PI * 2;
        }
    }

    public CustomMainMenuScreen(Screen parent) {
        super(Text.literal("NovaClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Initialize stars
        stars.clear();
        for (int i = 0; i < 150; i++) {
            stars.add(new Star(width, height, random));
        }
        
        // Initialize comets
        comets.clear();
        for (int i = 0; i < 8; i++) {
            comets.add(new Comet(width, height));
        }
        
        int cx = width / 2;
        int y = height / 2 - 40;
        addDrawableChild(ButtonWidget.builder(Text.literal("Одиночная игра"), b -> client.setScreen(new SelectWorldScreen(this))).dimensions(cx - 80, y, 160, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Сетевая игра"), b -> client.setScreen(new MultiplayerScreen(this))).dimensions(cx - 80, y + 24, 160, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Модули"), b -> client.setScreen(new ModuleScreen(this))).dimensions(cx - 80, y + 48, 160, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Настройки"), b -> client.setScreen(new OptionsScreen(this, client.options))).dimensions(cx - 80, y + 72, 160, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Выход"), b -> MinecraftClient.getInstance().scheduleStop()).dimensions(cx - 80, y + 96, 160, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        anim = Math.min(1f, anim + delta * 0.05f);
        
        // Deep space background gradient
        int bg1 = ColorHelper.Argb.getArgb(255, 5, 8, 20);
        int bg2 = ColorHelper.Argb.getArgb(255, 15, 20, 45);
        int bg3 = ColorHelper.Argb.getArgb(255, 25, 30, 60);
        context.fillGradient(0, 0, width, height / 2, bg1, bg2);
        context.fillGradient(0, height / 2, width, height, bg2, bg3);
        
        // Draw twinkling stars
        long time = System.currentTimeMillis();
        for (Star star : stars) {
            float twinkle = (MathHelper.sin(time * 0.003f * star.twinkleSpeed + star.twinkleOffset) + 1) * 0.5f;
            int starAlpha = (int)(twinkle * 180 + 50);
            int starColor = ColorHelper.Argb.getArgb(starAlpha, 255, 255, 255);
            context.fill((int)star.x, (int)star.y, (int)star.x + (int)star.size, (int)star.y + (int)star.size, starColor);
        }
        
        // Update and draw comets with trails
        for (Comet comet : comets) {
            comet.update(width, height);
            
            // Draw comet tail (gradient trail)
            int tailLength = 80;
            for (int i = 0; i < tailLength; i++) {
                float tailX = comet.x - comet.speedX * i * 2;
                float tailY = comet.y - comet.speedY * i * 2;
                float tailAlpha = comet.alpha * (1f - (float)i / tailLength) * 0.6f;
                float tailSize = comet.size * (1f - (float)i / tailLength);
                int tailColor = ColorHelper.Argb.getArgb((int)(tailAlpha * 255), 
                    (int)(100 + 155 * (1f - (float)i / tailLength)),
                    (int)(150 + 105 * (1f - (float)i / tailLength)),
                    255);
                context.fill((int)tailX, (int)tailY, (int)(tailX + tailSize * 3), (int)(tailY + tailSize), tailColor);
            }
            
            // Draw comet head (bright white/yellow)
            int headColor = ColorHelper.Argb.getArgb((int)(comet.alpha * 255), 255, 255, 200);
            context.fill((int)comet.x, (int)comet.y, (int)(comet.x + comet.size * 4), (int)(comet.y + comet.size * 2), headColor);
        }
        
        // Subtle nebula effect
        float nx = (mouseX - width / 2f) / width * 20;
        float ny = (mouseY - height / 2f) / height * 20;
        context.fillGradient(-60 + (int)nx, -60 + (int)ny, width / 2 + 60 + (int)nx, height / 2 + 60 + (int)ny,
                ColorHelper.Argb.getArgb(40, 50, 30, 100), ColorHelper.Argb.getArgb(10, 0, 0, 0));

        // Title text with glow effect
        int titleAlpha = (int)(anim * 255);
        // Glow effect
        for (int i = 3; i > 0; i--) {
            int glowAlpha = titleAlpha / (i * 3);
            context.drawCenteredTextWithShadow(textRenderer, "NovaClient", 
                width / 2 + i, 36 + i, ColorHelper.Argb.getArgb(glowAlpha, 100, 180, 255));
            context.drawCenteredTextWithShadow(textRenderer, "NovaClient", 
                width / 2 - i, 36 - i, ColorHelper.Argb.getArgb(glowAlpha, 100, 180, 255));
        }
        // Main title
        context.drawCenteredTextWithShadow(textRenderer, "NovaClient", width / 2, 36,
                ColorHelper.Argb.getArgb(titleAlpha, 255, 255, 255));
        context.drawCenteredTextWithShadow(textRenderer, "Fabric 1.21.1 | Легальный QoL клиент", width / 2, 52,
                ColorHelper.Argb.getArgb(titleAlpha, 180, 210, 255));

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
    
    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        // Reinitialize stars and comets on resize
        init();
    }
}
