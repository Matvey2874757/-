package com.novaclient.module.movement;

import com.novaclient.module.Category;
import com.novaclient.module.BooleanSetting;
import com.novaclient.module.Module;
import com.novaclient.util.ClientRefs;

public final class SprintModule extends Module {
    public final BooleanSetting requireFullHunger = addSetting(new BooleanSetting("Require Hunger", true));

    public SprintModule() {
        super("Sprint", "Автоспринт без конфликтов", Category.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null) return;
        var p = ClientRefs.MC.player;
        if (requireFullHunger.get() && p.getHungerManager().getFoodLevel() <= 6) return;
        if (p.forwardSpeed > 0 && !p.horizontalCollision && !p.isSneaking() && !p.isTouchingWater()) {
            p.setSprinting(true);
        }
    }
}
