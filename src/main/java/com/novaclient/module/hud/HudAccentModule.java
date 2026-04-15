package com.novaclient.module.hud;

import com.novaclient.module.BooleanSetting;
import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class HudAccentModule extends Module {
    public final BooleanSetting showFps = addSetting(new BooleanSetting("Show FPS", true));
    public final NumberSetting opacity = addSetting(new NumberSetting("Opacity", 0.65, 0.2, 1.0));

    public HudAccentModule() {
        super("HUD Accent", "Мини-панель с настраиваемыми параметрами", Category.HUD);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (MinecraftClient.getInstance().textRenderer == null) {
            return;
        }
        int alpha = (int) (opacity.get() * 255.0);
        int x = 8;
        int y = 168;
        context.fill(x - 2, y - 2, x + 132, y + 12, (alpha << 24) | 0x1A2744);
        String txt = showFps.get() ? "HUD Accent • FPS: " + MinecraftClient.getInstance().getCurrentFps() : "HUD Accent • Active";
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, txt, x, y, 0xFFE4F2FF);
    }
}
