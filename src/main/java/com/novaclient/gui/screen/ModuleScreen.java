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

import java.util.List;

public final class ModuleScreen extends Screen {
    private final Screen parent;
    private boolean enabledOnly;
    private Category filter;

    public ModuleScreen(Screen parent) {
        super(Text.literal("Модули NovaClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        rebuild();
    }

    private void rebuild() {
        clearChildren();

        addDrawableChild(ButtonWidget.builder(enabledLabel(), b -> {
            enabledOnly = !enabledOnly;
            rebuild();
        }).dimensions(12, 12, 120, 20).build());
        addDrawableChild(ButtonWidget.builder(categoryLabel(), b -> {
            filter = nextCategory(filter);
            rebuild();
        }).dimensions(136, 12, 170, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Вкл. все"), b -> {
            NovaClient.MODULE_MANAGER.enableAll();
            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
            rebuild();
        }).dimensions(310, 12, 90, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Выкл. все"), b -> {
            NovaClient.MODULE_MANAGER.disableAll();
            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
            rebuild();
        }).dimensions(404, 12, 90, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Сброс"), b -> {
            NovaClient.MODULE_MANAGER.resetAllSettings();
            NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
            rebuild();
        }).dimensions(498, 12, 80, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Кат. ON"), b -> {
            if (filter != null) {
                NovaClient.MODULE_MANAGER.toggleCategory(filter, true);
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                rebuild();
            }
        }).dimensions(582, 12, 76, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Кат. OFF"), b -> {
            if (filter != null) {
                NovaClient.MODULE_MANAGER.toggleCategory(filter, false);
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                rebuild();
            }
        }).dimensions(662, 12, 82, 20).build());

        int x = width / 2 - 100;
        int y = 40 + 16;
        for (Module module : filteredModules()) {
            addDrawableChild(ButtonWidget.builder(label(module), b -> {
                module.toggle();
                b.setMessage(label(module));
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
            }).dimensions(x, y, 200, 20).build());
            y += 22;

            for (Setting<?> setting : module.getSettings()) {
                if (setting instanceof BooleanSetting boolSetting) {
                    addDrawableChild(ButtonWidget.builder(booleanLabel(boolSetting), b -> {
                        boolSetting.set(!boolSetting.get());
                        b.setMessage(booleanLabel(boolSetting));
                        NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                    }).dimensions(x + 8, y, 192, 18).build());
                    y += 20;
                } else if (setting instanceof NumberSetting numberSetting) {
                    ButtonWidget labelButton = ButtonWidget.builder(numberLabel(numberSetting), b -> {
                    }).dimensions(x + 36, y, 136, 18).build();
                    labelButton.active = false;
                    addDrawableChild(labelButton);

                    addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> {
                        numberSetting.set(numberSetting.get() - step(numberSetting));
                        updateNumberButtons(numberSetting, labelButton);
                        NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                    }).dimensions(x + 8, y, 24, 18).build());

                    addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> {
                        numberSetting.set(numberSetting.get() + step(numberSetting));
                        updateNumberButtons(numberSetting, labelButton);
                        NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
                    }).dimensions(x + 176, y, 24, 18).build());
                    y += 20;
                }
            }

            if (y > height - 50) {
                x += 210;
                y = 40;
            }
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Назад"), b -> client.setScreen(parent)).dimensions(width / 2 - 50, height - 28, 100, 20).build());
    }

    private List<Module> filteredModules() {
        return NovaClient.MODULE_MANAGER.getModules().stream()
                .filter(m -> !enabledOnly || m.isEnabled())
                .filter(m -> filter == null || m.getCategory() == filter)
                .toList();
    }

    private Text label(Module m) {
        return Text.literal((m.isEnabled() ? "§a[ON] " : "§c[OFF] ") + m.getName());
    }

    private Text enabledLabel() {
        return Text.literal(enabledOnly ? "Только: §aON" : "Показывать: Все");
    }

    private Text categoryLabel() {
        return Text.literal("Категория: " + (filter == null ? "Все" : filter.displayName()));
    }

    private Category nextCategory(Category current) {
        if (current == null) return Category.values()[0];
        int idx = current.ordinal() + 1;
        return idx >= Category.values().length ? null : Category.values()[idx];
    }

    private Text booleanLabel(BooleanSetting setting) {
        return Text.literal("  " + setting.getName() + ": " + (setting.get() ? "§aON" : "§cOFF"));
    }

    private Text numberLabel(NumberSetting setting) {
        return Text.literal(" " + setting.getName() + ": " + format(setting.get()));
    }

    private String format(double value) {
        return Math.abs(value - Math.rint(value)) < 1e-6 ? String.valueOf((int) Math.rint(value)) : String.format("%.2f", value);
    }

    private double step(NumberSetting setting) {
        return setting.getMax() - setting.getMin() > 20 ? 1.0 : 0.1;
    }

    private void updateNumberButtons(NumberSetting setting, ButtonWidget labelButton) {
        labelButton.setMessage(numberLabel(setting));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, width, height, 0xEE111521, 0xEE1A2030);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
