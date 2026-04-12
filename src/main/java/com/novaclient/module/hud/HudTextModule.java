package com.novaclient.module.hud;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import net.minecraft.client.gui.DrawContext;

public abstract class HudTextModule extends Module {
    public final NumberSetting x;
    public final NumberSetting y;
    public final NumberSetting scale;
    public final NumberSetting backgroundColor;
    public final NumberSetting borderColor;

    protected HudTextModule(String name, String description, int x, int y) {
        super(name, description, Category.HUD);
        this.x = addSetting(new NumberSetting("X", x, 0, 2000));
        this.y = addSetting(new NumberSetting("Y", y, 0, 2000));
        this.scale = addSetting(new NumberSetting("Scale", 1.0, 0.5, 2.0));
        this.backgroundColor = addSetting(new NumberSetting("Background Alpha", 180, 0, 255));
        this.borderColor = addSetting(new NumberSetting("Border Alpha", 220, 0, 255));
    }

    protected void drawLine(DrawContext context, String text, int line, int color) {
        if (net.minecraft.client.MinecraftClient.getInstance().textRenderer == null) return;
        int renderX = (int)(x.get().doubleValue() * scale.get().doubleValue());
        int renderY = (int)(y.get().doubleValue() + line * 10 * scale.get().doubleValue());
        context.drawTextWithShadow(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                text,
                renderX,
                renderY,
                color
        );
    }

    protected void renderBackground(DrawContext context, int width, int height) {
        int bgAlpha = backgroundColor.get().intValue();
        int borderAlpha = borderColor.get().intValue();
        
        int renderX = (int)(x.get().doubleValue() * scale.get().doubleValue());
        int renderY = (int)(y.get().doubleValue() * scale.get().doubleValue());
        
        // Рисуем основной фон (темный полупрозрачный)
        context.fill(renderX - 3, renderY - 3, renderX + width + 3, renderY + height + 3, 0xAA000000);
        
        // Рисуем внутреннюю рамку (более светлая)
        context.drawHorizontalLine(renderX - 3, renderX + width + 3, renderY - 3, (borderAlpha << 24) | 0x404040);
        context.drawHorizontalLine(renderX - 3, renderX + width + 3, renderY + height + 3, (borderAlpha << 24) | 0x404040);
        context.drawVerticalLine(renderX - 3, renderY - 3, renderY + height + 3, (borderAlpha << 24) | 0x404040);
        context.drawVerticalLine(renderX + width + 3, renderY - 3, renderY + height + 3, (borderAlpha << 24) | 0x404040);
        
        // Рисуем верхний акцент (синяя линия сверху для современного вида)
        context.drawHorizontalLine(renderX - 2, renderX + width + 2, renderY - 2, 0xFF3498DB);
    }

    protected int getTextWidth(String text) {
        if (net.minecraft.client.MinecraftClient.getInstance().textRenderer == null) return 0;
        return net.minecraft.client.MinecraftClient.getInstance().textRenderer.getWidth(text);
    }

    protected int getTextHeight() {
        return 10;
    }
}
