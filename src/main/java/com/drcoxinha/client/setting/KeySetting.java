package com.drcoxinha.client.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lwjgl.glfw.GLFW;

/** Tecla do GLFW. -1 = nenhuma. */
public class KeySetting extends Setting<Integer> {
    public static final int NONE = -1;

    public KeySetting(String name, int defaultKey) {
        super(name, defaultKey);
    }

    public boolean isDown(long window) {
        return this.value >= 0 && GLFW.glfwGetKey(window, this.value) == GLFW.GLFW_PRESS;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public void fromJson(JsonElement element) {
        this.value = element.getAsInt();
    }
}
