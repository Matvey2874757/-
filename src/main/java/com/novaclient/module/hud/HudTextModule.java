package com.novaclient.module.hud;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import net.minecraft.client.gui.DrawContext;

public abstract class HudTextModule extends Module {
    protected int x;
    protected int y;

    protected HudTextModule(String name, String description, int x, int y) {
        super(name, description, Category.HUD);
        this.x = x;
        this.y = y;
    }

    protected void drawLine(DrawContext context, String text, int line, int color) {
        if (net.minecraft.client.MinecraftClient.getInstance().textRenderer == null) return;
        context.drawTextWithShadow(net.minecraft.client.MinecraftClient.getInstance().textRenderer, text, x, y + line * 10, color);
    }
}
