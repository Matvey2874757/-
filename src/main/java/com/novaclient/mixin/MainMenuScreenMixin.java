package com.novaclient.mixin;

import com.novaclient.gui.screen.CustomMainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MainMenuScreenMixin extends Screen {
    protected MainMenuScreenMixin(net.minecraft.text.Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void novaclient$replace(CallbackInfo ci) {
        if (client != null && !(client.currentScreen instanceof CustomMainMenuScreen)) {
            client.setScreen(new CustomMainMenuScreen(null));
            ci.cancel();
        }
    }
}
