package com.novaclient.module.render;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.BlockHitResult;

public final class BlockOutlineModule extends Module {
    public final NumberSetting width = addSetting(new NumberSetting("Line Width", 2, 1, 6));
    public final NumberSetting red = addSetting(new NumberSetting("Red", 102, 0, 255));
    public final NumberSetting green = addSetting(new NumberSetting("Green", 204, 0, 255));
    public final NumberSetting blue = addSetting(new NumberSetting("Blue", 255, 0, 255));

    public BlockOutlineModule() {
        super("Block Outline", "Настраиваемая обводка блока", Category.RENDER);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (!(ClientRefs.MC.crosshairTarget instanceof BlockHitResult)) return;
        int thickness = width.get().intValue();
        int cx = ClientRefs.MC.getWindow().getScaledWidth() / 2;
        int cy = ClientRefs.MC.getWindow().getScaledHeight() / 2;
        int size = 8;
        int color = (170 << 24)
                | (red.get().intValue() << 16)
                | (green.get().intValue() << 8)
                | blue.get().intValue();

        context.fill(cx - size, cy - size, cx + size, cy - size + thickness, color);
        context.fill(cx - size, cy + size - thickness, cx + size, cy + size, color);
        context.fill(cx - size, cy - size, cx - size + thickness, cy + size, color);
        context.fill(cx + size - thickness, cy - size, cx + size, cy + size, color);
    }
}
