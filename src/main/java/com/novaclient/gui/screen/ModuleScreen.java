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
    private static final int SIDEBAR_WIDTH = 156;
    private static final int CATEGORY_GAP = 8;

    private final Screen parent;
    private final Map<Category, List<Module>> grouped = new EnumMap<>(Category.class);
    private final List<TabBox> categoryTabs = new ArrayList<>();

    private Category activeCategory = Category.RENDER;

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

        categoryTabs.clear();
        int x = 20;
        int y = 52;
        for (Category category : Category.values()) {
            categoryTabs.add(new TabBox(category, x, y, SIDEBAR_WIDTH - 16, 26));
            y += 26 + CATEGORY_GAP;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Категории слева • модули справа", width / 2, 27, 0xB9D3EEFF);

        renderSidebar(context, mouseX, mouseY);
        renderActiveModules(context, mouseX, mouseY);
        drawBackButton(context, mouseX, mouseY);
    }

    private void renderBackground(DrawContext context, int mouseX, int mouseY) {
        context.fillGradient(0, 0, width, height, 0xF00B1222, 0xF01C2941);

        int nx = (int) ((mouseX - width / 2f) * 0.03f);
        int ny = (int) ((mouseY - height / 2f) * 0.03f);

        context.fill(24 + nx, 22 + ny, width / 2 + 60 + nx, 110 + ny, 0x1F4A83CC);
        context.fill(width / 2 - 40 - nx, height - 160 - ny, width - 26 - nx, height - 36 - ny, 0x1F54CA92);
    }

    private void renderSidebar(DrawContext context, int mouseX, int mouseY) {
        int x = 20;
        int y = 44;
        int h = height - 82;
        context.fill(x, y, x + SIDEBAR_WIDTH, y + h, 0xA1162238);
        context.fill(x, y, x + SIDEBAR_WIDTH, y + 2, 0xFF63B3FF);

        context.drawTextWithShadow(textRenderer, "Категории", x + 10, y + 10, 0xFFEAF4FF);

        for (TabBox tab : categoryTabs) {
            boolean hovered = tab.contains(mouseX, mouseY);
            boolean active = tab.category == activeCategory;

            int bg = active ? 0xE1344E79 : hovered ? 0xC02A3D5E : 0x90222E47;
            int line = active ? 0xFF8BD1FF : 0xFF5D8AC5;
            context.fill(tab.x, tab.y, tab.x + tab.width, tab.y + tab.height, bg);
            context.fill(tab.x, tab.y, tab.x + tab.width, tab.y + 2, line);

            int textColor = active ? 0xFFFFFFFF : hovered ? 0xFFF2F8FF : 0xFFD7E5FF;
            context.drawCenteredTextWithShadow(textRenderer, localizeCategory(tab.category), tab.x + tab.width / 2, tab.y + 9, textColor);
        }
    }

    private void renderActiveModules(DrawContext context, int mouseX, int mouseY) {
        List<Module> modules = grouped.getOrDefault(activeCategory, List.of());

        int panelX = 20 + SIDEBAR_WIDTH + 12;
        int panelY = 44;
        int panelW = width - panelX - 20;
        int panelH = height - 82;

        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xA2162238);
        context.fill(panelX, panelY, panelX + panelW, panelY + 2, 0xFF63B3FF);
        context.drawTextWithShadow(textRenderer, "Функции: " + localizeCategory(activeCategory), panelX + 10, panelY + 10, 0xFFEAF5FF);

        int columns = panelW > 660 ? 2 : 1;
        int gap = 10;
        int cardW = (panelW - 20 - gap * (columns - 1)) / columns;
        int cardH = 44;

        int idx = 0;
        for (Module module : modules) {
            int col = idx % columns;
            int row = idx / columns;

            int x = panelX + 10 + col * (cardW + gap);
            int y = panelY + 30 + row * (cardH + 8);
            if (y + cardH > panelY + panelH - 10) {
                break;
            }

            boolean hovered = mouseX >= x && mouseX <= x + cardW && mouseY >= y && mouseY <= y + cardH;
            boolean enabled = module.isEnabled();

            int bg = enabled ? 0xBF245540 : 0x9A2E415F;
            if (hovered) {
                bg = enabled ? 0xDB30725A : 0xB54A5E7D;
            }

            context.fill(x, y, x + cardW, y + cardH, bg);
            context.fill(x, y, x + 3, y + cardH, enabled ? 0xFF79F0AE : 0xFFFF96A4);

            context.drawTextWithShadow(textRenderer, module.getName(), x + 10, y + 9, enabled ? 0xFFE9FFF0 : 0xFFE9EEFF);
            String desc = module.getDescription();
            if (desc.length() > 42) {
                desc = desc.substring(0, 39) + "...";
            }
            context.drawText(textRenderer, desc, x + 10, y + 22, 0xB7CCE7FF, false);

            int pillW = 40;
            int pillH = 16;
            int pillX = x + cardW - pillW - 10;
            int pillY = y + 14;
            context.fill(pillX, pillY, pillX + pillW, pillY + pillH, enabled ? 0xFF2B7E5A : 0xFF784554);
            context.drawCenteredTextWithShadow(textRenderer, enabled ? "ON" : "OFF", pillX + pillW / 2, pillY + 4,
                    enabled ? 0xFFE9FFF0 : 0xFFFFE2E8);

            idx++;
        }
    }

    private void drawBackButton(DrawContext context, int mouseX, int mouseY) {
        int bw = 124;
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

        for (TabBox tab : categoryTabs) {
            if (tab.contains(mouseX, mouseY)) {
                activeCategory = tab.category;
                return true;
            }
        }

        int bw = 124;
        int bh = 24;
        int bx = width / 2 - bw / 2;
        int by = height - 30;
        if (mouseX >= bx && mouseX <= bx + bw && mouseY >= by && mouseY <= by + bh) {
            client.setScreen(parent);
            return true;
        }

        List<Module> modules = grouped.getOrDefault(activeCategory, List.of());
        int panelX = 20 + SIDEBAR_WIDTH + 12;
        int panelY = 44;
        int panelW = width - panelX - 20;
        int panelH = height - 82;

        int columns = panelW > 660 ? 2 : 1;
        int gap = 10;
        int cardW = (panelW - 20 - gap * (columns - 1)) / columns;
        int cardH = 44;

        int idx = 0;
        for (Module module : modules) {
            int col = idx % columns;
            int row = idx / columns;

            int x = panelX + 10 + col * (cardW + gap);
            int y = panelY + 30 + row * (cardH + 8);
            if (y + cardH > panelY + panelH - 10) {
                break;
            }

            if (mouseX >= x && mouseX <= x + cardW && mouseY >= y && mouseY <= y + cardH) {
                module.toggle();
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                return true;
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

    private static final class TabBox {
        private final Category category;
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private TabBox(Category category, int x, int y, int width, int height) {
            this.category = category;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        private boolean contains(double mx, double my) {
            return mx >= x && mx <= x + width && my >= y && my <= y + height;
        }
    }
}
