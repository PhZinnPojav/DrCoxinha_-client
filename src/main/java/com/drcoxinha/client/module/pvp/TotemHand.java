package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.Notify;
import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ao abrir o inventario sem totem na mao esquerda, escolhe um totem ALEATORIO do inventario
 * e o coloca na mao secundaria. Roda uma vez por abertura do inventario.
 */
public class TotemHand extends Module {
    public final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 0, 0, 10, 1));

    private final Random random = new Random();
    private boolean done;
    private int wait = -1;

    public TotemHand() {
        super("Totem Hand", "Repoe o totem na mao esquerda", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        if (!(mc.currentScreen instanceof InventoryScreen)) {
            done = false;
            wait = -1;
            return;
        }
        if (done) return;

        if (mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            done = true;
            return;
        }

        if (wait < 0) wait = delay.asInt();
        if (wait > 0) {
            wait--;
            return;
        }

        done = true;
        PlayerScreenHandler handler = mc.player.playerScreenHandler;
        List<Integer> slots = new ArrayList<>();
        // 9-35 = inventario, 36-44 = hotbar
        for (int i = 9; i <= 44; i++) {
            if (handler.getSlot(i).getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                slots.add(i);
            }
        }
        if (slots.isEmpty()) {
            Notify.bar("Totem Hand: nenhum totem no inventario");
            return;
        }

        int chosen = slots.get(random.nextInt(slots.size()));
        // SWAP com botao 40 = troca com a mao secundaria
        mc.interactionManager.clickSlot(handler.syncId, chosen, 40, SlotActionType.SWAP, mc.player);
        Notify.bar("Totem Hand: totem colocado na mao esquerda");
    }
}
