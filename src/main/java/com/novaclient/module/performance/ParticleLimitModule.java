package com.novaclient.module.performance;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.option.ParticlesMode;

public final class ParticleLimitModule extends Module {
    public final NumberSetting maxParticles = addSetting(new NumberSetting("Max Particles", 4000, 500, 10000));
    public final BooleanSetting fpsProtect = addSetting(new BooleanSetting("FPS Protect", true));
    private ParticlesMode previousMode;

    public ParticleLimitModule() {
        super("Particle Limits", "Ограничение количества частиц", Category.PERFORMANCE);
    }

    @Override
    protected void onEnable() {
        if (ClientRefs.MC.options == null) return;
        previousMode = ClientRefs.MC.options.getParticles().getValue();
        applyMode();
    }

    @Override
    public void tick() {
        applyMode();
    }

    @Override
    protected void onDisable() {
        if (ClientRefs.MC.options == null || previousMode == null) return;
        ClientRefs.MC.options.getParticles().setValue(previousMode);
    }

    private void applyMode() {
        if (ClientRefs.MC.options == null) return;
        double limit = maxParticles.get();
        ParticlesMode mode = limit <= 1500 ? ParticlesMode.MINIMAL : (limit <= 4500 ? ParticlesMode.DECREASED : ParticlesMode.ALL);
        if (fpsProtect.get() && ClientRefs.MC.getCurrentFps() < 45) {
            mode = ParticlesMode.MINIMAL;
        }
        if (ClientRefs.MC.options.getParticles().getValue() != mode) {
            ClientRefs.MC.options.getParticles().setValue(mode);
        }
    }
}
