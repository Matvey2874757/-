package com.novaclient.module.hud;

import com.novaclient.module.BooleanSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class CoordinatesModule extends HudTextModule {
    public final BooleanSetting showYaw = addSetting(new BooleanSetting("Show Yaw", true));

    public CoordinatesModule() {
        super("Coordinates", "X/Y/Z и направление", 8, 48);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null) return;
        var p = ClientRefs.MC.player;
        drawLine(context, String.format("XYZ: %.1f %.1f %.1f", p.getX(), p.getY(), p.getZ()), 0, 0xFFFFFFFF);
        if (showYaw.get()) {
            drawLine(context, "Yaw: " + Math.round(p.getYaw()), 1, 0xFFFFFFFF);
        }
    }
}
