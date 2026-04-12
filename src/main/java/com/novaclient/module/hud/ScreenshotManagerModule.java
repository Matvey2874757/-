package com.novaclient.module.hud;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.BooleanSetting;
import com.novaclient.util.ClientRefs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public final class ScreenshotManagerModule extends Module {
    public final BooleanSetting showHud = addSetting(new BooleanSetting("Show HUD", true));
    private long lastRefreshAt;
    private int screenshotsCount;

    public ScreenshotManagerModule() {
        super("Screenshot Manager", "Просмотр скриншотов внутри клиента", Category.HUD);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.options == null) return;
        long now = System.currentTimeMillis();
        if (now - lastRefreshAt > 2000) {
            screenshotsCount = countScreenshots();
            lastRefreshAt = now;
        }
        if (ClientRefs.MC.options.screenshotKey.wasPressed() && ClientRefs.MC.player != null) {
            ClientRefs.MC.player.sendMessage(Text.literal("§7[NovaClient] Скриншотов в папке: §b" + screenshotsCount), true);
        }
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (!showHud.get() || ClientRefs.MC.textRenderer == null) return;
        
        // Рисуем фон для скриншот менеджера
        String text = "Screenshots: " + screenshotsCount;
        int width = getTextWidth(text);
        int height = 10;
        
        int startX = (int)(x.get().intValue() * scale.get());
        int startY = (int)(y.get().intValue() * scale.get());
        
        // Фон
        context.fill(startX - 3, startY - 2, startX + width + 3, startY + height + 2, 0xAA000000);
        
        // Синяя линия сверху
        context.drawHorizontalLine(startX - 2, startX + width + 2, startY - 2, 0xFF3498DB);
        
        // Текст
        context.drawTextWithShadow(
            ClientRefs.MC.textRenderer,
            text,
            startX,
            startY,
            0xFFE5E5E5
        );
    }

    private int countScreenshots() {
        Path dir = FabricLoader.getInstance().getGameDir().resolve("screenshots");
        if (!Files.isDirectory(dir)) return 0;
        try (Stream<Path> files = Files.list(dir)) {
            return (int) files.filter(Files::isRegularFile).count();
        } catch (IOException ignored) {
            return 0;
        }
    }
}
