package com.novaclient.module.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class SessionStatsModule extends HudTextModule {
    private final long sessionStart = System.currentTimeMillis();

    public SessionStatsModule() {
        super("Session Stats", "Показывает длительность сессии", 8, 152);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (MinecraftClient.getInstance().textRenderer == null) return;
        long sec = (System.currentTimeMillis() - sessionStart) / 1000L;
        long min = sec / 60;
        long rem = sec % 60;
        context.fill(x - 2, y - 2, x + 108, y + 12, 0x90182742);
        drawLine(context, String.format("Session: %02d:%02d", min, rem), 0, 0xFFDDF0FF);
    }
}
