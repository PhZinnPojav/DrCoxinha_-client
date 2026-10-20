package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class FreeLook extends Module {
    public final KeySetting key = add(new KeySetting("Tecla", 342)); // Alt esquerdo
    public final BoolSetting noLimit = add(new BoolSetting("Sem limite (atravessa blocos)", false));
    public final NumberSetting distance = add(new NumberSetting("Distancia da camera", 4.0, 1.0, 20.0, 0.5));

    public FreeLook() {
        super("FreeLook", "Gira so a camera, nao o personagem", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
