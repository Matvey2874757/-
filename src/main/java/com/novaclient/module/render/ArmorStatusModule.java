package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ArmorItem;

public final class ArmorStatusModule extends HudTextModule {
    public ArmorStatusModule() {
        super("Armor Status", "Износ брони в процентах", 8, 78);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null) return;
        
        int iconSize = 16;
        int gap = 4;
        int totalWidth = iconSize * 4 + gap * 3;
        int totalHeight = iconSize + 10; // + место для процентов
        
        renderBackground(context, totalWidth, totalHeight);
        
        int renderX = (int)(x.get().doubleValue() * scale.get().doubleValue());
        int renderY = (int)(y.get().doubleValue() * scale.get().doubleValue());
        
        int slot = 0;
        for (var stack : ClientRefs.MC.player.getArmorItems()) {
            if (!stack.isDamageable() || !(stack.getItem() instanceof ArmorItem)) continue;
            
            int max = stack.getMaxDamage();
            int left = max - stack.getDamage();
            int pct = (int) ((left * 100.0) / max);
            
            int x = renderX + slot * (iconSize + gap);
            int y = renderY;
            
            // Рисуем иконку брони
            context.drawItem(stack, x, y);
            
            // Рисуем процент износа под иконкой
            String pctText = pct + "%";
            int textWidth = getTextWidth(pctText);
            context.drawTextWithShadow(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                pctText,
                x + (iconSize - textWidth) / 2,
                y + iconSize + 2,
                pct > 50 ? 0xFF00FF00 : (pct > 25 ? 0xFFFFFF00 : 0xFFFF0000)
            );
            
            slot++;
        }
    }
}
