package com.novaclient.module.combat;

import com.novaclient.module.*;

public final class AimAssistModule extends Module {
    public final NumberSetting strength = addSetting(new NumberSetting("Strength %", 8, 0, 20));

    public AimAssistModule() {
        super("Aim Assist", "Очень мягкое визуальное сопровождение цели", Category.COMBAT);
    }
}
