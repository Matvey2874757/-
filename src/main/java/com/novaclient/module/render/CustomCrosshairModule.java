package com.novaclient.module.render;

import com.novaclient.module.*;

public final class CustomCrosshairModule extends Module {
    public final NumberSetting size = addSetting(new NumberSetting("Size", 6, 2, 20));
    public final NumberSetting red = addSetting(new NumberSetting("Red", 255, 0, 255));
    public final NumberSetting green = addSetting(new NumberSetting("Green", 255, 0, 255));
    public final NumberSetting blue = addSetting(new NumberSetting("Blue", 255, 0, 255));
    public final BooleanSetting animated = addSetting(new BooleanSetting("Animated", true));

    public CustomCrosshairModule() {
        super("Custom Crosshair", "Кастомный прицел", Category.RENDER);
    }
}
