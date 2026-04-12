package com.novaclient.gui.screen;

import com.novaclient.NovaClient;
import com.novaclient.module.BooleanSetting;
import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import com.novaclient.module.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.util.ArrayList;
import java.util.List;

public final class ModuleScreen extends Screen {
    private final Screen parent;
    private Category selectedCategory;
    private int scrollOffset = 0;
    private final int sidebarWidth = 140;
    private boolean showEnabledOnly = false;
    private final List<ModuleCard> moduleCards = new ArrayList<>();
    private int maxContentHeight = 0;

    private static class ModuleCard {
        Module module;
        int x, y, width, height;
        boolean expanded = false;
        float animAlpha = 0f;
        
        ModuleCard(Module module, int x, int y, int width, int height) {
            this.module = module;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        boolean isHovered(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
        
        int getTotalHeight() {
            if (!expanded || module.getSettings().isEmpty()) {
                return height;
            }
            int settingsHeight = module.getSettings().size() * 24;
            return height + settingsHeight + 8;
        }
    }

    public ModuleScreen(Screen parent) {
        super(Text.literal("Модули NovaClient"));
        this.parent = parent;
        this.selectedCategory = null; // null means all categories
    }

    @Override
    protected void init() {
        rebuild();
    }

    private void rebuild() {
        clearChildren();
        moduleCards.clear();

        // Sidebar category buttons
        int categoryY = 10;
        
        // All categories button
        addDrawableChild(createCategoryButton(
            Text.literal(selectedCategory == null ? "§a> Все <" : "   Все"),
            () -> {
                selectedCategory = null;
                scrollOffset = 0;
                rebuild();
            },
            0, categoryY
        ));
        categoryY += 28;

        // Individual category buttons
        for (Category cat : Category.values()) {
            long count = NovaClient.MODULE_MANAGER.getModules().stream()
                    .filter(m -> m.getCategory() == cat)
                    .count();
            boolean isSelected = selectedCategory == cat;
            String prefix = isSelected ? "§a> " : "   ";
            String suffix = " §7(" + count + ")";
            
            addDrawableChild(createCategoryButton(
                Text.literal(prefix + cat.displayName() + suffix),
                () -> {
                    selectedCategory = cat;
                    scrollOffset = 0;
                    rebuild();
                },
                0, categoryY
            ));
            categoryY += 28;
        }

        // Filter toggle button at bottom of sidebar
        addDrawableChild(ButtonWidget.builder(
            Text.literal(showEnabledOnly ? "§a✓ Только ON" : "  Все модули"),
            b -> {
                showEnabledOnly = !showEnabledOnly;
                scrollOffset = 0;
                rebuild();
            }
        ).dimensions(5, height - 65, sidebarWidth - 10, 22).build());

        // Back button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§c← Назад"),
            b -> client.setScreen(parent)
        ).dimensions(5, height - 38, sidebarWidth - 10, 22).build());

        // Main content area - module list
        List<Module> modules = getFilteredModules();
        int contentX = sidebarWidth + 15;
        int contentStartY = 45;
        maxContentHeight = height - 55;
        
        // Title
        String titleText = "§fМодули";
        if (selectedCategory != null) {
            titleText += " §7> §f" + selectedCategory.displayName();
        }
        if (showEnabledOnly) {
            titleText += " §7[Только активные]";
        }
        
        // Build module cards
        int currentY = contentStartY - scrollOffset;
        for (Module module : modules) {
            if (currentY > maxContentHeight) {
                break;
            }
            
            int cardHeight = 42;
            ModuleCard card = new ModuleCard(module, contentX, currentY, 320, cardHeight);
            moduleCards.add(card);
            
            currentY += card.getTotalHeight() + 6;
        }

