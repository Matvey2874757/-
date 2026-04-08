package com.novaclient.module.movement;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.util.ClientRefs;

public final class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", "Автоспринт без конфликтов", Category.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null) return;
        var p = ClientRefs.MC.player;
        if (p.forwardSpeed > 0 && !p.isSneaking() && !p.isTouchingWater()) {
            p.setSprinting(true);
        }
    }
}
