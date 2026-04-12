package com.novaclient.module.hud;

import com.novaclient.module.BooleanSetting;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class KeystrokesModule extends HudTextModule {
    public final BooleanSetting showCps = addSetting(new BooleanSetting("Show CPS", true));
    public final NumberSetting demoCps = addSetting(new NumberSetting("Demo CPS", 8, 1, 20));
    
    private static final int BUTTON_SIZE = 14;
    private static final int GAP = 3;

    public KeystrokesModule() {
        super("Keystrokes", "Показывает WASD, мышь и CPS", 8, 8);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.options == null) return;
        
        int startX = (int)(x.get().intValue() * scale.get());
        int startY = (int)(y.get().intValue() * scale.get());
        
        // Рисуем фон
        int totalWidth = BUTTON_SIZE * 3 + GAP * 2;
        int totalHeight = BUTTON_SIZE * 3 + GAP * 2;
        if (showCps.get()) {
            totalHeight += 12;
        }
        
        renderBackground(context, totalWidth, totalHeight);
        
        // Row 1: W
        drawKeyButton(context, startX + BUTTON_SIZE + GAP, startY, ClientRefs.MC.options.forwardKey.isPressed(), "W");
        
        // Row 2: A S D
        drawKeyButton(context, startX, startY + BUTTON_SIZE + GAP, ClientRefs.MC.options.leftKey.isPressed(), "A");
        drawKeyButton(context, startX + BUTTON_SIZE + GAP, startY + BUTTON_SIZE + GAP, ClientRefs.MC.options.backKey.isPressed(), "S");
        drawKeyButton(context, startX + (BUTTON_SIZE + GAP) * 2, startY + BUTTON_SIZE + GAP, ClientRefs.MC.options.rightKey.isPressed(), "D");
        
        // Row 3: LMB RMB
        drawKeyButton(context, startX, startY + (BUTTON_SIZE + GAP) * 2, ClientRefs.MC.options.attackKey.isPressed(), "L");
        drawKeyButton(context, startX + BUTTON_SIZE + GAP, startY + (BUTTON_SIZE + GAP) * 2, ClientRefs.MC.options.useKey.isPressed(), "R");
        
        // CPS
        if (showCps.get()) {
            int cpsY = startY + (BUTTON_SIZE + GAP) * 2 + BUTTON_SIZE + 2;
            String cpsText = "CPS: " + (ClientRefs.MC.options.attackKey.isPressed() ? "~" + demoCps.get().intValue() : "0");
            context.drawTextWithShadow(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                cpsText,
                startX,
                cpsY,
                0xFFAAAAAA
            );
        }
    }
    
    private void drawKeyButton(DrawContext context, int x, int y, boolean pressed, String label) {
        int bgColor = pressed ? 0xFF4CAF50 : 0xFF606060;
        int borderColor = pressed ? 0xFF388E3C : 0xFF404040;
        
        // Кнопка фон
        context.fill(x, y, x + BUTTON_SIZE, y + BUTTON_SIZE, bgColor);
        
        // Граница кнопки
        context.drawHorizontalLine(x, x + BUTTON_SIZE, y, borderColor);
        context.drawHorizontalLine(x, x + BUTTON_SIZE, y + BUTTON_SIZE, borderColor);
        context.drawVerticalLine(x, y, y + BUTTON_SIZE, borderColor);
        context.drawVerticalLine(x + BUTTON_SIZE, y, y + BUTTON_SIZE, borderColor);
        
        // Текст кнопки
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            label,
            x + (BUTTON_SIZE - 8) / 2,
            y + (BUTTON_SIZE - 8) / 2,
            0xFFFFFFFF
        );
    }
}
