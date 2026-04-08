package com.novaclient.module;

import com.novaclient.module.combat.AimAssistModule;
import com.novaclient.module.combat.HitDelayTimerModule;
import com.novaclient.module.hud.*;
import com.novaclient.module.movement.*;
import com.novaclient.module.performance.*;
import com.novaclient.module.render.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void registerDefaults() {
        modules.clear();
        modules.add(new FullbrightModule());
        modules.add(new CustomCrosshairModule());
        modules.add(new HitColorModule());
        modules.add(new ItemPhysicsModule());
        modules.add(new BlockOutlineModule());
        modules.add(new LowDurabilityWarningModule());
        modules.add(new PotionStatusIconsModule());
        modules.add(new ArmorStatusModule());

        modules.add(new KeystrokesModule());
        modules.add(new CoordinatesModule());
        modules.add(new FpsPingModule());
        modules.add(new ComboCounterModule());
        modules.add(new ReachDisplayModule());
        modules.add(new TargetHudModule());
        modules.add(new CustomChatModule());
        modules.add(new ScreenshotManagerModule());

        modules.add(new SprintModule());
        modules.add(new InventoryMoveModule());
        modules.add(new NoSlowdownModule());
        modules.add(new BetterSneakModule());

        modules.add(new AimAssistModule());
        modules.add(new HitDelayTimerModule());

        modules.add(new EntityCullingModule());
        modules.add(new ParticleLimitModule());
        modules.add(new DynamicViewDistanceModule());
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public Module getByName(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void tickModules() {
        modules.stream().filter(Module::isEnabled).forEach(Module::tick);
    }
}
