package com.novaclient.module.performance;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class MemoryUsageModule extends Module {
    public MemoryUsageModule() {
        super("Memory Usage", "Показывает использование памяти JVM", Category.PERFORMANCE);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (MinecraftClient.getInstance().textRenderer == null) {
            return;
        }

        Runtime runtime = Runtime.getRuntime();
        long used = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        long max = runtime.maxMemory() / (1024 * 1024);

        int x = 8;
        int y = 120;
        context.fill(x - 2, y - 2, x + 128, y + 12, 0x90182742);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                "Memory: " + used + "MB / " + max + "MB", x, y, 0xFFDDF0FF);
    }
}
