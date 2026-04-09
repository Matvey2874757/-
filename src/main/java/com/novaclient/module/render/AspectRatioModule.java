package com.novaclient.module.render;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class AspectRatioModule extends Module {
    public final NumberSetting ratio = addSetting(new NumberSetting("Aspect Ratio", 2.35, 1.0, 2.39));

    public AspectRatioModule() {
        super("Aspect Ratio", "Кинематографическое соотношение сторон", Category.RENDER);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.getWindow() == null) {
            return;
        }

        int width = ClientRefs.MC.getWindow().getScaledWidth();
        int height = ClientRefs.MC.getWindow().getScaledHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        double currentRatio = (double) width / (double) height;
        double targetRatio = ratio.get();

        if (targetRatio > currentRatio) {
            int targetHeight = (int) (width / targetRatio);
            int bar = Math.max(0, (height - targetHeight) / 2);
            if (bar > 0) {
                context.fill(0, 0, width, bar, 0xE0000000);
                context.fill(0, height - bar, width, height, 0xE0000000);
            }
        } else if (targetRatio < currentRatio) {
            int targetWidth = (int) (height * targetRatio);
            int bar = Math.max(0, (width - targetWidth) / 2);
            if (bar > 0) {
                context.fill(0, 0, bar, height, 0xE0000000);
                context.fill(width - bar, 0, width, height, 0xE0000000);
            }
        }
    }
}
