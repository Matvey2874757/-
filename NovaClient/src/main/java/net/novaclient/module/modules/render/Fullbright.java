package net.novaclient.module.modules.render;

import net.minecraft.client.MinecraftClient;
import net.novaclient.config.ConfigManager;
import net.novaclient.module.Module;

public class Fullbright extends Module {
    private float previousGamma;

    public Fullbright() {
        super("Fullbright", "Render");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            previousGamma = client.options.getGamma().floatValue();
            client.options.getGamma().setValue(16.0);
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            client.options.getGamma().setValue(previousGamma);
        }
    }
}
