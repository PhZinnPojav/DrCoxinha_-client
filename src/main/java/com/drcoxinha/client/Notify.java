package com.drcoxinha.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/** Mensagens curtas na barra de acao (acima da hotbar), para feedback dos modulos. */
public final class Notify {
    private Notify() {}

    public static void bar(String message) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.sendMessage(Text.literal("\u00a76DrCoxinha_ \u00a7f" + message), true);
        }
    }
}
