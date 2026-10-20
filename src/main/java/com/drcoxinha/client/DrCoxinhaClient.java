package com.drcoxinha.client;

import com.drcoxinha.client.config.ConfigManager;
import com.drcoxinha.client.gui.ClickGuiScreen;
import com.drcoxinha.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class DrCoxinhaClient implements ClientModInitializer {
    public static final String NAME = "DrCoxinha_ Client";

    private static boolean rightShiftWasDown;

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        ConfigManager.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long window = client.getWindow().getHandle();

            // Shift Direito abre/fecha o menu (so com a tela livre ou com o proprio menu aberto)
            boolean down = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (down && !rightShiftWasDown) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                } else if (client.currentScreen instanceof ClickGuiScreen) {
                    client.currentScreen.close();
                }
            }
            rightShiftWasDown = down;

            if (client.player != null && client.world != null) {
                ModuleManager.tick(client);
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ConfigManager.save());
    }
}
