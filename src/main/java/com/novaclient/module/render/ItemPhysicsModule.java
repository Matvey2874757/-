package com.novaclient.module.render;

import com.novaclient.module.Category;
import com.novaclient.module.Module;
import com.novaclient.module.BooleanSetting;
import com.novaclient.module.NumberSetting;
import com.novaclient.util.ClientRefs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.ItemEntity;

public final class ItemPhysicsModule extends Module {
    public final BooleanSetting showCount = addSetting(new BooleanSetting("Show Count", true));
    public final NumberSetting x = addSetting(new NumberSetting("X", 8, 0, 2000));
    public final NumberSetting y = addSetting(new NumberSetting("Y", 244, 0, 2000));

    public ItemPhysicsModule() {
        super("Item Physics", "Реалистичный поворот предметов", Category.RENDER);
    }

    @Override
    public void renderHud(DrawContext context, float tickDelta) {
        if (!(ClientRefs.MC.targetedEntity instanceof ItemEntity item) || ClientRefs.MC.textRenderer == null) return;
        String suffix = showCount.get() ? (" x" + item.getStack().getCount()) : "";
        context.drawTextWithShadow(ClientRefs.MC.textRenderer,
                "Item: " + item.getStack().getName().getString() + suffix,
                x.get().intValue(), y.get().intValue(), 0xFFEED9A1);
    }
}
