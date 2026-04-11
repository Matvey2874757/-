package net.novaclient.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.novaclient.ui.screens.MainMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TitleScreen.class)
public class MainMenuScreenMixin {

    @ModifyVariable(method = "init", at = @At("HEAD"), argsOnly = true)
    public Screen modifyMainMenu(Screen screen) {
        // Replace the title screen with our custom main menu when appropriate
        return screen;
    }
}