        // Quick actions at top right
        int actionX = sidebarWidth + 20;
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§aВкл. все"),
            b -> {
                NovaClient.MODULE_MANAGER.enableAll();
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                rebuild();
            }
        ).dimensions(actionX, 8, 75, 18).build());
        
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§cВыкл. все"),
            b -> {
                NovaClient.MODULE_MANAGER.disableAll();
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                rebuild();
            }
        ).dimensions(actionX + 80, 8, 80, 18).build());
        
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§eСброс"),
            b -> {
                NovaClient.MODULE_MANAGER.resetAllSettings();
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                rebuild();
            }
        ).dimensions(actionX + 165, 8, 55, 18).build());
    }

    private ButtonWidget createCategoryButton(Text text, Runnable action, int x, int y) {
        return ButtonWidget.builder(text, b -> action.run())
            .dimensions(x + 5, y, sidebarWidth - 10, 24)
            .build();
    }

    private List<Module> getFilteredModules() {
        return NovaClient.MODULE_MANAGER.getModules().stream()
                .filter(m -> selectedCategory == null || m.getCategory() == selectedCategory)
                .filter(m -> !showEnabledOnly || m.isEnabled())
                .toList();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Background gradient - fully opaque (alpha 255)
        int bgTop = ColorHelper.Argb.getArgb(255, 10, 15, 26);
        int bgBottom = ColorHelper.Argb.getArgb(255, 18, 24, 40);
        context.fillGradient(0, 0, width, height, bgTop, bgBottom);
        
        // Sidebar background - fully opaque
        context.fill(0, 0, sidebarWidth, height, ColorHelper.Argb.getArgb(255, 12, 16, 28));
        
        // Sidebar separator line with glow
        context.fill(sidebarWidth, 0, sidebarWidth + 3, height, ColorHelper.Argb.getArgb(180, 60, 80, 120));
        
        // Title
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 10, 0xFFFFFFFF);
        
        // Sidebar title
        context.drawTextWithShadow(textRenderer, "§7Категории", 10, 5, 0xFF8899AA);
        
        // Module count info
        List<Module> filtered = getFilteredModules();
        String info = "§7" + filtered.size() + " модул.";
        context.drawTextWithShadow(textRenderer, info, sidebarWidth + 10, 25, 0xFF667788);
        
        // Render module cards
        for (ModuleCard card : moduleCards) {
            renderModuleCard(context, card, mouseX, mouseY, delta);
        }
        
        // Scroll indicators
        if (scrollOffset > 0) {
            context.fill(width - 25, 35, width - 8, 55, ColorHelper.Argb.getArgb(150, 50, 60, 80));
            context.drawCenteredTextWithShadow(textRenderer, "▲", width - 16, 38, 0xFFFFFFFF);
        }
        
        int totalContentHeight = moduleCards.stream().mapToInt(ModuleCard::getTotalHeight).sum() + moduleCards.size() * 6;
        if (scrollOffset + getHeight() - 50 < totalContentHeight) {
            context.fill(width - 25, height - 55, width - 8, height - 35, ColorHelper.Argb.getArgb(150, 50, 60, 80));
            context.drawCenteredTextWithShadow(textRenderer, "▼", width - 16, height - 52, 0xFFFFFFFF);
        }
        
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderModuleCard(DrawContext context, ModuleCard card, int mouseX, int mouseY, float delta) {
        boolean hovered = card.isHovered(mouseX, mouseY);
        
        // Smooth hover animation
        if (hovered) {
            card.animAlpha = Math.min(1f, card.animAlpha + delta * 0.15f);
        } else {
            card.animAlpha = Math.max(0f, card.animAlpha - delta * 0.15f);
        }
        
        // Card background - reduced opacity
        int baseAlpha = card.module.isEnabled() ? 180 : 140;
        int alphaBoost = (int)(card.animAlpha * 30);
        
        // Glow effect for enabled modules - reduced intensity
        if (card.module.isEnabled()) {
            int glowSize = (int)(2 + card.animAlpha * 4);
            for (int g = glowSize; g > 0; g--) {
                int glowAlpha = (int)(15 * card.animAlpha * (1f - (float)g / glowSize));
                int glowColor = ColorHelper.Argb.getArgb(glowAlpha, 50, 200, 100);
                context.fill(card.x - g, card.y - g, card.x + card.width + g, card.y + card.height + g, glowColor);
            }
        }
        
        // Main card gradient - more transparent
        int topColor = ColorHelper.Argb.getArgb(baseAlpha + alphaBoost, 
            card.module.isEnabled() ? 20 : 15, 
            card.module.isEnabled() ? 35 : 20, 
            card.module.isEnabled() ? 50 : 35);
        int bottomColor = ColorHelper.Argb.getArgb(baseAlpha + alphaBoost, 
            card.module.isEnabled() ? 15 : 10, 
            card.module.isEnabled() ? 25 : 15, 
            card.module.isEnabled() ? 40 : 25);
        context.fillGradient(card.x, card.y, card.x + card.width, card.y + card.height, topColor, bottomColor);
        
        // Border - less intense
        int borderColor = ColorHelper.Argb.getArgb(80 + (int)(card.animAlpha * 100), 
            card.module.isEnabled() ? 50 : 40, 
            card.module.isEnabled() ? 180 : 100, 
            card.module.isEnabled() ? 100 : 80);
        context.fill(card.x - 1, card.y - 1, card.x + card.width + 1, card.y, borderColor);
        context.fill(card.x - 1, card.y + card.height, card.x + card.width + 1, card.y + card.height + 1, borderColor);
        context.fill(card.x - 1, card.y, card.x, card.y + card.height, borderColor);
        context.fill(card.x + card.width, card.y, card.x + card.width + 1, card.y + card.height, borderColor);
        
        // Module name and status
        String status = card.module.isEnabled() ? "§a[ON]" : "§c[OFF]";
        String moduleName = "§f" + card.module.getName();
        context.drawTextWithShadow(textRenderer, status, card.x + 8, card.y + 12, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, moduleName, card.x + 50, card.y + 12, 0xFFFFFF);
        
        // Toggle button
        int toggleBtnX = card.x + card.width - 70;
        int toggleBtnY = card.y + 10;
        int toggleBtnWidth = 60;
        int toggleBtnHeight = 20;
        
        int btnColor = card.module.isEnabled() ? 
            ColorHelper.Argb.getArgb(255, 40, 180, 80) : 
            ColorHelper.Argb.getArgb(255, 180, 50, 50);
        context.fill(toggleBtnX, toggleBtnY, toggleBtnX + toggleBtnWidth, toggleBtnY + toggleBtnHeight, btnColor);
        
        String btnText = card.module.isEnabled() ? "§fON" : "§fOFF";
        context.drawCenteredTextWithShadow(textRenderer, btnText, toggleBtnX + toggleBtnWidth/2, toggleBtnY + 6, 0xFFFFFF);
        
        // Expand button (if has settings)
        if (!card.module.getSettings().isEmpty()) {
            int expandBtnX = toggleBtnX - 35;
            int expandBtnY = toggleBtnY;
            int expandBtnWidth = 30;
            
            int expandColor = ColorHelper.Argb.getArgb(255, 60, 80, 120);
            context.fill(expandBtnX, expandBtnY, expandBtnX + expandBtnWidth, expandBtnY + toggleBtnHeight, expandColor);
            
            String expandText = card.expanded ? "§f▲" : "§f▼";
            context.drawCenteredTextWithShadow(textRenderer, expandText, expandBtnX + expandBtnWidth/2, expandBtnY + 6, 0xFFFFFF);
        }
        
        // Render settings if expanded
        if (card.expanded && !card.module.getSettings().isEmpty()) {
            int settingY = card.y + card.height + 6;
            
            for (Setting<?> setting : card.module.getSettings()) {
                if (setting instanceof BooleanSetting boolSetting) {
                    renderBooleanSetting(context, boolSetting, card.x, settingY, card.width, mouseX, mouseY);
                    settingY += 24;
                } else if (setting instanceof NumberSetting numberSetting) {
                    renderNumberSetting(context, numberSetting, card.x, settingY, card.width, mouseX, mouseY);
                    settingY += 24;
                }
            }
        }
    }

    private void renderBooleanSetting(DrawContext context, BooleanSetting setting, int x, int y, int width, int mouseX, int mouseY) {
        int labelX = x + 10;
        int toggleX = x + width - 50;
        int toggleY = y + 2;
        int toggleWidth = 40;
        int toggleHeight = 18;
        
        // Label
        context.drawTextWithShadow(textRenderer, "§7" + setting.getName() + ":", labelX, y + 4, 0xCCDDDD);
        
        // Toggle button
        int btnColor = setting.get() ? ColorHelper.Argb.getArgb(255, 40, 160, 80) : ColorHelper.Argb.getArgb(255, 160, 50, 50);
        context.fill(toggleX, toggleY, toggleX + toggleWidth, toggleY + toggleHeight, btnColor);
        
        String btnText = setting.get() ? "§fON" : "§fOFF";
        context.drawCenteredTextWithShadow(textRenderer, btnText, toggleX + toggleWidth/2, toggleY + 4, 0xFFFFFF);
        
        // Check click
        if (mouseX >= toggleX && mouseX <= toggleX + toggleWidth && mouseY >= toggleY && mouseY <= toggleY + toggleHeight) {
            // Hover effect
            context.fill(toggleX, toggleY, toggleX + toggleWidth, toggleY + toggleHeight, ColorHelper.Argb.getArgb(50, 255, 255, 255));
        }
    }

    private void renderNumberSetting(DrawContext context, NumberSetting setting, int x, int y, int width, int mouseX, int mouseY) {
        int labelX = x + 10;
        int btnWidth = 25;
        int btnHeight = 18;
        int valueWidth = 80;
        int controlStartX = x + width - btnWidth * 2 - valueWidth - 10;
        int startY = y + 2;
        
        // Label
        context.drawTextWithShadow(textRenderer, "§7" + setting.getName() + ":", labelX, y + 4, 0xCCDDDD);
        
        // Minus button
        int minusX = controlStartX;
        boolean minusHovered = mouseX >= minusX && mouseX <= minusX + btnWidth && mouseY >= startY && mouseY <= startY + btnHeight;
        int minusColor = minusHovered ? ColorHelper.Argb.getArgb(255, 80, 100, 140) : ColorHelper.Argb.getArgb(255, 60, 80, 120);
        context.fill(minusX, startY, minusX + btnWidth, startY + btnHeight, minusColor);
        context.drawCenteredTextWithShadow(textRenderer, "§f-", minusX + btnWidth/2, startY + 4, 0xFFFFFF);
        
        // Value display
        int valueX = minusX + btnWidth + 1;
        context.fill(valueX, startY, valueX + valueWidth, startY + btnHeight, ColorHelper.Argb.getArgb(255, 40, 50, 70));
        String valueText = format(setting.get());
        context.drawCenteredTextWithShadow(textRenderer, "§f" + valueText, valueX + valueWidth/2, startY + 4, 0xFFFFFF);
        
        // Plus button
        int plusX = valueX + valueWidth + 1;
        boolean plusHovered = mouseX >= plusX && mouseX <= plusX + btnWidth && mouseY >= startY && mouseY <= startY + btnHeight;
        int plusColor = plusHovered ? ColorHelper.Argb.getArgb(255, 80, 100, 140) : ColorHelper.Argb.getArgb(255, 60, 80, 120);
        context.fill(plusX, startY, plusX + btnWidth, startY + btnHeight, plusColor);
        context.drawCenteredTextWithShadow(textRenderer, "§f+", plusX + btnWidth/2, startY + 4, 0xFFFFFF);
        
        // Store positions in context for click detection (using matrix stack as temporary storage)
        context.getMatrices().push();
        context.getMatrices().translate(minusX, plusX, startY);
        context.getMatrices().pop();
    }

    private String format(double value) {
        return Math.abs(value - Math.rint(value)) < 1e-6 ? String.valueOf((int) Math.rint(value)) : String.format("%.2f", value);
    }

    private double getStep(NumberSetting setting) {
        return setting.getMax() - setting.getMin() > 20 ? 1.0 : 0.1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check module card clicks
        for (ModuleCard card : moduleCards) {
            if (card.isHovered((int)mouseX, (int)mouseY)) {
                int toggleBtnX = card.x + card.width - 70;
                int toggleBtnY = card.y + 10;
                
                // Toggle button click
                if (mouseX >= toggleBtnX && mouseX <= toggleBtnX + 60 && mouseY >= toggleBtnY && mouseY <= toggleBtnY + 20) {
                    card.module.toggle();
                    NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                    rebuild();
                    return true;
                }
                
                // Expand button click
                if (!card.module.getSettings().isEmpty()) {
                    int expandBtnX = toggleBtnX - 35;
                    if (mouseX >= expandBtnX && mouseX <= expandBtnX + 30 && mouseY >= toggleBtnY && mouseY <= toggleBtnY + 20) {
                        card.expanded = !card.expanded;
                        rebuild();
                        return true;
                    }
                }
            }
            
            // Check setting clicks for expanded cards
            if (card.expanded && !card.module.getSettings().isEmpty()) {
                int settingY = card.y + card.height + 6;
                for (Setting<?> setting : card.module.getSettings()) {
                    if (setting instanceof BooleanSetting boolSetting) {
                        int toggleX = card.x + card.width - 50;
                        if (mouseX >= toggleX && mouseX <= toggleX + 40 && mouseY >= settingY + 2 && mouseY <= settingY + 20) {
                            boolSetting.set(!boolSetting.get());
                            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                            rebuild();
                            return true;
                        }
                        settingY += 24;
                    } else if (setting instanceof NumberSetting numberSetting) {
                        int btnWidth = 25;
                        int btnHeight = 18;
                        int valueWidth = 80;
                        int startY = settingY + 2;
                        int controlStartX = card.x + card.width - btnWidth * 2 - valueWidth - 10;
                        int minusX = controlStartX;
                        int plusX = minusX + btnWidth + valueWidth + 1;
                        
                        if (mouseX >= minusX && mouseX <= minusX + btnWidth && mouseY >= startY && mouseY <= startY + btnHeight) {
                            numberSetting.set(Math.max(numberSetting.getMin(), numberSetting.get() - getStep(numberSetting)));
                            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                            rebuild();
                            return true;
                        }
                        if (mouseX >= plusX && mouseX <= plusX + btnWidth && mouseY >= startY && mouseY <= startY + btnHeight) {
                            numberSetting.set(Math.min(numberSetting.getMax(), numberSetting.get() + getStep(numberSetting)));
                            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                            rebuild();
                            return true;
                        }
                        settingY += 24;
                    }
                }
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseX > sidebarWidth) { // Only scroll in main content area
            List<Module> modules = getFilteredModules();
            int maxScroll = Math.max(0, moduleCards.stream().mapToInt(ModuleCard::getTotalHeight).sum() + moduleCards.size() * 6 - maxContentHeight);
            
            if (verticalAmount < 0) {
                scrollOffset = Math.min(maxScroll, scrollOffset + 20);
            } else {
                scrollOffset = Math.max(0, scrollOffset - 20);
            }
            rebuild();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    protected void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Empty method to prevent rendering blurred world background
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
