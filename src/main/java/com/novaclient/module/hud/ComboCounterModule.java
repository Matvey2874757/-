package com.novaclient.module.hud;

import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;

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
        drawLine(context, "Combo: " + combo, 0, 0xFFFFFFFF);
    }
}
