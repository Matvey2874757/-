package com.novaclient.gui.screen;

import com.novaclient.NovaClient;
import com.novaclient.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class ModuleScreen extends Screen {
    private final Screen parent;

    public ModuleScreen(Screen parent) {
        super(Text.literal("Модули NovaClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = width / 2 - 100;
        int y = 40;
        for (Module module : NovaClient.MODULE_MANAGER.getModules()) {
            addDrawableChild(ButtonWidget.builder(label(module), b -> {
                module.toggle();
                b.setMessage(label(module));
                NovaClient.CONFIG.save(NovaClient.MODULE_MANAGER);
            }).dimensions(x, y, 200, 20).build());
            y += 22;
            if (y > height - 50) {
                x += 210;
                y = 40;
            }
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Назад"), b -> client.setScreen(parent)).dimensions(width / 2 - 50, height - 28, 100, 20).build());
    }

    private Text label(Module m) {
        return Text.literal((m.isEnabled() ? "§a[ON] " : "§c[OFF] ") + m.getName());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, width, height, 0xEE111521, 0xEE1A2030);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
