package com.novaclient.module.performance;

import com.novaclient.module.Category;
import com.novaclient.module.Module;

public final class EntityCullingModule extends Module {
    public EntityCullingModule() {
        super("Entity Culling", "Пропуск рендера скрытых энтити", Category.PERFORMANCE);
    }
}
