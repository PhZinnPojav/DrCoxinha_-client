package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class TotemHand extends Module {
    public final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 0, 0, 10, 1));

    public TotemHand() {
        super("Totem Hand", "Repoe o totem na mao esquerda", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
