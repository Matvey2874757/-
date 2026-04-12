package com.novaclient.module.render;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public final class CustomCrosshairModule extends Module {
    public final NumberSetting size = addSetting(new NumberSetting("Size", 6, 2, 20));
    public final NumberSetting thickness = addSetting(new NumberSetting("Thickness", 2, 1, 6));
    public final NumberSetting red = addSetting(new NumberSetting("Red", 255, 0, 255));
    public final NumberSetting green = addSetting(new NumberSetting("Green", 255, 0, 255));
    public final NumberSetting blue = addSetting(new NumberSetting("Blue", 255, 0, 255));
    public final BooleanSetting animated = addSetting(new BooleanSetting("Animated", true));

    public CustomCrosshairModule() {
        super("Custom Crosshair", "Кастомный прицел", Category.RENDER);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.options == null || ClientRefs.MC.getWindow() == null) return;
        if (ClientRefs.MC.options.hudHidden) return;

        int cx = ClientRefs.MC.getWindow().getScaledWidth() / 2;
        int cy = ClientRefs.MC.getWindow().getScaledHeight() / 2;
        int len = Math.max(2, size.get().intValue());
        int thick = Math.max(1, thickness.get().intValue());
        int gap = animated.get() ? 2 + (int) (Math.sin(System.currentTimeMillis() / 180.0) * 1.5) : 2;
        int color = argb();

        context.fill(cx - thick, cy - gap - len, cx + thick, cy - gap, color);
        context.fill(cx - thick, cy + gap, cx + thick, cy + gap + len, color);
        context.fill(cx - gap - len, cy - thick, cx - gap, cy + thick, color);
        context.fill(cx + gap, cy - thick, cx + gap + len, cy + thick, color);
        context.fill(cx - 1, cy - 1, cx + 1, cy + 1, color);
    }

    private int argb() {
        int r = MathHelper.clamp(red.get().intValue(), 0, 255);
        int g = MathHelper.clamp(green.get().intValue(), 0, 255);
        int b = MathHelper.clamp(blue.get().intValue(), 0, 255);
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }
}
