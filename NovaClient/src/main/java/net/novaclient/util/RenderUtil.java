package net.novaclient.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class RenderUtil {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int color, int radius) {
        // Simple rounded rectangle implementation
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + width, y + height - radius, color);
    }

    public static void drawStringWithShadow(DrawContext context, String text, int x, int y, int color) {
        if (mc.textRenderer != null) {
            context.drawTextWithShadow(mc.textRenderer, text, x, y, color);
        }
    }

    public static void drawCenteredStringWithShadow(DrawContext context, String text, int x, int y, int color) {
        if (mc.textRenderer != null) {
            context.drawCenteredTextWithShadow(mc.textRenderer, text, x, y, color);
        }
    }

    public static int getStringWidth(String text) {
        return mc.textRenderer != null ? mc.textRenderer.getWidth(text) : 0;
    }

    public static int getStringHeight() {
        return mc.textRenderer != null ? mc.textRenderer.fontHeight : 9;
    }
}
