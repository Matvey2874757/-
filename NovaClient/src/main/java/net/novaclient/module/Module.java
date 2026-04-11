package net.novaclient.module;

import net.novaclient.config.ConfigManager;

public abstract class Module {
    protected final String name;
    protected final String category;
    protected boolean enabled;

    public Module(String name, String category) {
        this.name = name;
        this.category = category;
        this.enabled = ConfigManager.getBoolean(name, false);
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
            ConfigManager.setBoolean(name, enabled);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void onRender(float delta) {}
}
