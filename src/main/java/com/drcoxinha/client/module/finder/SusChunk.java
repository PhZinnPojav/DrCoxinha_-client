package com.drcoxinha.client.module.finder;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class SusChunk extends Module {
    public final ColorSetting color = add(new ColorSetting("Cor", 0xFF3B3B));
    public final NumberSetting fill = add(new NumberSetting("Preenchimento (%)", 20, 0, 100, 5));
    public final NumberSetting radius = add(new NumberSetting("Raio (chunks)", 8, 2, 16, 1));
    public final BoolSetting requireAllGrown = add(new BoolSetting("So se todas crescidas", true));

    public SusChunk() {
        super("Sus Chunk", "Marca chunks com geodo 100% crescido", Category.FINDER);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
