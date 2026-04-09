package com.novaclient.module.movement;

import com.novaclient.module.*;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.util.InputUtil;

public final class BetterSneakModule extends Module {
    public final BooleanSetting toggle = addSetting(new BooleanSetting("Toggle", true));
    public final BooleanSetting resetInScreens = addSetting(new BooleanSetting("Reset In Screens", true));
    private boolean toggledSneak;
    private boolean previousPressed;

    public BetterSneakModule() {
        super("Better Sneak", "Toggle-sneak без изменения высоты глаз", Category.MOVEMENT);
    }

    @Override
    protected void onDisable() {
        toggledSneak = false;
        previousPressed = false;
        if (ClientRefs.MC.options != null) {
            ClientRefs.MC.options.sneakKey.setPressed(false);
        }
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null || ClientRefs.MC.options == null) return;
        if (resetInScreens.get() && ClientRefs.MC.currentScreen != null) {
            toggledSneak = false;
            ClientRefs.MC.options.sneakKey.setPressed(false);
            return;
        }
        long handle = ClientRefs.MC.getWindow().getHandle();
        InputUtil.Key key = ClientRefs.MC.options.sneakKey.getBoundKey();
        boolean pressed = key.getCode() > 0 && InputUtil.isKeyPressed(handle, key.getCode());

        if (toggle.get()) {
            if (pressed && !previousPressed) {
                toggledSneak = !toggledSneak;
            }
            ClientRefs.MC.options.sneakKey.setPressed(toggledSneak);
        } else {
            ClientRefs.MC.options.sneakKey.setPressed(pressed);
        }
        previousPressed = pressed;
    }
}
