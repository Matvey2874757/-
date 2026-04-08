package com.novaclient.module.render;

import com.novaclient.module.*;

public final class BlockOutlineModule extends Module {
    public final NumberSetting width = addSetting(new NumberSetting("Line Width", 2, 1, 6));

    public BlockOutlineModule() {
        super("Block Outline", "Настраиваемая обводка блока", Category.RENDER);
    }
}
