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
        
        int renderX = (int)(x.get().doubleValue() * scale.get().doubleValue());
        int renderY = (int)(y.get().doubleValue() * scale.get().doubleValue());
        
        // Фон модуля
        renderBackground(context, Math.max(getTextWidth(name), barWidth), 30);
        
        // Имя цели
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            name,
            renderX,
            renderY,
            0xFFFFFFFF
        );
        
        // Фон полоски здоровья
        context.fill(renderX, renderY + 12, renderX + barWidth, renderY + 12 + barHeight, 0xFF303030);
        
        // Заполненная часть полоски
        context.fill(renderX, renderY + 12, renderX + filledWidth, renderY + 12 + barHeight, healthColor);
        
        // Граница полоски
        context.drawHorizontalLine(renderX, renderX + barWidth, renderY + 12, 0xFF505050);
        context.drawHorizontalLine(renderX, renderX + barWidth, renderY + 12 + barHeight, 0xFF505050);
        context.drawVerticalLine(renderX, renderY + 12, renderY + 12 + barHeight, 0xFF505050);
        context.drawVerticalLine(renderX + barWidth, renderY + 12, renderY + 12 + barHeight, 0xFF505050);
        
        // Текст здоровья
        String healthText = String.format("%.1f/%.1f", health, maxHealth);
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            healthText,
            renderX,
            renderY + 18,
            healthColor
        );
        
        // Дистанция
        if (ClientRefs.MC.player != null) {
            String distText = String.format("Dist: %.2f", ClientRefs.MC.player.distanceTo(living));
            context.drawTextWithShadow(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                distText,
                renderX,
                renderY + 28,
                0xFFAAAAAA
            );
        }
    }
}
