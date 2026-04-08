package com.novaclient.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

public final class CustomMainMenuScreen extends Screen {
    private float anim;
    private final Screen parent;

    public CustomMainMenuScreen(Screen parent) {
        super(Text.literal("NovaClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = width / 2;
        int y = height / 2 - 40;
        addDrawableChild(ButtonWidget.builder(Text.literal("Одиночная игра"), b -> client.setScreen(new SelectWorldScreen(this))).dimensions(cx - 80, y, 160, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Сетевая игра"), b -> client.setScreen(new MultiplayerScreen(this))).dimensions(cx - 80, y + 24, 160, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Модули"), b -> client.setScreen(new ModuleScreen(this))).dimensions(cx - 80, y + 48, 160, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Настройки"), b -> client.setScreen(new OptionsScreen(this, client.options))).dimensions(cx - 80, y + 72, 160, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Выход"), b -> MinecraftClient.getInstance().scheduleStop()).dimensions(cx - 80, y + 96, 160, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        anim = Math.min(1f, anim + delta * 0.05f);
        float nx = (mouseX - width / 2f) / width;
        float ny = (mouseY - height / 2f) / height;

        int bg1 = ColorHelper.Argb.getArgb(255, 18, 24, 40);
        int bg2 = ColorHelper.Argb.getArgb(255, 30, 42, 72);
        context.fillGradient(0, 0, width, height, bg1, bg2);

        int px = (int) (nx * 18);
        int py = (int) (ny * 18);
        context.fillGradient(-40 + px, -40 + py, width + 40 + px, height / 2 + py,
                ColorHelper.Argb.getArgb(110, 0, 175, 255), ColorHelper.Argb.getArgb(30, 0, 0, 0));

        int alpha = (int) (anim * 255);
        context.drawCenteredTextWithShadow(textRenderer, "NovaClient", width / 2, 36,
                ColorHelper.Argb.getArgb(alpha, 255, 255, 255));
        context.drawCenteredTextWithShadow(textRenderer, "Fabric 1.21.1 | Легальный QoL клиент", width / 2, 52,
                ColorHelper.Argb.getArgb(alpha, 180, 210, 255));

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
