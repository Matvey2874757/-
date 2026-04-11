package net.novaclient.module.modules.render;

import net.novaclient.config.ConfigManager;
import net.novaclient.module.Module;

public class CustomCrosshair extends Module {
    public static int color;
    public static float scale;

    public CustomCrosshair() {
        super("CustomCrosshair", "Render");
        color = ConfigManager.getInt("crosshair_color", 0xFFFFFFFF);
        scale = ConfigManager.getFloat("crosshair_scale", 1.0f);
    }

    @Override
    public void onEnable() {
        color = ConfigManager.getInt("crosshair_color", 0xFFFFFFFF);
        scale = ConfigManager.getFloat("crosshair_scale", 1.0f);
    }
}
