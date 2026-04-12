package com.novaclient.module.render;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public final class HitColorModule extends Module {
    public final NumberSetting red = addSetting(new NumberSetting("Red", 255, 0, 255));
    public final NumberSetting green = addSetting(new NumberSetting("Green", 80, 0, 255));
    public final NumberSetting blue = addSetting(new NumberSetting("Blue", 80, 0, 255));
    public final NumberSetting alpha = addSetting(new NumberSetting("Alpha", 180, 0, 255));
    public final NumberSetting size = addSetting(new NumberSetting("Size", 14, 4, 40));

    public HitColorModule() {
        super("Hit Color", "Цветной визуальный эффект удара", Category.RENDER);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (!(ClientRefs.MC.targetedEntity instanceof LivingEntity living) || living.hurtTime <= 0) return;
        int color = (clamp(alpha) << 24) | (clamp(red) << 16) | (clamp(green) << 8) | clamp(blue);
        int cx = ClientRefs.MC.getWindow().getScaledWidth() / 2;
        int cy = ClientRefs.MC.getWindow().getScaledHeight() / 2;
        int s = size.get().intValue();
        context.fill(cx - s, cy - s, cx + s, cy + s, color);
    }

    private int clamp(NumberSetting setting) {
        return MathHelper.clamp(setting.get().intValue(), 0, 255);
    }
}
