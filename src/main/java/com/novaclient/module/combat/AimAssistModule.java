package com.novaclient.module.combat;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class AimAssistModule extends Module {
    public final NumberSetting strength = addSetting(new NumberSetting("Strength %", 8, 0, 20));
    public final NumberSetting range = addSetting(new NumberSetting("Range", 5.0, 2.0, 8.0));
    public final NumberSetting fov = addSetting(new NumberSetting("FOV", 90, 20, 180));

    public AimAssistModule() {
        super("Aim Assist", "Очень мягкое визуальное сопровождение цели", Category.COMBAT);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null || ClientRefs.MC.world == null) return;
        if (!ClientRefs.MC.options.attackKey.isPressed()) return;

        PlayerEntity self = ClientRefs.MC.player;
        LivingEntity target = ClientRefs.MC.world.getEntitiesByClass(LivingEntity.class,
                        self.getBoundingBox().expand(range.get()),
                        e -> e.isAlive() && e != self)
                .stream()
                .min((a, b) -> Double.compare(self.squaredDistanceTo(a), self.squaredDistanceTo(b)))
                .orElse(null);
        if (target == null) return;

        Vec3d eyes = self.getEyePos();
        Vec3d aim = target.getBoundingBox().getCenter().subtract(eyes);
        double horizontal = Math.sqrt(aim.x * aim.x + aim.z * aim.z);
        float targetYaw = (float) (MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(aim.z, aim.x)) - 90.0));
        float targetPitch = (float) (-Math.toDegrees(Math.atan2(aim.y, horizontal)));
        float yawDelta = MathHelper.wrapDegrees(targetYaw - self.getYaw());
        if (Math.abs(yawDelta) > fov.get() * 0.5f) return;

        float factor = (float) (strength.get() / 100.0);
        float newYaw = self.getYaw() + yawDelta * factor;
        float newPitch = self.getPitch() + (targetPitch - self.getPitch()) * factor;

        self.setYaw(newYaw);
        self.setPitch(MathHelper.clamp(newPitch, -90.0f, 90.0f));
    }
}
