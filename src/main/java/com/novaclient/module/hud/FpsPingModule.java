package com.novaclient.module.hud;

import com.novaclient.module.BooleanSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class FpsPingModule extends HudTextModule {
    public final BooleanSetting compact = addSetting(new BooleanSetting("Compact", false));

    public FpsPingModule() {
        super("FPS / Ping", "Показывает FPS и ping", 8, 108);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        int fps = ClientRefs.MC.getCurrentFps();
        int ping = 0;
        if (ClientRefs.MC.getNetworkHandler() != null && ClientRefs.MC.player != null && ClientRefs.MC.getNetworkHandler().getPlayerListEntry(ClientRefs.MC.player.getUuid()) != null) {
            ping = ClientRefs.MC.getNetworkHandler().getPlayerListEntry(ClientRefs.MC.player.getUuid()).getLatency();
        }
        drawLine(context, compact.get() ? (fps + " FPS / " + ping + "ms") : ("FPS: " + fps + " | Ping: " + ping), 0, 0xFFFFFFFF);
    }
}
