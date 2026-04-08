package com.novaclient.config;

import com.google.gson.*;
import com.novaclient.module.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path dir;
    private final Path file;

    public ConfigManager(Path configRoot) {
        this.dir = configRoot.resolve("novaclient");
        this.file = dir.resolve("modules.json");
    }

    public void save(ModuleManager manager) {
        try {
            Files.createDirectories(dir);
            JsonObject root = new JsonObject();
            for (Module module : manager.getModules()) {
                JsonObject modJson = new JsonObject();
                modJson.addProperty("enabled", module.isEnabled());
                JsonObject settings = new JsonObject();
                for (Setting<?> setting : module.getSettings()) {
                    Object val = setting.get();
                    if (val instanceof Number n) {
                        settings.addProperty(setting.getName(), n);
                    } else if (val instanceof Boolean b) {
                        settings.addProperty(setting.getName(), b);
                    } else {
                        settings.addProperty(setting.getName(), String.valueOf(val));
                    }
                }
                modJson.add("settings", settings);
                root.add(module.getName(), modJson);
            }
            Files.writeString(file, GSON.toJson(root));
        } catch (IOException ignored) {
        }
    }

    public void load(ModuleManager manager) {
        if (!Files.exists(file)) return;
        try {
            JsonObject root = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            for (Module module : manager.getModules()) {
                if (!root.has(module.getName())) continue;
                JsonObject modJson = root.getAsJsonObject(module.getName());
                if (modJson.has("enabled")) module.setEnabled(modJson.get("enabled").getAsBoolean());
                JsonObject settings = modJson.has("settings") ? modJson.getAsJsonObject("settings") : new JsonObject();
                for (Setting<?> setting : module.getSettings()) {
                    if (!settings.has(setting.getName())) continue;
                    JsonElement e = settings.get(setting.getName());
                    if (setting instanceof NumberSetting ns) {
                        ns.set(e.getAsDouble());
                    } else if (setting instanceof BooleanSetting bs) {
                        bs.set(e.getAsBoolean());
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
