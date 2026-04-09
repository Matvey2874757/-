package com.novaclient.module.render;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;

public final class PotionStatusIconsModule extends Module {
    public final NumberSetting x = addSetting(new NumberSetting("X", 8, 0, 2000));
    public final NumberSetting y = addSetting(new NumberSetting("Y", 212, 0, 2000));
    public final NumberSetting maxLines = addSetting(new NumberSetting("Max Lines", 5, 1, 12));

    public PotionStatusIconsModule() {
        super("Potion Status Icons", "Иконки эффектов зелий", Category.RENDER);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (ClientRefs.MC.player == null || ClientRefs.MC.textRenderer == null) return;
        int x = this.x.get().intValue();
        int y = this.y.get().intValue();
        int line = 0;

        for (StatusEffectInstance effect : ClientRefs.MC.player.getStatusEffects()) {
            String name = effect.getEffectType().value().getName().getString();
            int seconds = Math.max(0, effect.getDuration() / 20);
            context.drawTextWithShadow(ClientRefs.MC.textRenderer,
                    name + " " + (effect.getAmplifier() + 1) + " (" + seconds + "s)",
                    x, y + line * 10, 0xFFBEE3FF);
            line++;
            if (line >= maxLines.get().intValue()) break;
        }
    }
}
