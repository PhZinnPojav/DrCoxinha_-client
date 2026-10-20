package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class Esp extends Module {
    public final ColorSetting color = add(new ColorSetting("Cor", 0x00FF00));
    public final NumberSetting fill = add(new NumberSetting("Preenchimento (%)", 25, 0, 100, 5));
    public final NumberSetting lineWidth = add(new NumberSetting("Espessura da linha", 2, 1, 5, 0.5));

    public Esp() {
        super("ESP", "Jogadores em verde atraves das paredes", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
