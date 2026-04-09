package com.novaclient.module.performance;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;

public final class DynamicViewDistanceModule extends Module {
    public final NumberSetting distance = addSetting(new NumberSetting("Render Distance", 12, 2, 32));

    public DynamicViewDistanceModule() {
        super("Dynamic View Distance", "Настройка чанков без перезапуска", Category.PERFORMANCE);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.options != null) {
            int target = distance.get().intValue();
            if (ClientRefs.MC.options.getViewDistance().getValue() != target) {
                ClientRefs.MC.options.getViewDistance().setValue(target);
            }
        }
    }
}
