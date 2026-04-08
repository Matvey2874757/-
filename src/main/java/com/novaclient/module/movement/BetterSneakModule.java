package com.novaclient.module.movement;

import com.novaclient.module.*;

public final class BetterSneakModule extends Module {
    public final BooleanSetting toggle = addSetting(new BooleanSetting("Toggle", true));

    public BetterSneakModule() {
        super("Better Sneak", "Toggle-sneak без изменения высоты глаз", Category.MOVEMENT);
    }
}
