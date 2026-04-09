package com.novaclient.module.render;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;

public final class FullbrightModule extends Module {
    public final NumberSetting gamma = addSetting(new NumberSetting("Gamma", 16.0, 1.0, 32.0));
    private double previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Повышает гамму для яркой картинки", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        if (ClientRefs.MC.options == null) return;
        previousGamma = ClientRefs.MC.options.getGamma().getValue();
        ClientRefs.MC.options.getGamma().setValue(gamma.get());
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.options == null) return;
        if (Math.abs(ClientRefs.MC.options.getGamma().getValue() - gamma.get()) > 1e-4) {
            ClientRefs.MC.options.getGamma().setValue(gamma.get());
        }
    }

    @Override
    protected void onDisable() {
        if (ClientRefs.MC.options == null) return;
        ClientRefs.MC.options.getGamma().setValue(previousGamma);
    }
}
