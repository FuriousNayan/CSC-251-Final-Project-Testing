package com.wecib.model;

public enum CardType {
    FIRE,
    WATER,
    EARTH,
    ELECTRIC,
    WIND,
    SHADOW,
    LIGHT,
    AURA,
    DIRT,
    STEEL;

    public String displayName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
