package com.novaclient.module.render;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.util.ClientRefs;

public final class FullbrightModule extends Module {
    private double previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Повышает гамму для яркой картинки", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        if (ClientRefs.MC.options == null) return;
        previousGamma = ClientRefs.MC.options.getGamma().getValue();
        ClientRefs.MC.options.getGamma().setValue(16.0);
    }

    @Override
    protected void onDisable() {
        if (ClientRefs.MC.options == null) return;
        ClientRefs.MC.options.getGamma().setValue(previousGamma);
    }
}
