package com.novaclient.module.hud;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;

public final class CustomChatModule extends Module {
    public final NumberSetting alpha = addSetting(new NumberSetting("Alpha", 0.7, 0.1, 1.0));
    public final BooleanSetting restoreOnDisable = addSetting(new BooleanSetting("Restore On Disable", true));
    private double previousOpacity = -1;

    public CustomChatModule() {
        super("Custom Chat", "Цвета, прозрачность и фильтр чата", Category.HUD);
    }

    @Override
    protected void onEnable() {
        if (ClientRefs.MC.options == null) return;
        previousOpacity = ClientRefs.MC.options.getChatOpacity().getValue();
        ClientRefs.MC.options.getChatOpacity().setValue(alpha.get());
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.options == null) return;
        ClientRefs.MC.options.getChatOpacity().setValue(alpha.get());
    }

    @Override
    protected void onDisable() {
        if (!restoreOnDisable.get() || ClientRefs.MC.options == null || previousOpacity < 0) return;
        ClientRefs.MC.options.getChatOpacity().setValue(previousOpacity);
    }
}
