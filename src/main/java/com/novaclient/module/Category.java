package com.novaclient.module;

public enum Category {
    RENDER,
    HUD,
    MOVEMENT,
    COMBAT,
    PERFORMANCE;

    public String displayName() {
        return switch (this) {
            case RENDER -> "Render";
            case HUD -> "HUD";
            case MOVEMENT -> "Movement";
            case COMBAT -> "Combat";
            case PERFORMANCE -> "Performance";
        };
    }
}
