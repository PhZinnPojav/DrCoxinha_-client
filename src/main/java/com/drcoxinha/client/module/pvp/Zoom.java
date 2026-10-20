package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class Zoom extends Module {
    public final KeySetting key = add(new KeySetting("Tecla", 67)); // C
    public final NumberSetting amount = add(new NumberSetting("Nivel de zoom (x)", 4.0, 1.5, 10.0, 0.5));
    public final BoolSetting smooth = add(new BoolSetting("Suavizar", true));

    public Zoom() {
        super("Zoom", "Zoom enquanto a tecla estiver pressionada", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
