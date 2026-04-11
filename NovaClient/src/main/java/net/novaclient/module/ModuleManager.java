package net.novaclient.module;

import net.novaclient.module.modules.hud.*;
import net.novaclient.module.modules.movement.*;
import net.novaclient.module.modules.performance.*;
import net.novaclient.module.modules.pvp.*;
import net.novaclient.module.modules.render.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        // Render Modules
        modules.add(new Fullbright());
        modules.add(new CustomCrosshair());
        modules.add(new HitColor());
        modules.add(new ItemPhysics());
        modules.add(new BlockOutline());
        modules.add(new LowDurabilityWarning());
        modules.add(new PotionStatusIcons());
        modules.add(new ArmorStatus());

        // HUD Modules
        modules.add(new Keystrokes());
        modules.add(new Coordinates());
        modules.add(new FpsPingDisplay());
        modules.add(new ComboCounter());
        modules.add(new ReachDisplay());
        modules.add(new TargetHud());
        modules.add(new CustomChat());
        modules.add(new ScreenshotManager());

        // Movement Modules
        modules.add(new Sprint());
        modules.add(new InventoryMove());
        modules.add(new BetterSneak());

        // PvP Modules
        modules.add(new AimAssist());
        modules.add(new HitDelayTimer());

        // Performance Modules
        modules.add(new EntityCulling());
        modules.add(new RenderDistanceChanger());

        // Enable modules that were saved as enabled
        modules.forEach(module -> {
            if (module.isEnabled()) {
                module.onEnable();
            }
        });
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static List<Module> getModulesByCategory(String category) {
        return modules.stream()
                .filter(m -> m.getCategory().equals(category))
                .toList();
    }

    public static void onUpdate() {
        modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
    }

    public static void onRender(float delta) {
        modules.stream().filter(Module::isEnabled).forEach(m -> m.onRender(delta));
    }
}
