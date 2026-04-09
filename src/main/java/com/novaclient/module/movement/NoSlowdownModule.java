package com.novaclient.module.movement;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.util.math.Vec3d;

public final class NoSlowdownModule extends Module {
    public final NumberSetting multiplier = addSetting(new NumberSetting("Multiplier", 1.08, 1.0, 1.3));

    public NoSlowdownModule() {
        super("No Slowdown", "Смягчение slowdown в паутине (легит)", Category.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null) return;
        if (!ClientRefs.MC.player.isUsingItem()) return;
        Vec3d vel = ClientRefs.MC.player.getVelocity();
        double factor = multiplier.get();
        ClientRefs.MC.player.setVelocity(vel.x * factor, vel.y, vel.z * factor);
    }
}
