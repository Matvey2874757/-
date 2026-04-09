package com.novaclient.module.movement;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public final class InventoryMoveModule extends Module {
    public InventoryMoveModule() {
        super("Inventory Move", "Движение при открытом инвентаре", Category.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ClientRefs.MC.player == null || ClientRefs.MC.currentScreen == null || ClientRefs.MC.options == null) return;
        if (ClientRefs.MC.currentScreen instanceof net.minecraft.client.gui.screen.ChatScreen) return;
        long handle = ClientRefs.MC.getWindow().getHandle();
        syncKey(ClientRefs.MC.options.forwardKey, handle);
        syncKey(ClientRefs.MC.options.backKey, handle);
        syncKey(ClientRefs.MC.options.leftKey, handle);
        syncKey(ClientRefs.MC.options.rightKey, handle);
        syncKey(ClientRefs.MC.options.jumpKey, handle);
        syncKey(ClientRefs.MC.options.sprintKey, handle);
        syncKey(ClientRefs.MC.options.sneakKey, handle);
    }

    private void syncKey(KeyBinding binding, long handle) {
        InputUtil.Key key = binding.getBoundKey();
        int code = key.getCode();
        if (code <= 0) return;
        binding.setPressed(InputUtil.isKeyPressed(handle, code));
    }
}
