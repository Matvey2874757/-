package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class ComboCounterModule extends HudTextModule {
    public final NumberSetting resetDelayMs = addSetting(new NumberSetting("Reset Delay", 1600, 500, 5000));
    private int combo;
    private int lastTargetId = -1;
    private int lastHurtTime;
    private long lastHitAt;

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
    public void tick() {
        long resetDelay = resetDelayMs.get().longValue();
        if (!(ClientRefs.MC.targetedEntity instanceof LivingEntity living)) {
            if (System.currentTimeMillis() - lastHitAt > resetDelay) reset();
            return;
        }

        if (living.getId() != lastTargetId) {
            lastTargetId = living.getId();
            lastHurtTime = living.hurtTime;
            if (System.currentTimeMillis() - lastHitAt > resetDelay * 0.75) reset();
        }

        if (living.hurtTime > lastHurtTime) {
            onHit();
            lastHitAt = System.currentTimeMillis();
        }
        lastHurtTime = living.hurtTime;

        if (System.currentTimeMillis() - lastHitAt > resetDelay) {
            reset();
        }
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        String text = "Combo: " + combo;
        int lineHeight = getTextHeight();
        int totalHeight = lineHeight;
        int maxWidth = getTextWidth(text);
        
        renderBackground(context, maxWidth, totalHeight);
        drawLine(context, text, 0, 0xFFFFFFFF);
    }
}
