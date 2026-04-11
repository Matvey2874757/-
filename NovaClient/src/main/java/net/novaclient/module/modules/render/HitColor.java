package net.novaclient.module.modules.render;

import net.novaclient.config.ConfigManager;
import net.novaclient.module.Module;

public class HitColor extends Module {
    public static int hitColor;

    public HitColor() {
        super("HitColor", "Render");
        hitColor = ConfigManager.getInt("hit_color", 0xFFFF0000);
    }

    @Override
    public void onEnable() {
        hitColor = ConfigManager.getInt("hit_color", 0xFFFF0000);
    }
}
