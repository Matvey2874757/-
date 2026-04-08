package com.novaclient.module.render;

import com.novaclient.module.*;

public final class HitColorModule extends Module {
    public final NumberSetting red = addSetting(new NumberSetting("Red", 255, 0, 255));
    public final NumberSetting green = addSetting(new NumberSetting("Green", 80, 0, 255));
    public final NumberSetting blue = addSetting(new NumberSetting("Blue", 80, 0, 255));

    public HitColorModule() {
        super("Hit Color", "Цветной визуальный эффект удара", Category.RENDER);
    }
}
