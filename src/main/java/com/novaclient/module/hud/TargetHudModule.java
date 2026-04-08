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
        drawLine(context, "Target: " + living.getName().getString(), 0, 0xFFFFFFFF);
        drawLine(context, String.format("HP: %.1f/%.1f", living.getHealth(), living.getMaxHealth()), 1, 0xFFFF6666);
    }
}
