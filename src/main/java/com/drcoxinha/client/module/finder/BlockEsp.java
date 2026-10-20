package com.drcoxinha.client.module.finder;

import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;

public class BlockEsp extends Module {
    public final BoolSetting chests = add(new BoolSetting("Baus", true));
    public final ColorSetting chestColor = add(new ColorSetting("Cor: Baus", 0xFFA500));
    public final BoolSetting shulkers = add(new BoolSetting("Shulkers", true));
    public final ColorSetting shulkerColor = add(new ColorSetting("Cor: Shulkers", 0xE040FB));
    public final BoolSetting spawners = add(new BoolSetting("Spawners", true));
    public final ColorSetting spawnerColor = add(new ColorSetting("Cor: Spawners", 0xFF3B3B));
    public final BoolSetting barrels = add(new BoolSetting("Barris", true));
    public final ColorSetting barrelColor = add(new ColorSetting("Cor: Barris", 0x00BFFF));
    public final NumberSetting fill = add(new NumberSetting("Preenchimento (%)", 25, 0, 100, 5));
    public final NumberSetting range = add(new NumberSetting("Alcance (blocos)", 64, 16, 128, 8));

    public BlockEsp() {
        super("Block ESP", "Destaca baus, shulkers, spawners e barris", Category.FINDER);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // TODO Etapa 3: logica do modulo
    }
}
