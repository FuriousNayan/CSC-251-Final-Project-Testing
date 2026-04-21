package com.wecib.model;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private final String name;
    private final int maxHp;
    private int currentHp;
    private final CardType type;
    private final List<Attack> attacks;
    private final String imagePath;

    private Card(Builder builder) {
        this.name = builder.name;
        this.maxHp = builder.maxHp;
        this.currentHp = builder.maxHp;
        this.type = builder.type;
        this.attacks = List.copyOf(builder.attacks);
        this.imagePath = builder.imagePath;
    }

    public Card copy() {
        Builder b = new Builder(name, type, maxHp);
        for (Attack a : attacks) b.attack(a);
        if (imagePath != null) b.image(imagePath);
        return b.build();
    }

    public String getName() { return name; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentHp() { return currentHp; }
    public CardType getType() { return type; }
    public List<Attack> getAttacks() { return attacks; }
    public String getImagePath() { return imagePath; }

    public boolean isAlive() { return currentHp > 0; }

    public void takeDamage(int amount) {
        currentHp = Math.max(0, currentHp - amount);
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public void resetHp() {
        currentHp = maxHp;
    }

    public double hpPercent() {
        return (double) currentHp / maxHp;
    }

    @Override
    public String toString() {
        return name + " [" + type.displayName() + "] HP: " + currentHp + "/" + maxHp;
    }

    public static class Builder {
        private final String name;
        private final CardType type;
        private final int maxHp;
        private final List<Attack> attacks = new ArrayList<>();
        private String imagePath;

        public Builder(String name, CardType type, int maxHp) {
            this.name = name;
            this.type = type;
            this.maxHp = maxHp;
        }

        public Builder attack(Attack attack) {
            this.attacks.add(attack);
            return this;
        }

        public Builder image(String path) {
            this.imagePath = path;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
