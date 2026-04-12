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
            drawLine(context, "Reach: -", 0, 0xFFFFFFFF);
            return;
        }
        double reach = ClientRefs.MC.player.distanceTo(ClientRefs.MC.targetedEntity);
        maxReach = Math.max(maxReach, reach);
        drawLine(context, String.format("Reach: %.2f", reach), 0, 0xFFFFFFFF);
        drawLine(context, String.format("Max: %.2f", maxReach), 1, 0xFFBBBBBB);
    }
}
