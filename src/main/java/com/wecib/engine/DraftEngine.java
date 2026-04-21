package com.wecib.engine;

import com.wecib.model.Card;
import com.wecib.model.CardRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DraftEngine {

    private static final int DECK_SIZE = 5;
    private static final int CHOICES_PER_ROUND = 2;

    private final List<Card> pool;
    private final PlayerState player;
    private final PlayerState opponent;
    private List<Card> currentChoices;
    private int round;

    public DraftEngine(PlayerState player, PlayerState opponent) {
        this.player = player;
        this.opponent = opponent;
        this.pool = new ArrayList<>(CardRegistry.getAllCards());
        Collections.shuffle(this.pool);
        this.round = 0;
        this.currentChoices = new ArrayList<>();
    }

    public List<Card> nextChoices() {
        currentChoices.clear();
        int available = Math.min(CHOICES_PER_ROUND, pool.size());
        for (int i = 0; i < available; i++) {
            currentChoices.add(pool.remove(0));
        }
        round++;
        return List.copyOf(currentChoices);
    }

    public void playerPicks(int choiceIndex) {
        if (choiceIndex < 0 || choiceIndex >= currentChoices.size()) return;

        Card picked = currentChoices.remove(choiceIndex);
        player.addCardToDeck(picked);

        if (!currentChoices.isEmpty()) {
            Card opponentPick = currentChoices.get(0);
            opponent.addCardToDeck(opponentPick);
        }
    }

    public boolean isDraftComplete() {
        return round >= DECK_SIZE;
    }

    public int getRound() { return round; }
    public int getDeckSize() { return DECK_SIZE; }

    public List<Card> getDraftedCards() {
        return player.getDeck().getCards();
    }

    public void autoFillOpponent() {
        Collections.shuffle(pool);
        while (opponent.getDeck().size() < DECK_SIZE && !pool.isEmpty()) {
            opponent.addCardToDeck(pool.remove(0));
        }
    }
}
