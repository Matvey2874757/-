package com.novaclient.module;

public enum Category {
    RENDER,
    HUD,
    HUD_LAYOUT,
    MOVEMENT,
    COMBAT,
    PERFORMANCE;

    public String displayName() {
        return switch (this) {
            case RENDER -> "Render";
            case HUD -> "HUD";
            case HUD_LAYOUT -> "HUD Layout";
            case MOVEMENT -> "Movement";
            case COMBAT -> "Combat";
            case PERFORMANCE -> "Performance";
        };
    }
}
