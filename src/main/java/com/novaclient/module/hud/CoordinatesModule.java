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
        
        String[] lines = new String[] {
            String.format("XYZ: %.1f %.1f %.1f", p.getX(), p.getY(), p.getZ())
        };
        
        if (showYaw.get()) {
            lines = java.util.Arrays.copyOf(lines, lines.length + 1);
            lines[lines.length - 1] = "Yaw: " + Math.round(p.getYaw());
        }
        
        int lineHeight = getTextHeight();
        int totalHeight = lines.length * lineHeight;
        int maxWidth = 0;
        
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, getTextWidth(line));
        }
        
        renderBackground(context, maxWidth, totalHeight);
        
        for (int i = 0; i < lines.length; i++) {
            drawLine(context, lines[i], i, 0xFFFFFFFF);
        }
    }
}
