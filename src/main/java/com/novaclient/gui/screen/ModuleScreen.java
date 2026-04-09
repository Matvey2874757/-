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
    private static final int TAB_HEIGHT = 24;
    private static final int TAB_GAP = 8;

    private final Screen parent;
    private final Map<Category, List<Module>> grouped = new EnumMap<>(Category.class);
    private final List<TabBox> tabs = new ArrayList<>();

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

        tabs.clear();
        int tabWidth = Math.max(94, (width - 40 - TAB_GAP * (Category.values().length - 1)) / Category.values().length);
        int x = 20;
        for (Category category : Category.values()) {
            tabs.add(new TabBox(category, x, 44, tabWidth, TAB_HEIGHT));
            x += tabWidth + TAB_GAP;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Вкладки: открой нужную категорию и кликай по модулю для ON/OFF", width / 2, 27, 0xB9D3EEFF);

        renderTabs(context, mouseX, mouseY);
        renderActiveModules(context, mouseX, mouseY);
        drawBackButton(context, mouseX, mouseY);
    }

    private void renderBackground(DrawContext context, int mouseX, int mouseY) {
        context.fillGradient(0, 0, width, height, 0xF00B1222, 0xF01C2941);

        int nx = (int) ((mouseX - width / 2f) * 0.03f);
        int ny = (int) ((mouseY - height / 2f) * 0.03f);

        context.fill(34 + nx, 30 + ny, width / 2 + 100 + nx, 120 + ny, 0x253E79C8);
        context.fill(width / 2 - 70 - nx, height - 150 - ny, width - 30 - nx, height - 40 - ny, 0x2042C18B);
    }

    private void renderTabs(DrawContext context, int mouseX, int mouseY) {
        for (TabBox tab : tabs) {
            boolean hovered = tab.contains(mouseX, mouseY);
            boolean active = tab.category == activeCategory;

            int bg = active ? 0xE0324A73 : hovered ? 0xC0293C5C : 0x90212D45;
            int line = active ? 0xFF8BD1FF : 0xFF5E89C4;
            context.fill(tab.x, tab.y, tab.x + tab.width, tab.y + tab.height, bg);
            context.fill(tab.x, tab.y, tab.x + tab.width, tab.y + 2, line);

            int textColor = active ? 0xFFFFFFFF : hovered ? 0xFFF0F7FF : 0xFFD6E3FF;
            context.drawCenteredTextWithShadow(textRenderer, localizeCategory(tab.category), tab.x + tab.width / 2, tab.y + 8, textColor);
        }
    }

    private void renderActiveModules(DrawContext context, int mouseX, int mouseY) {
        List<Module> modules = grouped.getOrDefault(activeCategory, List.of());
        int panelX = 20;
        int panelY = 78;
        int panelW = width - 40;
        int panelH = height - 116;

        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xA2162238);
        context.fill(panelX, panelY, panelX + panelW, panelY + 2, 0xFF63B3FF);

        int columns = panelW > 780 ? 3 : 2;
        int gap = 10;
        int cardW = (panelW - 20 - gap * (columns - 1)) / columns;

        int idx = 0;
        for (Module module : modules) {
            int col = idx % columns;
            int row = idx / columns;
            int x = panelX + 10 + col * (cardW + gap);
            int y = panelY + 10 + row * 36;
            int h = 30;

            if (y + h > panelY + panelH - 8) {
                break;
            }

            boolean hovered = mouseX >= x && mouseX <= x + cardW && mouseY >= y && mouseY <= y + h;
            boolean enabled = module.isEnabled();

            int bg = enabled ? 0xB8255641 : 0x90314256;
            if (hovered) {
                bg = enabled ? 0xD0316D54 : 0xB14B5D7D;
            }

            context.fill(x, y, x + cardW, y + h, bg);
            context.fill(x, y, x + 3, y + h, enabled ? 0xFF79F0AE : 0xFFFF95A4);

            context.drawTextWithShadow(textRenderer, module.getName(), x + 8, y + 6, enabled ? 0xFFE8FFEF : 0xFFE9EEFF);
            String status = enabled ? "ON" : "OFF";
            context.drawTextWithShadow(textRenderer, status, x + cardW - 8 - textRenderer.getWidth(status), y + 6, enabled ? 0xFF8EFFB7 : 0xFFFFA0AF);

            String desc = module.getDescription();
            if (desc.length() > 38) {
                desc = desc.substring(0, 35) + "...";
            }
            context.drawText(textRenderer, desc, x + 8, y + 18, 0xB7CCE7FF, false);

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

        for (TabBox tab : tabs) {
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
        int panelX = 20;
        int panelY = 78;
        int panelW = width - 40;
        int panelH = height - 116;
        int columns = panelW > 780 ? 3 : 2;
        int gap = 10;
        int cardW = (panelW - 20 - gap * (columns - 1)) / columns;

        int idx = 0;
        for (Module module : modules) {
            int col = idx % columns;
            int row = idx / columns;
            int x = panelX + 10 + col * (cardW + gap);
            int y = panelY + 10 + row * 36;
            int h = 30;
            if (y + h > panelY + panelH - 8) {
                break;
            }

            if (mouseX >= x && mouseX <= x + cardW && mouseY >= y && mouseY <= y + h) {
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
