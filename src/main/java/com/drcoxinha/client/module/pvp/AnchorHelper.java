package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class AnchorHelper extends Module {
    public final NumberSetting slot = add(new NumberSetting("Slot de destino", 1, 1, 9, 1));
    public final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 0, 0, 10, 1));

    public AnchorHelper() {
        super("Anchor Helper", "Troca o item apos colocar a ancora", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
