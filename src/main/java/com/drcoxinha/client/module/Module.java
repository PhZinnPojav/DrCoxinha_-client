package com.drcoxinha.client.module;

import com.drcoxinha.client.Notify;
import com.drcoxinha.client.setting.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    public final String name;
    public final String description;
    public final Category category;
    public final List<Setting<?>> settings = new ArrayList<>();

    private boolean enabled;

    protected Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    protected <S extends Setting<?>> S add(S setting) {
        settings.add(setting);
        return setting;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        if (this.enabled == value) return;
        this.enabled = value;
        if (value) onEnable(); else onDisable();
        Notify.bar(name + ": " + (value ? "\u00a7aON" : "\u00a7cOFF"));
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    /** Usado so ao carregar a config (nao dispara onEnable). */
    public void setEnabledRaw(boolean value) {
        this.enabled = value;
    }

    /** Modulos como Freecam nao devem voltar ligados ao abrir o jogo. */
    public boolean persistsEnabled() {
        return true;
    }

    protected void onEnable() {}

    protected void onDisable() {}

    /** Chamado todo tick de cliente enquanto o modulo estiver ligado e o jogador no mundo. */
    public void onTick(MinecraftClient mc) {}
}
