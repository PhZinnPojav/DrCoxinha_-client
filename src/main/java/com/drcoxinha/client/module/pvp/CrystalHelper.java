package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.Notify;
import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

/**
 * Segurando cristal do fim e clicando (botao direito) num bloco que NAO e obsidiana/bedrock:
 * troca para a obsidiana da hotbar, coloca, e volta rapido para o cristal.
 * Se o bloco ja e obsidiana/bedrock, nao faz nada (o proprio jogo coloca o cristal).
 */
public class CrystalHelper extends Module {
    public final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 0, 0, 10, 1));
    public final NumberSetting returnDelay = add(new NumberSetting("Voltar ao cristal (ticks)", 1, 0, 5, 1));

    private boolean wasUse;
    private BlockHitResult pending;
    private int wait;

    private int restoreTo = -1;
    private int restoreTicks;

    public CrystalHelper() {
        super("Crystal Helper", "Troca p/ obsidiana, coloca e volta", Category.PVP);
    }

    @Override
    protected void onDisable() {
        pending = null;
    }

    @Override
    public void onTick(MinecraftClient mc) {
        // volta para o cristal depois de colocar a obsidiana
        if (restoreTo >= 0) {
            if (restoreTicks > 0) {
                restoreTicks--;
            } else {
                mc.player.getInventory().setSelectedSlot(restoreTo);
                restoreTo = -1;
            }
            return;
        }

        boolean use = mc.options.useKey.isPressed();
        boolean click = use && !wasUse;
        wasUse = use;

        if (click && mc.currentScreen == null
                && mc.player.getMainHandStack().isOf(Items.END_CRYSTAL)
                && mc.crosshairTarget instanceof BlockHitResult hit
                && hit.getType() == HitResult.Type.BLOCK) {
            BlockState st = mc.world.getBlockState(hit.getBlockPos());
            if (!st.isOf(Blocks.OBSIDIAN) && !st.isOf(Blocks.BEDROCK)) {
                pending = hit;
                wait = delay.asInt();
            }
        }

        if (pending != null) {
            if (wait > 0) {
                wait--;
                return;
            }
            BlockHitResult hit = pending;
            pending = null;
            placeObsidian(mc, hit);
        }
    }

    private void placeObsidian(MinecraftClient mc, BlockHitResult hit) {
        PlayerInventory inv = mc.player.getInventory();
        int slot = findInHotbar(inv, Items.OBSIDIAN);
        if (slot < 0) {
            Notify.bar("Crystal Helper: sem obsidiana na hotbar");
            return;
        }

        int crystalSlot = inv.getSelectedSlot();
        inv.setSelectedSlot(slot);
        ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
        if (result.isAccepted()) {
            mc.player.swingHand(Hand.MAIN_HAND);
            Notify.bar("Crystal Helper: obsidiana colocada");
        } else {
            Notify.bar("Crystal Helper: nao deu para colocar aqui");
        }

        // continua com a obsidiana selecionada por alguns ticks e entao volta ao cristal
        restoreTo = crystalSlot;
        restoreTicks = returnDelay.asInt();
    }

    private static int findInHotbar(PlayerInventory inv, Item item) {
        for (int i = 0; i < 9; i++) {
            if (inv.getStack(i).isOf(item)) return i;
        }
        return -1;
    }
}
