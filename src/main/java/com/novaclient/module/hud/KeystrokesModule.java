package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayDeque;
import java.util.Deque;

public final class KeystrokesModule extends HudTextModule {
    private final Deque<Long> clicks = new ArrayDeque<>();
    private boolean wasAttackPressed;

    public KeystrokesModule() {
        super("Keystrokes", "Показывает WASD, мышь и CPS", 8, 8);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.options == null || ClientRefs.MC.textRenderer == null) {
            return;
        }

        boolean attackPressed = ClientRefs.MC.options.attackKey.isPressed();
        if (attackPressed && !wasAttackPressed) {
            clicks.addLast(System.currentTimeMillis());
        }
        wasAttackPressed = attackPressed;
        trimClicks();

        int panelX = x;
        int panelY = y;
        int key = 20;
        int gap = 3;

        drawKey(context, panelX + key + gap, panelY, key, key, "W", ClientRefs.MC.options.forwardKey.isPressed());
        drawKey(context, panelX, panelY + key + gap, key, key, "A", ClientRefs.MC.options.leftKey.isPressed());
        drawKey(context, panelX + key + gap, panelY + key + gap, key, key, "S", ClientRefs.MC.options.backKey.isPressed());
        drawKey(context, panelX + (key + gap) * 2, panelY + key + gap, key, key, "D", ClientRefs.MC.options.rightKey.isPressed());

        int mouseY = panelY + (key + gap) * 2 + 2;
        drawKey(context, panelX, mouseY, key * 2 + gap, key, "LMB", ClientRefs.MC.options.attackKey.isPressed());
        drawKey(context, panelX + (key * 2 + gap) + gap, mouseY, key * 2 + gap, key, "RMB", ClientRefs.MC.options.useKey.isPressed());

        int panelWidth = key * 4 + gap * 3;
        int cpsY = mouseY + key + gap;
        drawKey(context, panelX, cpsY, panelWidth / 2 - 2, 16, "CPS", false);
        drawKey(context, panelX + panelWidth / 2 + 2, cpsY, panelWidth / 2 - 2, 16, String.valueOf(clicks.size()), true);
    }

    private void drawKey(DrawContext context, int x, int y, int w, int h, String label, boolean pressed) {
        TextRenderer tr = ClientRefs.MC.textRenderer;
        int bg = pressed ? 0xCC2D7058 : 0xAA23324D;
        int line = pressed ? 0xFF81F7B4 : 0xFF84B5F6;
        int text = pressed ? 0xFFEDFFF4 : 0xFFE6EEFF;

        context.fill(x, y, x + w, y + h, bg);
        context.fill(x, y, x + w, y + 1, line);
        context.drawCenteredTextWithShadow(tr, label, x + w / 2, y + (h - 8) / 2, text);
    }

    private void trimClicks() {
        long cutoff = System.currentTimeMillis() - 1000L;
        while (!clicks.isEmpty() && clicks.peekFirst() < cutoff) {
            clicks.removeFirst();
        }
    }
}
