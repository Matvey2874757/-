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
        
        String[] lines = new String[] {
            "W: " + key(ClientRefs.MC.options.forwardKey.isPressed()),
            "A: " + key(ClientRefs.MC.options.leftKey.isPressed()) + " S: " + key(ClientRefs.MC.options.backKey.isPressed()) + " D: " + key(ClientRefs.MC.options.rightKey.isPressed()),
            "LMB: " + key(ClientRefs.MC.options.attackKey.isPressed()) + " RMB: " + key(ClientRefs.MC.options.useKey.isPressed())
        };
        
        if (showCps.get()) {
            lines = java.util.Arrays.copyOf(lines, lines.length + 1);
            lines[lines.length - 1] = "CPS: " + (ClientRefs.MC.options.attackKey.isPressed() ? "~" + demoCps.get().intValue() : "0");
        }
        
        int lineHeight = getTextHeight();
        int totalHeight = lines.length * lineHeight;
        int maxWidth = 0;
        
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, getTextWidth(line));
        }
        
        renderBackground(context, maxWidth, totalHeight);
        
        for (int i = 0; i < lines.length; i++) {
            int color = i == lines.length - 1 && showCps.get() ? 0xFFAAAAAA : 0xFFFFFFFF;
            drawLine(context, lines[i], i, color);
        }
    }

    private String key(boolean down) {
        return down ? "§a●" : "§7○";
    }
}
