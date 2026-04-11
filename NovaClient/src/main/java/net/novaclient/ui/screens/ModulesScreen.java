package net.novaclient.ui.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.novaclient.module.Module;
import net.novaclient.module.ModuleManager;
import net.novaclient.ui.widgets.ToggleButtonWidget;

import java.util.List;

public class ModulesScreen extends Screen {
    private final Screen parent;
    private int scrollOffset = 0;

    public ModulesScreen(Screen parent) {
        super(Text.of("NovaClient Modules"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        List<Module> modules = ModuleManager.getModules();
        int y = 40;
        
        // Draw category headers and modules
        String[] categories = {"Render", "HUD", "Movement", "PvP", "Performance"};
        
        for (String category : categories) {
            List<Module> categoryModules = ModuleManager.getModulesByCategory(category);
            if (!categoryModules.isEmpty()) {
                y += 10;
                for (Module module : categoryModules) {
                    this.addDrawableChild(new ToggleButtonWidget(
                        50, y, 200, 18,
                        Text.literal(module.getName()),
                        button -> module.toggle(),
                        module.isEnabled()
                    ));
                    y += 22;
                }
                y += 5;
            }
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            assert this.client != null;
            this.client.setScreen(parent);
        }).dimensions(this.width - 100, this.height - 30, 90, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Semi-transparent dark background
        context.fill(0, 0, width, height, 0xCC000000);
        
        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        
        // Instructions
        String instructions = "Click to toggle modules";
        context.drawCenteredTextWithShadow(this.textRenderer, instructions, this.width / 2, 25, 0xAAAAAA);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
