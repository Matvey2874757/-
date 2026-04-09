package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class DirectionCompassModule extends HudTextModule {
    public DirectionCompassModule() {
        super("Direction", "Показывает направление взгляда", 8, 104);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (MinecraftClient.getInstance().textRenderer == null || ClientRefs.MC.player == null) return;
        float yaw = (ClientRefs.MC.player.getYaw() % 360 + 360) % 360;
        String dir = yaw >= 315 || yaw < 45 ? "South" : yaw < 135 ? "West" : yaw < 225 ? "North" : "East";
        context.fill(x - 2, y - 2, x + 108, y + 12, 0x90182742);
        drawLine(context, "Dir: " + dir, 0, 0xFFDDF0FF);
    }
}
