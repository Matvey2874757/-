package com.novaclient.module.render;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;
import net.minecraft.text.Text;

public final class LowDurabilityWarningModule extends Module {
    public final NumberSetting threshold = addSetting(new NumberSetting("Threshold %", 15, 1, 40));
    public final BooleanSetting checkOffhand = addSetting(new BooleanSetting("Check Offhand", true));
    private long lastWarn;

    public LowDurabilityWarningModule() {
        super("Low Durability Warning", "Предупреждение при низкой прочности", Category.RENDER);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null) return;
        int minPct = 101;
        var main = ClientRefs.MC.player.getMainHandStack();
        if (main.isDamageable()) {
            minPct = Math.min(minPct, (int) (((main.getMaxDamage() - main.getDamage()) * 100.0) / main.getMaxDamage()));
        }
        var off = ClientRefs.MC.player.getOffHandStack();
        if (checkOffhand.get() && off.isDamageable()) {
            minPct = Math.min(minPct, (int) (((off.getMaxDamage() - off.getDamage()) * 100.0) / off.getMaxDamage()));
        }
        if (minPct <= threshold.get().intValue() && System.currentTimeMillis() - lastWarn > 3000) {
            ClientRefs.MC.player.sendMessage(Text.literal("§c[NovaClient] Низкая прочность: " + minPct + "%"), true);
            lastWarn = System.currentTimeMillis();
        }
    }
}
