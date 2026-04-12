package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;

public final class TargetHudModule extends HudTextModule {
    public TargetHudModule() {
        super("Target HUD", "Имя и здоровье цели", 8, 168);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (!(ClientRefs.MC.targetedEntity instanceof LivingEntity living)) return;
        
        String[] lines = new String[] {
            "Target: " + living.getName().getString(),
            String.format("HP: %.1f/%.1f", living.getHealth(), living.getMaxHealth())
        };
        
        if (ClientRefs.MC.player != null) {
            lines = java.util.Arrays.copyOf(lines, lines.length + 1);
            lines[lines.length - 1] = String.format("Dist: %.2f", ClientRefs.MC.player.distanceTo(living));
        }
        
        int lineHeight = getTextHeight();
        int totalHeight = lines.length * lineHeight;
        int maxWidth = 0;
        
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, getTextWidth(line));
        }
        
        renderBackground(context, maxWidth, totalHeight);
        
        drawLine(context, lines[0], 0, 0xFFFFFFFF);
        drawLine(context, lines[1], 1, 0xFFFF6666);
        if (lines.length > 2) {
            drawLine(context, lines[2], 2, 0xFFAAAAFF);
        }
    }
}
