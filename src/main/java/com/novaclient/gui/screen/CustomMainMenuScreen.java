package com.novaclient.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class CustomMainMenuScreen extends Screen {
    private static final int BUTTON_WIDTH = 220;
    private static final int BUTTON_HEIGHT = 26;
    private static final int BUTTON_GAP = 10;

    private float fade;
    private final Screen parent;
    private final List<MenuButton> buttons = new ArrayList<>();

    public CustomMainMenuScreen(Screen parent) {
        super(Text.literal("NovaClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        buttons.clear();
        int x = width / 2 - BUTTON_WIDTH / 2;
        int y = height / 2 - 64;

        addButton("Одиночная игра", "Миры и сохранения", x, y, () -> client.setScreen(new SelectWorldScreen(this)));
        addButton("Сетевая игра", "Сервера и Realms", x, y + (BUTTON_HEIGHT + BUTTON_GAP), () -> client.setScreen(new MultiplayerScreen(this)));
        addButton("Модули", "Настройка функций клиента", x, y + (BUTTON_HEIGHT + BUTTON_GAP) * 2, () -> client.setScreen(new ModuleScreen(this)));
        addButton("Настройки", "Опции Minecraft", x, y + (BUTTON_HEIGHT + BUTTON_GAP) * 3, () -> client.setScreen(new OptionsScreen(this, client.options)));
        addButton("Выход", "Закрыть игру", x, y + (BUTTON_HEIGHT + BUTTON_GAP) * 4, () -> MinecraftClient.getInstance().scheduleStop());
    }

    private void addButton(String title, String subtitle, int x, int y, Runnable action) {
        buttons.add(new MenuButton(Text.literal(title), Text.literal(subtitle), x, y, BUTTON_WIDTH, BUTTON_HEIGHT, action));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        fade = Math.min(1f, fade + delta * 0.04f);

        renderBackgroundLayers(context, mouseX, mouseY);
        renderHeader(context, delta);

        for (int i = 0; i < buttons.size(); i++) {
            MenuButton button = buttons.get(i);
            float appear = clamp((fade * 1.35f) - i * 0.08f, 0f, 1f);
            button.render(context, textRenderer, mouseX, mouseY, appear);
        }

        int footerColor = ColorHelper.Argb.getArgb((int) (180 * fade), 190, 210, 245);
        context.drawCenteredTextWithShadow(textRenderer, "Fabric 1.21.1  •  Java 21  •  NovaClient QoL", width / 2, height - 20, footerColor);
    }

    private void renderBackgroundLayers(DrawContext context, int mouseX, int mouseY) {
        float nx = (mouseX - width / 2f) / width;
        float ny = (mouseY - height / 2f) / height;

        int top = ColorHelper.Argb.getArgb(255, 10, 14, 26);
        int bottom = ColorHelper.Argb.getArgb(255, 26, 36, 60);
        context.fillGradient(0, 0, width, height, top, bottom);

        int blur1 = ColorHelper.Argb.getArgb(95, 72, 170, 255);
        int blur2 = ColorHelper.Argb.getArgb(80, 140, 82, 255);
        int blur3 = ColorHelper.Argb.getArgb(65, 50, 220, 180);

        int p1x = (int) (width * 0.2f + nx * 45f);
        int p1y = (int) (height * 0.2f + ny * 45f);
        int p2x = (int) (width * 0.8f + nx * 35f);
        int p2y = (int) (height * 0.3f + ny * 35f);
        int p3x = (int) (width * 0.55f - nx * 38f);
        int p3y = (int) (height * 0.78f - ny * 38f);

        drawSoftRect(context, p1x - 220, p1y - 120, p1x + 220, p1y + 120, blur1);
        drawSoftRect(context, p2x - 200, p2y - 110, p2x + 200, p2y + 110, blur2);
        drawSoftRect(context, p3x - 240, p3y - 130, p3x + 240, p3y + 130, blur3);

        context.fillGradient(0, height - 56, width, height,
                ColorHelper.Argb.getArgb(0, 0, 0, 0), ColorHelper.Argb.getArgb(130, 4, 8, 20));
    }

    private void renderHeader(DrawContext context, float delta) {
        int alpha = (int) (255 * fade);
        float pulse = (float) Math.sin((client == null ? 0L : client.world != null ? client.world.getTime() : System.currentTimeMillis() / 50L) * 0.04f) * 0.08f + 1f;

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(width / 2f, 78, 0);
        matrices.scale(pulse, pulse, 1f);
        context.drawCenteredTextWithShadow(textRenderer, "NOVA CLIENT", 0, 0, ColorHelper.Argb.getArgb(alpha, 236, 244, 255));
        matrices.pop();

        context.drawCenteredTextWithShadow(textRenderer, "Современный легитный интерфейс", width / 2, 95,
                ColorHelper.Argb.getArgb((int) (210 * fade), 170, 205, 255));
    }

    private void drawSoftRect(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2, y2, color);
        context.fill(x1 - 8, y1 - 8, x2 + 8, y1, ColorHelper.Argb.withAlpha(28, color));
        context.fill(x1 - 8, y2, x2 + 8, y2 + 8, ColorHelper.Argb.withAlpha(28, color));
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (MenuButton menuButton : buttons) {
                if (menuButton.isInside(mouseX, mouseY)) {
                    menuButton.action.run();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && parent != null) {
            client.setScreen(parent);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static final class MenuButton {
        private final Text title;
        private final Text subtitle;
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final Runnable action;

        private MenuButton(Text title, Text subtitle, int x, int y, int width, int height, Runnable action) {
            this.title = title;
            this.subtitle = subtitle;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.action = action;
        }

        private boolean isInside(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }

        private void render(DrawContext context, net.minecraft.client.font.TextRenderer textRenderer, int mouseX, int mouseY, float appear) {
            if (appear <= 0.01f) {
                return;
            }

            int offsetY = (int) ((1f - appear) * 18f);
            int drawY = y + offsetY;
            boolean hovered = isInside(mouseX, mouseY);

            int base = hovered ? ColorHelper.Argb.getArgb((int) (220 * appear), 34, 46, 76)
                    : ColorHelper.Argb.getArgb((int) (168 * appear), 20, 30, 50);
            int border = hovered ? ColorHelper.Argb.getArgb((int) (255 * appear), 115, 196, 255)
                    : ColorHelper.Argb.getArgb((int) (180 * appear), 70, 108, 165);

            context.fill(x, drawY, x + width, drawY + height, base);
            context.fill(x, drawY, x + width, drawY + 1, border);
            context.fill(x, drawY + height - 1, x + width, drawY + height, ColorHelper.Argb.withAlpha((int) (140 * appear), border));

            int titleColor = hovered
                    ? ColorHelper.Argb.getArgb((int) (255 * appear), 243, 250, 255)
                    : ColorHelper.Argb.getArgb((int) (235 * appear), 222, 236, 255);
            int subColor = ColorHelper.Argb.getArgb((int) (220 * appear), 153, 182, 224);

            context.drawTextWithShadow(textRenderer, title, x + 10, drawY + 7, titleColor);
            context.drawText(textRenderer, subtitle, x + width - 9 - textRenderer.getWidth(subtitle), drawY + 8, subColor, false);
        }
    }
}
