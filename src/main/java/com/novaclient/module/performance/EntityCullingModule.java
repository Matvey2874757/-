package com.novaclient.module.performance;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;

public final class EntityCullingModule extends Module {
    public final NumberSetting distanceScale = addSetting(new NumberSetting("Distance Scale", 0.75, 0.2, 1.0));
    private double previousDistanceScaling = -1;

    public EntityCullingModule() {
        super("Entity Culling", "Пропуск рендера скрытых энтити", Category.PERFORMANCE);
    }

    @Override
    protected void onEnable() {
        if (ClientRefs.MC.options == null) return;
        previousDistanceScaling = ClientRefs.MC.options.getEntityDistanceScaling().getValue();
        ClientRefs.MC.options.getEntityDistanceScaling().setValue(distanceScale.get());
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.options == null) return;
        ClientRefs.MC.options.getEntityDistanceScaling().setValue(distanceScale.get());
    }

    @Override
    protected void onDisable() {
        if (ClientRefs.MC.options == null || previousDistanceScaling < 0) return;
        ClientRefs.MC.options.getEntityDistanceScaling().setValue(previousDistanceScaling);
    }
}
