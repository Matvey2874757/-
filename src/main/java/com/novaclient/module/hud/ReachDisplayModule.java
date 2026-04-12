package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class ReachDisplayModule extends HudTextModule {
    private double maxReach;

    public ReachDisplayModule() {
        super("Reach Display", "Отображение дистанции до цели", 8, 148);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.targetedEntity == null || ClientRefs.MC.player == null) {
            String text = "Reach: -";
            int lineHeight = getTextHeight();
            int totalHeight = lineHeight;
            int maxWidth = getTextWidth(text);
            
            renderBackground(context, maxWidth, totalHeight);
            drawLine(context, text, 0, 0xFFAAAAAA);
            return;
        }
        
        double reach = ClientRefs.MC.player.distanceTo(ClientRefs.MC.targetedEntity);
        maxReach = Math.max(maxReach, reach);
        
        // Цвет зависит от дистанции
        int reachColor = reach > 3.5 ? 0xFFFF0000 : (reach > 3.0 ? 0xFFFFFF00 : 0xFF00FF00);
        
        String[] lines = new String[] {
            String.format("Reach: %.2f", reach),
            String.format("Max: %.2f", maxReach)
        };
        
        int lineHeight = getTextHeight();
        int totalHeight = lines.length * lineHeight;
        int maxWidth = 0;
        
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, getTextWidth(line));
        }
        
        renderBackground(context, maxWidth, totalHeight);
        drawLine(context, lines[0], 0, reachColor);
        drawLine(context, lines[1], 1, 0xFFBBBBBB);
    }
}
