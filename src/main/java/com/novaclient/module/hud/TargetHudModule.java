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
        
        String name = living.getName().getString();
        float health = living.getHealth();
        float maxHealth = living.getMaxHealth();
        float healthPercent = (health / maxHealth) * 100f;
        
        // Цвет здоровья
        int healthColor = healthPercent > 50 ? 0xFF00FF00 : (healthPercent > 25 ? 0xFFFFFF00 : 0xFFFF0000);
        
        // Полоска здоровья
        int barWidth = 80;
        int barHeight = 4;
        int filledWidth = (int)((health / maxHealth) * barWidth);
        
        int startX = (int)(x.get().intValue() * scale.get());
        int startY = (int)(y.get().intValue() * scale.get());
        
        // Фон модуля
        renderBackground(context, Math.max(getTextWidth(name), barWidth), 30);
        
        // Имя цели
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            name,
            startX,
            startY,
            0xFFFFFFFF
        );
        
        // Фон полоски здоровья
        context.fill(startX, startY + 12, startX + barWidth, startY + 12 + barHeight, 0xFF303030);
        
        // Заполненная часть полоски
        context.fill(startX, startY + 12, startX + filledWidth, startY + 12 + barHeight, healthColor);
        
        // Граница полоски
        context.drawHorizontalLine(startX, startX + barWidth, startY + 12, 0xFF505050);
        context.drawHorizontalLine(startX, startX + barWidth, startY + 12 + barHeight, 0xFF505050);
        context.drawVerticalLine(startX, startY + 12, startY + 12 + barHeight, 0xFF505050);
        context.drawVerticalLine(startX + barWidth, startY + 12, startY + 12 + barHeight, 0xFF505050);
        
        // Текст здоровья
        String healthText = String.format("%.1f/%.1f", health, maxHealth);
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            healthText,
            startX,
            startY + 18,
            healthColor
        );
        
        // Дистанция
        if (ClientRefs.MC.player != null) {
            String distText = String.format("Dist: %.2f", ClientRefs.MC.player.distanceTo(living));
            context.drawTextWithShadow(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                distText,
                startX,
                startY + 28,
                0xFFAAAAAA
            );
        }
    }
}
