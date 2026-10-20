package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.Notify;
import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

/**
 * 1) Depois de colocar uma ancora do renascimento, muda a hotbar para o "Slot de destino".
 * 2) Ao clicar com o direito numa ancora carregada (nivel 1+), muda para o "Slot ao explodir"
 *    e interage de novo com ela, explodindo-a.
 */
public class AnchorHelper extends Module {
    public final NumberSetting slot = add(new NumberSetting("Slot de destino", 1, 1, 9, 1));
    public final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 0, 0, 10, 1));
    public final BoolSetting explodeCharged = add(new BoolSetting("Explodir ancora carregada", true));
    public final NumberSetting explodeSlot = add(new NumberSetting("Slot ao explodir", 5, 1, 9, 1));

    private boolean wasUse;
    private BlockPos watch;
    private int ticksLeft;
    private int delayLeft = -1;

    public AnchorHelper() {
        super("Anchor Helper", "Troca de slot e explode a ancora", Category.PVP);
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

        // 2) clique numa ancora carregada: muda de slot e explode
        if (click && explodeCharged.get() && mc.currentScreen == null
                && mc.crosshairTarget instanceof BlockHitResult target
                && target.getType() == HitResult.Type.BLOCK) {
            BlockState st = mc.world.getBlockState(target.getBlockPos());
            if (st.isOf(Blocks.RESPAWN_ANCHOR) && st.get(RespawnAnchorBlock.CHARGES) >= 1) {
                PlayerInventory inv = mc.player.getInventory();
                int slotIndex = explodeSlot.asInt() - 1;
                // se ja esta no slot, o proprio clique do jogo ja explodiu a ancora
                if (inv.getSelectedSlot() != slotIndex) {
                    inv.setSelectedSlot(slotIndex);
                    ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, target);
                    if (result.isAccepted()) {
                        mc.player.swingHand(Hand.MAIN_HAND);
                    }
                    Notify.bar("Anchor Helper: slot " + explodeSlot.asInt() + " + explosao");
                }
                watch = null;
                return;
            }
        }

        // 1) apos colocar uma ancora, muda para o slot de destino
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
