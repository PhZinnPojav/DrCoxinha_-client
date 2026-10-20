package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class Freecam extends Module {
    public final NumberSetting speed = add(new NumberSetting("Velocidade", 1.0, 0.1, 5.0, 0.1));
    public final KeySetting toggleKey = add(new KeySetting("Tecla de ligar", KeySetting.NONE));

    @Override
    public boolean persistsEnabled() {
        return false;
    }

    public Freecam() {
        super("Freecam", "Camera livre (so no client)", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
