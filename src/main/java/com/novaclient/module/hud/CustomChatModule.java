package com.novaclient.module.hud;

import com.novaclient.module.*;

public final class CustomChatModule extends Module {
    public final NumberSetting alpha = addSetting(new NumberSetting("Alpha", 0.7, 0.1, 1.0));

    public CustomChatModule() {
        super("Custom Chat", "Цвета, прозрачность и фильтр чата", Category.HUD);
    }
}
