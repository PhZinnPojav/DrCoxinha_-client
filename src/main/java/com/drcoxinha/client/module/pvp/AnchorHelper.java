package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.Notify;
import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

/** Depois de colocar uma ancora do renascimento, muda a hotbar para o slot configurado. */
public class AnchorHelper extends Module {
    public final NumberSetting slot = add(new NumberSetting("Slot de destino", 1, 1, 9, 1));
    public final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 0, 0, 10, 1));

    private boolean wasUse;
    private BlockPos watch;
    private int ticksLeft;
    private int delayLeft = -1;

    public AnchorHelper() {
        super("Anchor Helper", "Troca o item apos colocar a ancora", Category.PVP);
    }

    @Override
    protected void onDisable() {
        watch = null;
    }

    @Override
    public void onTick(MinecraftClient mc) {
        boolean use = mc.options.useKey.isPressed();
        boolean click = use && !wasUse;
        wasUse = use;

        if (click && mc.currentScreen == null
                && mc.player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR)
                && mc.crosshairTarget instanceof BlockHitResult hit
                && hit.getType() == HitResult.Type.BLOCK) {
            watch = hit.getBlockPos().offset(hit.getSide());
            ticksLeft = 6;
            delayLeft = -1;
        }

        if (watch == null) return;

        if (mc.world.getBlockState(watch).isOf(Blocks.RESPAWN_ANCHOR)) {
            if (delayLeft < 0) delayLeft = delay.asInt();
            if (delayLeft > 0) {
                delayLeft--;
                return;
            }
            mc.player.getInventory().setSelectedSlot(slot.asInt() - 1);
            Notify.bar("Anchor Helper: slot " + slot.asInt() + " selecionado");
            watch = null;
        } else if (--ticksLeft <= 0) {
            watch = null;
        }
    }
}
