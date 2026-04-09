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
    private static final int BUTTON_WIDTH = 276;
    private static final int BUTTON_HEIGHT = 34;
    private static final int BUTTON_GAP = 12;
    private static final int STAR_COUNT = 90;
    private static final int COMET_COUNT = 6;

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
        int y = height / 2 - 84;

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

        renderSpaceBackground(context, mouseX, mouseY);
        renderHeader(context);

        for (int i = 0; i < buttons.size(); i++) {
            MenuButton button = buttons.get(i);
            float appear = clamp((fade * 1.35f) - i * 0.08f, 0f, 1f);
            button.render(context, textRenderer, mouseX, mouseY, appear);
        }

        int footerColor = ColorHelper.Argb.getArgb((int) (190 * fade), 190, 210, 245);
        context.drawCenteredTextWithShadow(textRenderer, "Fabric 1.21.1  •  Java 21  •  NovaClient QoL", width / 2, height - 20, footerColor);
    }

    private void renderSpaceBackground(DrawContext context, int mouseX, int mouseY) {
        float nx = (mouseX - width / 2f) / width;
        float ny = (mouseY - height / 2f) / height;

        context.fillGradient(0, 0, width, height, 0xFF050914, 0xFF111D36);
        context.fillGradient(0, height / 2, width, height, 0x10000000, 0x66020410);

        for (int i = 0; i < STAR_COUNT; i++) {
            int sx = hash(i * 71 + 13, width);
            int sy = hash(i * 97 + 19, height);
            int size = 1 + ((i * 23) % 2);
            int driftX = (int) (nx * (1 + i % 3) * 5f);
            int driftY = (int) (ny * (1 + i % 2) * 5f);
            int twinkle = 140 + (int) ((Math.sin((System.currentTimeMillis() * 0.004) + i) + 1) * 55);
            int starColor = ColorHelper.Argb.getArgb(Math.min(255, twinkle), 220, 235, 255);
            context.fill(sx + driftX, sy + driftY, sx + driftX + size, sy + driftY + size, starColor);
        }

        for (int i = 0; i < COMET_COUNT; i++) {
            drawComet(context, i, nx, ny);
        }

        context.fillGradient(0, height - 68, width, height, 0x00000000, 0x90010616);
    }

    private void drawComet(DrawContext context, int idx, float nx, float ny) {
        double time = System.currentTimeMillis() / 1000.0;
        double speed = 0.11 + idx * 0.015;
        double offset = (time * speed + idx * 0.22) % 1.0;

        int startX = (int) ((1.2 - offset) * width) - (idx * 38);
        int startY = (int) ((0.18 + offset * 0.55) * height) + idx * 12;

        startX += (int) (nx * 25);
        startY += (int) (ny * 25);

        int tail = 70 + idx * 10;
        for (int t = 0; t < tail; t += 3) {
            int alpha = Math.max(0, 175 - (t * 2));
            int x = startX + t;
            int y = startY - t / 2;
            context.fill(x, y, x + 3, y + 2, ColorHelper.Argb.getArgb(alpha, 120, 200, 255));
        }
        context.fill(startX - 2, startY - 1, startX + 3, startY + 3, 0xE0E8F6FF);
    }

    private int hash(int seed, int bound) {
        int x = seed;
        x ^= (x << 13);
        x ^= (x >>> 17);
        x ^= (x << 5);
        return Math.floorMod(x, Math.max(1, bound));
    }

    private void renderHeader(DrawContext context) {
        int alpha = (int) (255 * fade);
        float pulse = (float) Math.sin((System.currentTimeMillis() * 0.004)) * 0.05f + 1f;

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(width / 2f, 74, 0);
        matrices.scale(pulse, pulse, 1f);
        context.drawCenteredTextWithShadow(textRenderer, "NOVA CLIENT", 0, 0, ColorHelper.Argb.getArgb(alpha, 236, 244, 255));
        matrices.pop();

        context.drawCenteredTextWithShadow(textRenderer, "Космический интерфейс • легитный gameplay", width / 2, 92,
                ColorHelper.Argb.getArgb((int) (220 * fade), 170, 205, 255));
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

            int base = hovered ? ColorHelper.Argb.getArgb((int) (225 * appear), 28, 44, 78)
                    : ColorHelper.Argb.getArgb((int) (175 * appear), 18, 30, 56);
            int borderTop = hovered ? ColorHelper.Argb.getArgb((int) (255 * appear), 127, 200, 255)
                    : ColorHelper.Argb.getArgb((int) (185 * appear), 87, 127, 205);
            int borderBottom = hovered ? ColorHelper.Argb.getArgb((int) (175 * appear), 98, 172, 255)
                    : ColorHelper.Argb.getArgb((int) (130 * appear), 60, 95, 155);

            context.fill(x, drawY, x + width, drawY + height, base);
            context.fill(x, drawY, x + width, drawY + 2, borderTop);
            context.fill(x, drawY + height - 2, x + width, drawY + height, borderBottom);

            int titleColor = hovered
                    ? ColorHelper.Argb.getArgb((int) (255 * appear), 243, 250, 255)
                    : ColorHelper.Argb.getArgb((int) (240 * appear), 222, 236, 255);
            int subColor = ColorHelper.Argb.getArgb((int) (225 * appear), 153, 182, 224);

            int titleY = drawY + 8;
            int subY = drawY + 20;
            context.drawCenteredTextWithShadow(textRenderer, title, x + width / 2, titleY, titleColor);
            context.drawCenteredTextWithShadow(textRenderer, subtitle, x + width / 2, subY, subColor);
        }
    }
}
