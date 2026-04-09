package com.novaclient;

import com.novaclient.config.ConfigManager;
import com.novaclient.gui.screen.ModuleScreen;
import com.novaclient.module.Module;
import com.novaclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class NovaClient implements ClientModInitializer {
    public static final String MOD_ID = "novaclient";
    public static final ModuleManager MODULE_MANAGER = new ModuleManager();
    public static ConfigManager CONFIG;
    public static KeyBinding OPEN_MODULES;
    public static KeyBinding PANIC_DISABLE_ALL;

    @Override
    public void onInitializeClient() {
        MODULE_MANAGER.registerDefaults();
        CONFIG = new ConfigManager(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir());
        CONFIG.load(MODULE_MANAGER);

        OPEN_MODULES = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.novaclient.modules",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.novaclient"
        ));
        PANIC_DISABLE_ALL = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.novaclient.panic",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DELETE,
                "category.novaclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MODULE_MANAGER.tickModules();
            while (OPEN_MODULES.wasPressed()) {
                client.setScreen(new ModuleScreen(client.currentScreen));
            }
            while (PANIC_DISABLE_ALL.wasPressed()) {
                MODULE_MANAGER.disableAll();
                CONFIG.save(MODULE_MANAGER);
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            for (Module module : MODULE_MANAGER.getModules()) {
                if (module.isEnabled()) {
                    module.renderHud(drawContext, tickDelta);
                }
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> CONFIG.save(MODULE_MANAGER));
    }
}
