package com.wecib.engine;

import com.wecib.model.Card;
import com.wecib.model.Deck;

import java.util.ArrayList;
import java.util.List;

public class PlayerState {

    private final String name;
    private final Deck<Card> deck;
    private Card activeCard;
    private final List<Card> bench;
    private int energy;
    private int wins;

    public PlayerState(String name) {
        this.name = name;
        this.deck = new Deck<>();
        this.bench = new ArrayList<>();
        this.energy = 0;
        this.wins = 0;
    }

    public void addCardToDeck(Card card) {
        deck.add(card.copy());
    }

    public void setupForBattle() {
        bench.clear();
        activeCard = null;
        energy = 1;

        List<Card> allCards = deck.getCards();
        if (!allCards.isEmpty()) {
            activeCard = allCards.get(0).copy();
            for (int i = 1; i < allCards.size(); i++) {
                bench.add(allCards.get(i).copy());
            }
        }
    }

    public void gainEnergy() {
        energy = Math.min(energy + 2, 5);
    }

    public boolean spendEnergy(int cost) {
        if (energy < cost) return false;
        energy -= cost;
        return true;
    }

    public boolean switchActiveCard(int benchIndex) {
        if (benchIndex < 0 || benchIndex >= bench.size()) return false;
        Card swapIn = bench.remove(benchIndex);
        if (activeCard != null && activeCard.isAlive()) {
            bench.add(activeCard);
        }
        activeCard = swapIn;
        return true;
    }

    public boolean promoteFromBench() {
        if (bench.isEmpty()) return false;
        activeCard = bench.remove(0);
        return true;
    }

    public boolean hasCardsLeft() {
        return (activeCard != null && activeCard.isAlive()) || !bench.isEmpty();
    }

    public String getName() { return name; }
    public Deck<Card> getDeck() { return deck; }
    public Card getActiveCard() { return activeCard; }
    public List<Card> getBench() { return List.copyOf(bench); }
    public int getEnergy() { return energy; }
    public int getWins() { return wins; }

    public void recordWin() { wins++; }

    public void reset() {
        deck.clear();
        bench.clear();
        activeCard = null;
        energy = 0;
    }
}
