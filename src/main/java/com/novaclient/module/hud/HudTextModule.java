package com.novaclient.module.hud;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import net.minecraft.client.gui.DrawContext;

public abstract class HudTextModule extends Module {
    public final NumberSetting x;
    public final NumberSetting y;

    protected HudTextModule(String name, String description, int x, int y) {
        super(name, description, Category.HUD);
        this.x = addSetting(new NumberSetting("X", x, 0, 2000));
        this.y = addSetting(new NumberSetting("Y", y, 0, 2000));
    }

    protected void drawLine(DrawContext context, String text, int line, int color) {
        if (net.minecraft.client.MinecraftClient.getInstance().textRenderer == null) return;
        context.drawTextWithShadow(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                text,
                x.get().intValue(),
                y.get().intValue() + line * 10,
                color
        );
    }
}
