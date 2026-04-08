package com.novaclient.module.render;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;
import net.minecraft.text.Text;

public final class LowDurabilityWarningModule extends Module {
    public final NumberSetting threshold = addSetting(new NumberSetting("Threshold %", 15, 1, 40));
    private long lastWarn;

    public LowDurabilityWarningModule() {
        super("Low Durability Warning", "Предупреждение при низкой прочности", Category.RENDER);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null) return;
        var stack = ClientRefs.MC.player.getMainHandStack();
        if (!stack.isDamageable()) return;
        int max = stack.getMaxDamage();
        int left = max - stack.getDamage();
        int pct = (int) ((left * 100.0) / max);
        if (pct <= threshold.get().intValue() && System.currentTimeMillis() - lastWarn > 3000) {
            ClientRefs.MC.player.sendMessage(Text.literal("§c[NovaClient] Низкая прочность: " + pct + "%"), true);
            lastWarn = System.currentTimeMillis();
        }
    }
}
