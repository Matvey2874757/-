package net.novaclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.novaclient.NovaClient;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static final Path CONFIG_PATH = Paths.get("config", "novaclient", "config.json");
    private static JsonObject configJson;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
        } catch (IOException e) {
            NovaClient.LOGGER.error("Failed to create config directory", e);
        }

        loadConfig();
    }

    private static void loadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            NovaClient.LOGGER.warn("Config file not found, creating default");
            configJson = new JsonObject();
            saveConfig();
            return;
        }
        
        try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
            configJson = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
            NovaClient.LOGGER.warn("Could not load config, creating default", e);
            configJson = new JsonObject();
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            writer.write(gson.toJson(configJson));
        } catch (IOException e) {
            NovaClient.LOGGER.error("Could not save config", e);
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        if (configJson == null) return defaultValue;
        return configJson.has(key) ? configJson.get(key).getAsBoolean() : defaultValue;
    }

    public static void setBoolean(String key, boolean value) {
        if (configJson == null) configJson = new JsonObject();
        configJson.addProperty(key, value);
        saveConfig();
    }

    public static int getInt(String key, int defaultValue) {
        if (configJson == null) return defaultValue;
        return configJson.has(key) ? configJson.get(key).getAsInt() : defaultValue;
    }

    public static void setInt(String key, int value) {
        if (configJson == null) configJson = new JsonObject();
        configJson.addProperty(key, value);
        saveConfig();
    }

    public static double getDouble(String key, double defaultValue) {
        if (configJson == null) return defaultValue;
        return configJson.has(key) ? configJson.get(key).getAsDouble() : defaultValue;
    }

    public static void setDouble(String key, double value) {
        if (configJson == null) configJson = new JsonObject();
        configJson.addProperty(key, value);
        saveConfig();
    }

    public static String getString(String key, String defaultValue) {
        if (configJson == null) return defaultValue;
        return configJson.has(key) ? configJson.get(key).getAsString() : defaultValue;
    }

    public static void setString(String key, String value) {
        if (configJson == null) configJson = new JsonObject();
        configJson.addProperty(key, value);
        saveConfig();
    }
}
