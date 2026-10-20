package com.drcoxinha.client.module;

import com.drcoxinha.client.module.finder.Amethyst;
import com.drcoxinha.client.module.finder.BlockEsp;
import com.drcoxinha.client.module.finder.SusChunk;
import com.drcoxinha.client.module.pvp.*;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();

    private ModuleManager() {}

    public static void init() {
        // PvP
        MODULES.add(new CrystalHelper());
        MODULES.add(new CrystalPop());
        MODULES.add(new TotemHand());
        MODULES.add(new AnchorHelper());
        MODULES.add(new Esp());
        MODULES.add(new Freecam());
        MODULES.add(new Zoom());
        MODULES.add(new FreeLook());
        // Finder
        MODULES.add(new BlockEsp());
        MODULES.add(new Amethyst());
        MODULES.add(new SusChunk());
    }

    public static List<Module> all() {
        return MODULES;
    }

    public static List<Module> byCategory(Category category) {
        List<Module> out = new ArrayList<>();
        for (Module m : MODULES) {
            if (m.category == category) out.add(m);
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T get(Class<T> type) {
        for (Module m : MODULES) {
            if (m.getClass() == type) return (T) m;
        }
        return null;
    }

    public static void tick(MinecraftClient mc) {
        for (Module m : MODULES) {
            if (m.isEnabled()) m.onTick(mc);
        }
    }
}
