package com.novaclient.module.performance;

import com.novaclient.module.*;

public final class ParticleLimitModule extends Module {
    public final NumberSetting maxParticles = addSetting(new NumberSetting("Max Particles", 4000, 500, 10000));

    public ParticleLimitModule() {
        super("Particle Limits", "Ограничение количества частиц", Category.PERFORMANCE);
    }
}
