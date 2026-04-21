package com.wecib.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<T extends Card> {

    private final List<T> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<T> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public void add(T card) {
        cards.add(card);
    }

    public T draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(0);
    }

    public T peek() {
        if (cards.isEmpty()) return null;
        return cards.get(0);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public List<T> getCards() {
        return List.copyOf(cards);
    }

    public void clear() {
        cards.clear();
    }

    @Override
    public String toString() {
        return "Deck{size=" + cards.size() + ", cards=" + cards + "}";
    }
}
