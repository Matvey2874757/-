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
    private final int sidebarWidth = 120;
    private boolean showEnabledOnly = false;

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

        // Sidebar category buttons
        int categoryY = 10;
        
        // All categories button
        addDrawableChild(ButtonWidget.builder(
            Text.literal(selectedCategory == null ? "§a> Все <" : "   Все"),
            b -> {
                selectedCategory = null;
                scrollOffset = 0;
                rebuild();
            }
        ).dimensions(0, categoryY, sidebarWidth, 24).build());
        categoryY += 26;

        // Individual category buttons
        for (Category cat : Category.values()) {
            long count = NovaClient.MODULE_MANAGER.getModules().stream()
                    .filter(m -> m.getCategory() == cat)
                    .count();
            boolean isSelected = selectedCategory == cat;
            String prefix = isSelected ? "§a> " : "   ";
            String suffix = " §7(" + count + ")";
            
            addDrawableChild(ButtonWidget.builder(
                Text.literal(prefix + cat.displayName() + suffix),
                b -> {
                    selectedCategory = cat;
                    scrollOffset = 0;
                    rebuild();
                }
            ).dimensions(0, categoryY, sidebarWidth, 24).build());
            categoryY += 26;
        }

        // Filter toggle button at bottom of sidebar
        addDrawableChild(ButtonWidget.builder(
            Text.literal(showEnabledOnly ? "§aТолько ON" : "Все модули"),
            b -> {
                showEnabledOnly = !showEnabledOnly;
                scrollOffset = 0;
                rebuild();
            }
        ).dimensions(0, height - 60, sidebarWidth, 20).build());

        // Back button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Назад"),
            b -> client.setScreen(parent)
        ).dimensions(0, height - 35, sidebarWidth, 20).build());

        // Main content area - module list
        List<Module> modules = getFilteredModules();
        int contentX = sidebarWidth + 20;
        int contentY = 15;
        int maxVisibleHeight = height - 50;
        
        // Title
        String titleText = "Модули";
        if (selectedCategory != null) {
            titleText += " > " + selectedCategory.displayName();
        }
        if (showEnabledOnly) {
            titleText += " [Только активные]";
        }
        
        // Draw modules
        int visibleCount = 0;
        for (int i = scrollOffset; i < modules.size(); i++) {
            Module module = modules.get(i);
            if (contentY + (visibleCount * 70) > maxVisibleHeight) {
                break;
            }
            
            int moduleY = contentY + (visibleCount * 70);
            
            // Module main button
            addDrawableChild(ButtonWidget.builder(
                getModuleLabel(module),
                b -> {
                    module.toggle();
                    NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                    rebuild();
                }
            ).dimensions(contentX, moduleY, 180, 20).build());
            
            // Settings for this module
            int settingX = contentX + 10;
            int settingY = moduleY + 25;
            
            for (Setting<?> setting : module.getSettings()) {
                if (settingY + (module.getSettings().size() * 22) > maxVisibleHeight + contentY) {
                    break;
                }
                
                if (setting instanceof BooleanSetting boolSetting) {
                    addDrawableChild(ButtonWidget.builder(
                        getBooleanLabel(boolSetting),
                        b -> {
                            boolSetting.set(!boolSetting.get());
                            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                            rebuild();
                        }
                    ).dimensions(settingX, settingY, 160, 18).build());
                    settingY += 20;
                } else if (setting instanceof NumberSetting numberSetting) {
                    ButtonWidget labelButton = ButtonWidget.builder(
                        getNumberLabel(numberSetting),
                        b -> {}
                    ).dimensions(settingX + 30, settingY, 100, 18).build();
                    labelButton.active = false;
                    addDrawableChild(labelButton);

                    addDrawableChild(ButtonWidget.builder(
                        Text.literal("◀"),
                        b -> {
                            numberSetting.set(numberSetting.get() - getStep(numberSetting));
                            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                            rebuild();
                        }
                    ).dimensions(settingX, settingY, 26, 18).build());

                    addDrawableChild(ButtonWidget.builder(
                        Text.literal("▶"),
                        b -> {
                            numberSetting.set(numberSetting.get() + getStep(numberSetting));
                            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                            rebuild();
                        }
                    ).dimensions(settingX + 134, settingY, 26, 18).build());
                    settingY += 20;
                }
            }
            
            visibleCount++;
        }

        // Scroll indicators
        if (scrollOffset > 0) {
            addDrawableChild(ButtonWidget.builder(
                Text.literal("▲"),
                b -> {
                    scrollOffset = Math.max(0, scrollOffset - 5);
                    rebuild();
                }
            ).dimensions(width - 30, 10, 25, 20).build());
        }
        
        if (scrollOffset + visibleCount < modules.size()) {
            addDrawableChild(ButtonWidget.builder(
                Text.literal("▼"),
                b -> {
                    scrollOffset = Math.min(modules.size() - 1, scrollOffset + 5);
                    rebuild();
                }
            ).dimensions(width - 30, height - 35, 25, 20).build());
        }
        
        // Quick actions at top right
        int actionX = sidebarWidth + 20;
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Вкл. все"),
            b -> {
                NovaClient.MODULE_MANAGER.enableAll();
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                rebuild();
            }
        ).dimensions(actionX, 0, 80, 18).build());
        
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Выкл. все"),
            b -> {
                NovaClient.MODULE_MANAGER.disableAll();
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                rebuild();
            }
        ).dimensions(actionX + 85, 0, 85, 18).build());
        
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Сброс"),
            b -> {
                NovaClient.MODULE_MANAGER.resetAllSettings();
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                rebuild();
            }
        ).dimensions(actionX + 175, 0, 60, 18).build());
    }

    private List<Module> getFilteredModules() {
        return NovaClient.MODULE_MANAGER.getModules().stream()
                .filter(m -> selectedCategory == null || m.getCategory() == selectedCategory)
                .filter(m -> !showEnabledOnly || m.isEnabled())
                .toList();
    }

    private Text getModuleLabel(Module m) {
        return Text.literal((m.isEnabled() ? "§a[ON] " : "§c[OFF] ") + m.getName());
    }

    private Text getBooleanLabel(BooleanSetting setting) {
        return Text.literal("  " + setting.getName() + ": " + (setting.get() ? "§aON" : "§cOFF"));
    }

    private Text getNumberLabel(NumberSetting setting) {
        return Text.literal(" " + setting.getName() + ": " + format(setting.get()));
    }

    private String format(double value) {
        return Math.abs(value - Math.rint(value)) < 1e-6 ? String.valueOf((int) Math.rint(value)) : String.format("%.2f", value);
    }

    private double getStep(NumberSetting setting) {
        return setting.getMax() - setting.getMin() > 20 ? 1.0 : 0.1;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Background gradient
        context.fillGradient(0, 0, width, height, 0xFF0F141F, 0xFF1A2030);
        
        // Sidebar background
        context.fill(0, 0, sidebarWidth, height, ColorHelper.Argb.getArgb(200, 15, 20, 35));
        
        // Sidebar separator line
        context.fill(sidebarWidth, 0, sidebarWidth + 2, height, ColorHelper.Argb.getArgb(100, 50, 60, 80));
        
        // Title
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 8, 0xFFFFFFFF);
        
        // Sidebar title
        context.drawTextWithShadow(textRenderer, "Категории", 5, 5, 0xFFAAAAAA);
        
        // Module count info
        List<Module> filtered = getFilteredModules();
        String info = filtered.size() + " модул.";
        context.drawTextWithShadow(textRenderer, info, sidebarWidth + 10, 22, 0xFF888888);
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (mouseX > sidebarWidth) { // Only scroll in main content area
            List<Module> modules = getFilteredModules();
            if (verticalAmount < 0) {
                scrollOffset = Math.min(modules.size() - 1, scrollOffset + 3);
            } else {
                scrollOffset = Math.max(0, scrollOffset - 3);
            }
            rebuild();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
