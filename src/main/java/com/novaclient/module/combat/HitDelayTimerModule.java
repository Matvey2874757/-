package com.novaclient.module.combat;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;

public final class HitDelayTimerModule extends Module {
    public final NumberSetting x = addSetting(new NumberSetting("X", 8, 0, 2000));
    public final NumberSetting y = addSetting(new NumberSetting("Y", 188, 0, 2000));

    public HitDelayTimerModule() {
        super("Hit Delay Timer", "Визуальный таймер до следующего удара", Category.COMBAT);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null || ClientRefs.MC.textRenderer == null) return;
        float progress = ClientRefs.MC.player.getAttackCooldownProgress(0.0f);
        int percent = Math.round(progress * 100f);
        int x = this.x.get().intValue();
        int y = this.y.get().intValue();
        int color = percent >= 92 ? 0xFF66FF66 : 0xFFFFFFFF;
        context.drawTextWithShadow(ClientRefs.MC.textRenderer, "Hit Delay: " + percent + "%", x, y, color);
    }
}
