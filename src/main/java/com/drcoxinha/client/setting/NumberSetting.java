package com.drcoxinha.client.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Locale;

public class NumberSetting extends Setting<Double> {
    public final double min;
    public final double max;
    public final double step;

    public NumberSetting(String name, double defaultValue, double min, double max, double step) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void set(Double v) {
        double c = Math.max(min, Math.min(max, v));
        if (step > 0) {
            c = Math.round((c - min) / step) * step + min;
            c = Math.max(min, Math.min(max, c));
        }
        this.value = c;
    }

    public void setFromFraction(double fraction) {
        double f = Math.max(0.0, Math.min(1.0, fraction));
        set(min + (max - min) * f);
    }

    public double asDouble() {
        return this.value;
    }

    public float asFloat() {
        return (float) (double) this.value;
    }

    public int asInt() {
        return (int) Math.round(this.value);
    }

    public String display() {
        if (step >= 1.0) {
            return String.valueOf(asInt());
        }
        return String.format(Locale.ROOT, "%.1f", this.value);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public void fromJson(JsonElement element) {
        set(element.getAsDouble());
    }
}
