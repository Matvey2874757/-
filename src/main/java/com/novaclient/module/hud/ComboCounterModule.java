package com.novaclient.module.hud;

import net.minecraft.client.gui.DrawContext;

public final class ComboCounterModule extends HudTextModule {
    private int combo;

    public ComboCounterModule() {
        super("Combo Counter", "Счетчик попаданий подряд", 8, 128);
    }

    public void onHit() {
        combo++;
    }

    public void reset() {
        combo = 0;
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        drawLine(context, "Combo: " + combo, 0, 0xFFFFFFFF);
    }
}
