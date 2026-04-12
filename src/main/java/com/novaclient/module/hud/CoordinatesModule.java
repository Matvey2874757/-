package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class CoordinatesModule extends HudTextModule {
    public CoordinatesModule() {
        super("Coordinates", "X/Y/Z координаты", 8, 48);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null) return;
        
        String line = String.format("§eX: §f%.1f §eY: §f%.1f §eZ: §f%.1f", 
            ClientRefs.MC.player.getX(), 
            ClientRefs.MC.player.getY(), 
            ClientRefs.MC.player.getZ());
        
        int lineHeight = getTextHeight();
        int totalHeight = lineHeight;
        int maxWidth = getTextWidth(line.replace("§", ""));
        
        renderBackground(context, maxWidth, totalHeight);
        
        // Рисуем с форматированием
        int startX = (int)(x.get().intValue() * scale.get());
        int startY = (int)(y.get().intValue() * scale.get());
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            line,
            startX,
            startY,
            0xFFFFFFFF
        );
    }
}
