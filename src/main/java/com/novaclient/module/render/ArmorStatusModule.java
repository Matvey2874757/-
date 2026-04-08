package com.novaclient.module.render;

import com.novaclient.module.hud.HudTextModule;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class ArmorStatusModule extends HudTextModule {
    public ArmorStatusModule() {
        super("Armor Status", "Износ брони в процентах", 8, 78);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null) return;
        int line = 0;
        for (var stack : ClientRefs.MC.player.getArmorItems()) {
            if (!stack.isDamageable()) continue;
            int max = stack.getMaxDamage();
            int left = max - stack.getDamage();
            int pct = (int) ((left * 100.0) / max);
            drawLine(context, stack.getName().getString() + ": " + pct + "%", line++, 0xFFFFFFFF);
        }
    }
}
