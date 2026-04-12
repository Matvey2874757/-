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
    private int hoveredButton = -1;
    private final List<MenuButton> menuButtons = new ArrayList<>();

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

    private static class MenuButton {
        int x, y, width, height;
        Text text;
        Runnable action;
        float hoverAlpha = 0f;
        
        MenuButton(int x, int y, int width, int height, Text text, Runnable action) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.text = text;
            this.action = action;
        }
        
        boolean isHovered(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
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
        
        // Create menu buttons
        menuButtons.clear();
        int cx = width / 2;
        int y = height / 2 - 50;
        int btnWidth = 200;
        int btnHeight = 36;
        int spacing = 8;
        
        menuButtons.add(new MenuButton(cx - btnWidth/2, y, btnWidth, btnHeight, 
            Text.literal("Одиночная игра"), () -> client.setScreen(new SelectWorldScreen(this))));
        menuButtons.add(new MenuButton(cx - btnWidth/2, y + btnHeight + spacing, btnWidth, btnHeight, 
            Text.literal("Сетевая игра"), () -> client.setScreen(new MultiplayerScreen(this))));
        menuButtons.add(new MenuButton(cx - btnWidth/2, y + (btnHeight + spacing) * 2, btnWidth, btnHeight, 
            Text.literal("Модули"), () -> client.setScreen(new ModuleScreen(this))));
        menuButtons.add(new MenuButton(cx - btnWidth/2, y + (btnHeight + spacing) * 3, btnWidth, btnHeight, 
            Text.literal("Настройки"), () -> client.setScreen(new OptionsScreen(this, client.options))));
        menuButtons.add(new MenuButton(cx - btnWidth/2, y + (btnHeight + spacing) * 4, btnWidth, btnHeight, 
            Text.literal("Выход"), () -> MinecraftClient.getInstance().scheduleStop()));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        anim = Math.min(1f, anim + delta * 0.05f);
        
        // Deep space background gradient - fully opaque (alpha 255)
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
        
        // Subtle nebula effect - reduced opacity to prevent blur overlay
        float nx = (mouseX - width / 2f) / width * 20;
        float ny = (mouseY - height / 2f) / height * 20;
        context.fillGradient(-60 + (int)nx, -60 + (int)ny, width / 2 + 60 + (int)nx, height / 2 + 60 + (int)ny,
                ColorHelper.Argb.getArgb(25, 50, 30, 100), ColorHelper.Argb.getArgb(5, 0, 0, 0));

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

        // Render beautiful buttons
        int buttonIndex = 0;
        for (MenuButton btn : menuButtons) {
            boolean hovered = btn.isHovered(mouseX, mouseY);
            
            // Smooth hover animation
            if (hovered) {
                btn.hoverAlpha = Math.min(1f, btn.hoverAlpha + delta * 0.15f);
            } else {
                btn.hoverAlpha = Math.max(0f, btn.hoverAlpha - delta * 0.15f);
            }
            
            // Button background with gradient
            int baseAlpha = 160;
            int hoverBoost = (int)(btn.hoverAlpha * 30);
            int borderAlpha = (int)(80 + btn.hoverAlpha * 120);
            
            // Outer glow - reduced intensity
            int glowSize = (int)(3 + btn.hoverAlpha * 5);
            for (int g = glowSize; g > 0; g--) {
                int glowAlpha = (int)(20 * btn.hoverAlpha * (1f - (float)g / glowSize));
                int glowColor = ColorHelper.Argb.getArgb(glowAlpha, 100, 180, 255);
                context.fill(btn.x - g, btn.y - g, btn.x + btn.width + g, btn.y + btn.height + g, glowColor);
            }
            
            // Main button gradient - more transparent
            int topColor = ColorHelper.Argb.getArgb(baseAlpha + hoverBoost, 30 + (int)(btn.hoverAlpha * 30), 40 + (int)(btn.hoverAlpha * 40), 60 + (int)(btn.hoverAlpha * 60));
            int bottomColor = ColorHelper.Argb.getArgb(baseAlpha + hoverBoost, 20 + (int)(btn.hoverAlpha * 20), 30 + (int)(btn.hoverAlpha * 30), 50 + (int)(btn.hoverAlpha * 50));
            context.fillGradient(btn.x, btn.y, btn.x + btn.width, btn.y + btn.height, topColor, bottomColor);
            
            // Border - less intense
            int borderColor = ColorHelper.Argb.getArgb(borderAlpha, 80 + (int)(btn.hoverAlpha * 100), 150 + (int)(btn.hoverAlpha * 105), 255);
            context.fill(btn.x - 1, btn.y - 1, btn.x + btn.width + 1, btn.y, borderColor); // Top
            context.fill(btn.x - 1, btn.y + btn.height, btn.x + btn.width + 1, btn.y + btn.height + 1, borderColor); // Bottom
            context.fill(btn.x - 1, btn.y, btn.x, btn.y + btn.height, borderColor); // Left
            context.fill(btn.x + btn.width, btn.y, btn.x + btn.width + 1, btn.y + btn.height, borderColor); // Right
            
            // Inner highlight - reduced
            int highlightAlpha = (int)(30 * btn.hoverAlpha);
            int highlightColor = ColorHelper.Argb.getArgb(highlightAlpha, 255, 255, 255);
            context.fill(btn.x + 2, btn.y + 2, btn.x + btn.width - 2, btn.y + 8, highlightColor);
            
            // Button text
            int textColor = ColorHelper.Argb.getArgb(255, 255, 255, 255);
            int textX = btn.x + btn.width / 2;
            int textY = btn.y + btn.height / 2 - textRenderer.fontHeight / 2;
            context.drawCenteredTextWithShadow(textRenderer, btn.text, textX, textY, textColor);
            
            buttonIndex++;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (MenuButton btn : menuButtons) {
            if (btn.isHovered((int)mouseX, (int)mouseY)) {
                btn.action.run();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Empty method to prevent rendering blurred world background
    }
    
    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        // Reinitialize stars and comets on resize
        init();
    }
}
