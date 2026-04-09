package com.novaclient.module.movement;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.util.ClientRefs;

public final class AutoJumpModule extends Module {
    public AutoJumpModule() {
        super("Auto Jump", "Автоматический прыжок при движении", Category.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null || ClientRefs.MC.currentScreen != null) {
            return;
        }

        boolean moving = ClientRefs.MC.options.forwardKey.isPressed() || ClientRefs.MC.options.leftKey.isPressed()
                || ClientRefs.MC.options.rightKey.isPressed() || ClientRefs.MC.options.backKey.isPressed();

        if (moving && ClientRefs.MC.player.isOnGround()) {
            ClientRefs.MC.player.jump();
        }
    }
}
