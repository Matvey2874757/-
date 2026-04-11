package net.novaclient.ui.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.of("NovaClient Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 50;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Reset All Settings"), button -> {
            // Reset config logic here
        }).dimensions(centerX - 100, y, 200, 20).build());
        y += 30;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Open Config Folder"), button -> {
            // Open config folder logic here
        }).dimensions(centerX - 100, y, 200, 20).build());
        y += 40;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            assert this.client != null;
            this.client.setScreen(parent);
        }).dimensions(centerX - 100, y, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xCC000000);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
