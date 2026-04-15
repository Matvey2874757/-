package com.novaclient.module.movement;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.util.ClientRefs;

public final class AutoRespawnModule extends Module {
    public AutoRespawnModule() {
        super("Auto Respawn", "Автоматически возрождает после смерти", Category.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null) {
            return;
        }
        if (ClientRefs.MC.player.isDead()) {
            ClientRefs.MC.player.requestRespawn();
        }
    }
}
