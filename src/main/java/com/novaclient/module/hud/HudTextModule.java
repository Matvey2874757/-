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
        int scaledX = (int)(x.get().intValue() * scale.get());
        int scaledY = (int)(y.get().intValue() + line * 10 * scale.get());
        context.drawTextWithShadow(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                text,
                scaledX,
                scaledY,
                color
        );
    }

    protected void renderBackground(DrawContext context, int width, int height) {
        int bgAlpha = backgroundColor.get().intValue();
        int borderAlpha = borderColor.get().intValue();
        
        // Рисуем фон с закругленными углами (имитация)
        context.fill(
            (int)(x.get().intValue() * scale.get()) - 2,
            (int)(y.get().intValue() * scale.get()) - 2,
            (int)(x.get().intValue() * scale.get()) + width + 2,
            (int)(y.get().intValue() * scale.get()) + height + 2,
            (bgAlpha << 24) | 0x101010
        );
        
        // Рисуем границу
        context.drawHorizontalLine(
            (int)(x.get().intValue() * scale.get()) - 2,
            (int)(x.get().intValue() * scale.get()) + width + 2,
            (int)(y.get().intValue() * scale.get()) - 2,
            (borderAlpha << 24) | 0x303030
        );
        context.drawHorizontalLine(
            (int)(x.get().intValue() * scale.get()) - 2,
            (int)(x.get().intValue() * scale.get()) + width + 2,
            (int)(y.get().intValue() * scale.get()) + height + 2,
            (borderAlpha << 24) | 0x303030
        );
        context.drawVerticalLine(
            (int)(x.get().intValue() * scale.get()) - 2,
            (int)(y.get().intValue() * scale.get()) - 2,
            (int)(y.get().intValue() * scale.get()) + height + 2,
            (borderAlpha << 24) | 0x303030
        );
        context.drawVerticalLine(
            (int)(x.get().intValue() * scale.get()) + width + 2,
            (int)(y.get().intValue() * scale.get()) - 2,
            (int)(y.get().intValue() * scale.get()) + height + 2,
            (borderAlpha << 24) | 0x303030
        );
    }

    protected int getTextWidth(String text) {
        if (net.minecraft.client.MinecraftClient.getInstance().textRenderer == null) return 0;
        return net.minecraft.client.MinecraftClient.getInstance().textRenderer.getWidth(text);
    }

    protected int getTextHeight() {
        return 10;
    }
}
