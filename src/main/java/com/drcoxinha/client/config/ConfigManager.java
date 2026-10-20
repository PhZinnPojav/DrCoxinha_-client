package com.drcoxinha.client.config;

import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.module.ModuleManager;
import com.drcoxinha.client.setting.Setting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ConfigManager() {}

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("drcoxinha_client.json");
    }

    public static void save() {
        try {
            JsonObject root = new JsonObject();
            for (Module m : ModuleManager.all()) {
                JsonObject mo = new JsonObject();
                mo.addProperty("enabled", m.persistsEnabled() && m.isEnabled());
                JsonObject so = new JsonObject();
                for (Setting<?> s : m.settings) {
                    so.add(s.name, s.toJson());
                }
                mo.add("settings", so);
                root.add(m.name, mo);
            }
            Files.writeString(path(), GSON.toJson(root));
        } catch (Exception e) {
            System.err.println("[DrCoxinha_ Client] Falha ao salvar config: " + e);
        }
    }

    public static void load() {
        try {
            Path p = path();
            if (!Files.exists(p)) return;
            JsonObject root = JsonParser.parseString(Files.readString(p)).getAsJsonObject();
            for (Module m : ModuleManager.all()) {
                if (!root.has(m.name)) continue;
                JsonObject mo = root.getAsJsonObject(m.name);
                if (m.persistsEnabled() && mo.has("enabled")) {
                    m.setEnabledRaw(mo.get("enabled").getAsBoolean());
                }
                if (mo.has("settings")) {
                    JsonObject so = mo.getAsJsonObject("settings");
                    for (Setting<?> s : m.settings) {
                        if (so.has(s.name)) {
                            try {
                                s.fromJson(so.get(s.name));
                            } catch (Exception ignored) {
                                // valor invalido: mantem o padrao
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[DrCoxinha_ Client] Falha ao carregar config: " + e);
        }
    }
}
