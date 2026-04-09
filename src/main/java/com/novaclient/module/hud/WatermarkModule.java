package com.novaclient.module.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class WatermarkModule extends HudTextModule {
    public WatermarkModule() {
        super("Watermark", "Логотип клиента в HUD", 8, 72);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (MinecraftClient.getInstance().textRenderer == null) return;
        context.fill(x - 2, y - 2, x + 108, y + 12, 0xA0182742);
        context.fill(x - 2, y - 2, x + 108, y - 1, 0xFF63B3FF);
        drawLine(context, "NovaClient 1.21.1", 0, 0xFFEAF5FF);
    }
}
