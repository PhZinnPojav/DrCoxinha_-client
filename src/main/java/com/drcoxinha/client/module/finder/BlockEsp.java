package com.drcoxinha.client.module.finder;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class BlockEsp extends Module {
    public final ColorSetting color = add(new ColorSetting("Cor", 0x00BFFF));
    public final NumberSetting fill = add(new NumberSetting("Preenchimento (%)", 25, 0, 100, 5));
    public final NumberSetting range = add(new NumberSetting("Alcance (blocos)", 64, 16, 128, 8));
    // A lista de blocos escolhidos entra na Etapa 3.

    public BlockEsp() {
        super("Block ESP", "Destaca os blocos escolhidos", Category.FINDER);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 2/3: logica do modulo
    }
}
