package com.novaclient.module.hud;

import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class SpeedometerModule extends HudTextModule {
    public SpeedometerModule() {
        super("Speedometer", "Показывает скорость движения", 8, 136);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null || ClientRefs.MC.textRenderer == null) return;
        double speed = Math.sqrt(ClientRefs.MC.player.getVelocity().x * ClientRefs.MC.player.getVelocity().x
                + ClientRefs.MC.player.getVelocity().z * ClientRefs.MC.player.getVelocity().z) * 20.0;
        context.fill(x - 2, y - 2, x + 108, y + 12, 0x90182742);
        drawLine(context, String.format("Speed: %.2f b/s", speed), 0, 0xFFDDF0FF);
    }
}
