package com.novaclient.gui.screen;

import com.novaclient.NovaClient;
import com.novaclient.module.Category;
import com.novaclient.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ModuleScreen extends Screen {
    private final Screen parent;
    private final Map<Category, List<Module>> grouped = new EnumMap<>(Category.class);
    private float fade;

    public ModuleScreen(Screen parent) {
        super(Text.literal("Модули NovaClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        grouped.clear();
        for (Category category : Category.values()) {
            grouped.put(category, new ArrayList<>());
        }
        for (Module module : NovaClient.MODULE_MANAGER.getModules()) {
            grouped.get(module.getCategory()).add(module);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        fade = Math.min(1f, fade + delta * 0.06f);

        renderBackground(context, mouseX, mouseY);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Клик по карточке включает или выключает модуль", width / 2, 28, 0xBBD3EEFF);

        int columns = Math.max(1, Math.min(3, width / 330));
        int gap = 12;
        int panelWidth = (width - 40 - gap * (columns - 1)) / columns;
        int startX = 20;
        int startY = 48;

        int idx = 0;
        for (Category category : Category.values()) {
            int col = idx % columns;
            int row = idx / columns;
            int x = startX + col * (panelWidth + gap);
            int y = startY + row * 168;
            renderCategoryPanel(context, category, x, y, panelWidth, 156, mouseX, mouseY);
            idx++;
        }

        drawBackButton(context, mouseX, mouseY);
    }

    private void renderBackground(DrawContext context, int mouseX, int mouseY) {
        context.fillGradient(0, 0, width, height, 0xF00B1222, 0xF01C2941);

        int nx = (int) ((mouseX - width / 2f) * 0.03f);
        int ny = (int) ((mouseY - height / 2f) * 0.03f);

        context.fill(34 + nx, 30 + ny, width / 2 + 100 + nx, 120 + ny, 0x253E79C8);
        context.fill(width / 2 - 70 - nx, height - 150 - ny, width - 30 - nx, height - 40 - ny, 0x2042C18B);
    }

    private void renderCategoryPanel(DrawContext context, Category category, int x, int y, int width, int height, int mouseX, int mouseY) {
        context.fill(x, y, x + width, y + height, 0xB2172238);
        context.fill(x, y, x + width, y + 2, 0xFF63B3FF);

        Text catName = Text.literal(localizeCategory(category));
        context.drawTextWithShadow(textRenderer, catName, x + 8, y + 8, 0xFFF0F7FF);

        List<Module> modules = grouped.get(category);
        int rowY = y + 26;

        for (Module module : modules) {
            int rowHeight = 18;
            boolean hovered = mouseX >= x + 6 && mouseX <= x + width - 6 && mouseY >= rowY && mouseY <= rowY + rowHeight;
            boolean enabled = module.isEnabled();

            int bg = enabled ? 0xAA214D3C : 0x88314157;
            if (hovered) {
                bg = enabled ? 0xCC2A654E : 0xB04D5E7E;
            }

            context.fill(x + 6, rowY, x + width - 6, rowY + rowHeight, bg);

            int textColor = enabled ? 0xFFDAFFE7 : 0xFFE6EDFF;
            context.drawTextWithShadow(textRenderer, module.getName(), x + 12, rowY + 5, textColor);

            Text state = Text.literal(enabled ? "ON" : "OFF");
            int sw = textRenderer.getWidth(state);
            int sx = x + width - 12 - sw;
            int stateColor = enabled ? 0xFF80F4B0 : 0xFFFF9AA8;
            context.drawTextWithShadow(textRenderer, state, sx, rowY + 5, stateColor);

            rowY += 20;
            if (rowY > y + height - 20) {
                break;
            }
        }
    }

    private void drawBackButton(DrawContext context, int mouseX, int mouseY) {
        int bw = 120;
        int bh = 24;
        int bx = width / 2 - bw / 2;
        int by = height - 30;
        boolean hovered = mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh;

        context.fill(bx, by, bx + bw, by + bh, hovered ? 0xE03A527A : 0xC02A3D5E);
        context.fill(bx, by, bx + bw, by + 1, 0xFF87C7FF);
        context.drawCenteredTextWithShadow(textRenderer, "Назад", width / 2, by + 8, hovered ? 0xFFFFFFFF : 0xFFE2EDFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        int bw = 120;
        int bh = 24;
        int bx = width / 2 - bw / 2;
        int by = height - 30;
        if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh) {
            client.setScreen(parent);
            return true;
        }

        int columns = Math.max(1, Math.min(3, width / 330));
        int gap = 12;
        int panelWidth = (width - 40 - gap * (columns - 1)) / columns;
        int startX = 20;
        int startY = 48;

        int idx = 0;
        for (Category category : Category.values()) {
            int col = idx % columns;
            int row = idx / columns;
            int x = startX + col * (panelWidth + gap);
            int y = startY + row * 168;

            int rowY = y + 26;
            for (Module module : grouped.get(category)) {
                int rowHeight = 18;
                if (mouseX >= x + 6 && mouseX <= x + panelWidth - 6 && mouseY >= rowY && mouseY <= rowY + rowHeight) {
                    module.toggle();
                    NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                    return true;
                }
                rowY += 20;
                if (rowY > y + 156 - 20) {
                    break;
                }
            }
            idx++;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            client.setScreen(parent);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private static String localizeCategory(Category category) {
        return switch (category) {
            case RENDER -> "Render";
            case HUD -> "HUD / Utility";
            case MOVEMENT -> "Movement";
            case COMBAT -> "Combat Visual";
            case PERFORMANCE -> "Performance";
        };
    }
}
