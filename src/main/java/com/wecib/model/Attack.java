package com.wecib.model;

public class Attack {

    private final String name;
    private final int damage;
    private final int energyCost;
    private final CardType type;

    public Attack(String name, int damage, int energyCost, CardType type) {
        this.name = name;
        this.damage = damage;
        this.energyCost = energyCost;
        this.type = type;
    }

    public String getName() { return name; }
    public int getDamage() { return damage; }
    public int getEnergyCost() { return energyCost; }
    public CardType getType() { return type; }

    @Override
    public String toString() {
        return name + " [" + damage + " dmg, " + energyCost + " energy, " + type.displayName() + "]";
    }
}
