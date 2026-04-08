package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class CoordinatesModule extends HudTextModule {
    public CoordinatesModule() {
        super("Coordinates", "X/Y/Z и направление", 8, 48);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null) return;
        var p = ClientRefs.MC.player;
        drawLine(context, String.format("XYZ: %.1f %.1f %.1f", p.getX(), p.getY(), p.getZ()), 0, 0xFFFFFFFF);
        drawLine(context, "Yaw: " + Math.round(p.getYaw()), 1, 0xFFFFFFFF);
    }
}
