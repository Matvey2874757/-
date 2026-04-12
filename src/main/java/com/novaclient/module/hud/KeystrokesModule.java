package com.novaclient.module.hud;

import com.novaclient.module.BooleanSetting;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class KeystrokesModule extends HudTextModule {
    public final BooleanSetting showCps = addSetting(new BooleanSetting("Show CPS", true));
    public final NumberSetting demoCps = addSetting(new NumberSetting("Demo CPS", 8, 1, 20));

    public KeystrokesModule() {
        super("Keystrokes", "Показывает WASD, мышь и CPS", 8, 8);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.options == null) return;
        drawLine(context, "W: " + key(ClientRefs.MC.options.forwardKey.isPressed()), 0, 0xFFFFFFFF);
        drawLine(context, "A: " + key(ClientRefs.MC.options.leftKey.isPressed()) + " S: " + key(ClientRefs.MC.options.backKey.isPressed()) + " D: " + key(ClientRefs.MC.options.rightKey.isPressed()), 1, 0xFFFFFFFF);
        drawLine(context, "LMB: " + key(ClientRefs.MC.options.attackKey.isPressed()) + " RMB: " + key(ClientRefs.MC.options.useKey.isPressed()), 2, 0xFFFFFFFF);
        if (showCps.get()) {
            drawLine(context, "CPS: " + (ClientRefs.MC.options.attackKey.isPressed() ? "~" + demoCps.get().intValue() : "0"), 3, 0xFFAAAAAA);
        }
    }

    private String key(boolean down) {
        return down ? "§a●" : "§7○";
    }
}
