package com.drcoxinha.client.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/** Cor RGB guardada como 0xRRGGBB (sem alpha). */
public class ColorSetting extends Setting<Integer> {
    public ColorSetting(String name, int rgb) {
        super(name, rgb & 0xFFFFFF);
    }

    public int channel(int index) {
        return switch (index) {
            case 0 -> (value >> 16) & 0xFF;
            case 1 -> (value >> 8) & 0xFF;
            default -> value & 0xFF;
        };
    }

    public void setChannel(int index, int v) {
        int c = Math.max(0, Math.min(255, v));
        int r = channel(0), g = channel(1), b = channel(2);
        if (index == 0) r = c;
        else if (index == 1) g = c;
        else b = c;
        this.value = (r << 16) | (g << 8) | b;
    }

    /** Retorna a cor em ARGB com o alpha (0-255) informado. */
    public int argb(int alpha) {
        return ((alpha & 0xFF) << 24) | this.value;
    }

    public float red() { return channel(0) / 255.0f; }
    public float green() { return channel(1) / 255.0f; }
    public float blue() { return channel(2) / 255.0f; }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public void fromJson(JsonElement element) {
        this.value = element.getAsInt() & 0xFFFFFF;
    }
}
