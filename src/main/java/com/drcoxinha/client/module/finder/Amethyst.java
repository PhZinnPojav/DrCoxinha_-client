package com.drcoxinha.client.module.finder;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class Amethyst extends Module {
    public final ColorSetting color = add(new ColorSetting("Cor", 0xE040FB));
    public final NumberSetting fill = add(new NumberSetting("Preenchimento (%)", 30, 0, 100, 5));
    public final NumberSetting range = add(new NumberSetting("Alcance (blocos)", 64, 16, 128, 8));

    public Amethyst() {
        super("Amethyst", "Destaca todas as Amethyst Clusters", Category.FINDER);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
