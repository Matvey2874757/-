package com.novaclient.module.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class ClockHudModule extends HudTextModule {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ClockHudModule() {
        super("Clock", "Текущее время на экране", 8, 88);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (MinecraftClient.getInstance().textRenderer == null) return;
        String value = "Time: " + LocalTime.now().format(FORMATTER);
        context.fill(x - 2, y - 2, x + 108, y + 12, 0x90182742);
        drawLine(context, value, 0, 0xFFDDF0FF);
    }
}
