package com.drcoxinha.client.module.pvp;

import com.drcoxinha.client.Notify;
import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

/** Botao direito em cima de um cristal do fim = bate nele (explode). */
public class CrystalPop extends Module {
    public final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 0, 0, 10, 1));

    private int cooldown;

    public CrystalPop() {
        super("Crystal Pop", "Explode cristais com o botao direito", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (mc.currentScreen != null || !mc.options.useKey.isPressed()) return;

        if (mc.crosshairTarget instanceof EntityHitResult hit
                && hit.getEntity() instanceof EndCrystalEntity crystal) {
            mc.interactionManager.attackEntity(mc.player, crystal);
            mc.player.swingHand(Hand.MAIN_HAND);
            cooldown = delay.asInt();
            Notify.bar("Crystal Pop: cristal atingido");
        }
    }
}
