package com.drcoxinha.client.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ModeSetting extends Setting<String> {
    public final String[] modes;

    public ModeSetting(String name, String defaultMode, String... modes) {
        super(name, defaultMode);
        this.modes = modes;
    }

    public void next() {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(value)) {
                this.value = modes[(i + 1) % modes.length];
                return;
            }
        }
        this.value = modes[0];
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public void fromJson(JsonElement element) {
        String s = element.getAsString();
        for (String m : modes) {
            if (m.equals(s)) {
                this.value = s;
                return;
            }
        }
    }
}
