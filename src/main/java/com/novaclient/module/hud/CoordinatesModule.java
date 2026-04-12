package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class CoordinatesModule extends HudTextModule {
    public CoordinatesModule() {
        super("Coordinates", "X/Y/Z координаты и Yaw", 8, 48);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null) return;
        
        String line1 = String.format("§eX: §f%.1f §eY: §f%.1f §eZ: §f%.1f", 
            ClientRefs.MC.player.getX(), 
            ClientRefs.MC.player.getY(), 
            ClientRefs.MC.player.getZ());
        
        float yaw = ClientRefs.MC.player.getYaw();
        String line2 = String.format("§eYaw: §f%.1f°", yaw);
        
        int lineHeight = getTextHeight();
        int totalHeight = lineHeight * 2;
        int maxWidth = Math.max(getTextWidth(line1.replace("§", "")), getTextWidth(line2.replace("§", "")));
        
        renderBackground(context, maxWidth, totalHeight);
        
        // Рисуем с форматированием
        int renderX = (int)(x.get().doubleValue() * scale.get().doubleValue());
        int renderY = (int)(y.get().doubleValue() * scale.get().doubleValue());
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            line1,
            renderX,
            renderY,
            0xFFFFFFFF
        );
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            line2,
            renderX,
            renderY + lineHeight,
            0xFFFFFFFF
        );
    }
}
