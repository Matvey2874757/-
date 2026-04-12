package com.novaclient.module.hud;

import com.novaclient.module.BooleanSetting;
import com.novaclient.util.ClientRefs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public final class ScreenshotManagerModule extends HudTextModule {
    public final BooleanSetting showHud = addSetting(new BooleanSetting("Show HUD", true));
    private long lastRefreshAt;
    private int screenshotsCount;

    public ScreenshotManagerModule() {
        super("Screenshot Manager", "Просмотр скриншотов внутри клиента", 8, 188);
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
        
        String text = "Screenshots: " + screenshotsCount;
        int width = getTextWidth(text);
        int height = 10;
        
        int renderX = (int)(x.get().doubleValue() * scale.get().doubleValue());
        int renderY = (int)(y.get().doubleValue() * scale.get().doubleValue());
        
        renderBackground(context, width, height);
        
        // Текст
        context.drawTextWithShadow(
            ClientRefs.MC.textRenderer,
            text,
            renderX,
            renderY,
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
