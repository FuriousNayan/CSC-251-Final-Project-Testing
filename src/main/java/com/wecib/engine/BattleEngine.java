package com.wecib.engine;

import com.wecib.model.Attack;
import com.wecib.model.Card;
import com.wecib.util.TypeChart;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BattleEngine {

    private final PlayerState player;
    private final PlayerState opponent;
    private final List<Consumer<GameEvent<?>>> listeners = new ArrayList<>();
    private boolean playerTurn;
    private boolean battleOver;

    public BattleEngine(PlayerState player, PlayerState opponent) {
        this.player = player;
        this.opponent = opponent;
        this.playerTurn = true;
        this.battleOver = false;
    }

    public void addEventListener(Consumer<GameEvent<?>> listener) {
        listeners.add(listener);
    }

    private void fire(GameEvent<?> event) {
        for (Consumer<GameEvent<?>> l : listeners) {
            l.accept(event);
        }
    }

    public void startBattle() {
        player.setupForBattle();
        opponent.setupForBattle();
        playerTurn = true;
        battleOver = false;
        fire(new GameEvent<>(GameEvent.EventType.BATTLE_START, null));
        fire(new GameEvent<>(GameEvent.EventType.TURN_START, player));
    }

    public AttackResult performAttack(int attackIndex) {
        PlayerState attacker = playerTurn ? player : opponent;
        PlayerState defender = playerTurn ? opponent : player;

        Card attackerCard = attacker.getActiveCard();
        Card defenderCard = defender.getActiveCard();

        if (attackerCard == null || defenderCard == null || battleOver) {
            return null;
        }

        List<Attack> attacks = attackerCard.getAttacks();
        if (attackIndex < 0 || attackIndex >= attacks.size()) return null;

        Attack attack = attacks.get(attackIndex);

        if (!attacker.spendEnergy(attack.getEnergyCost())) {
            return new AttackResult(attack, 0, 1.0, false, "Not enough energy!");
        }

        double multiplier = TypeChart.getMultiplier(attack.getType(), defenderCard.getType());
        int damage = (int) (attack.getDamage() * multiplier);
        defenderCard.takeDamage(damage);

        boolean fainted = !defenderCard.isAlive();
        String effectiveness = TypeChart.getEffectivenessText(attack.getType(), defenderCard.getType());

        AttackResult result = new AttackResult(attack, damage, multiplier, fainted, effectiveness);

        fire(new GameEvent<>(GameEvent.EventType.ATTACK_PERFORMED, result));

        if (fainted) {
            fire(new GameEvent<>(GameEvent.EventType.CARD_FAINTED, defenderCard));

            if (!defender.promoteFromBench()) {
                battleOver = true;
                if (playerTurn) {
                    player.recordWin();
                    fire(new GameEvent<>(GameEvent.EventType.BATTLE_WON, player));
                } else {
                    opponent.recordWin();
                    fire(new GameEvent<>(GameEvent.EventType.BATTLE_LOST, opponent));
                }
                return result;
            }
        }

        endTurn();
        return result;
    }

    public boolean playerSwitch(int benchIndex) {
        if (!playerTurn || battleOver) return false;
        boolean switched = player.switchActiveCard(benchIndex);
        if (switched) {
            fire(new GameEvent<>(GameEvent.EventType.CARD_SWITCHED, player.getActiveCard()));
            endTurn();
        }
        return switched;
    }

    public void performOpponentTurn() {
        if (battleOver || playerTurn) return;

        Card opponentCard = opponent.getActiveCard();
        if (opponentCard == null || opponentCard.getAttacks().isEmpty()) {
            endTurn();
            return;
        }

        int bestIndex = 0;
        int bestDamage = 0;
        Card playerCard = player.getActiveCard();

        for (int i = 0; i < opponentCard.getAttacks().size(); i++) {
            Attack a = opponentCard.getAttacks().get(i);
            if (a.getEnergyCost() <= opponent.getEnergy()) {
                double mult = TypeChart.getMultiplier(a.getType(), playerCard.getType());
                int dmg = (int) (a.getDamage() * mult);
                if (dmg > bestDamage) {
                    bestDamage = dmg;
                    bestIndex = i;
                }
            }
        }

        performAttack(bestIndex);
    }

    private void endTurn() {
        playerTurn = !playerTurn;
        PlayerState current = playerTurn ? player : opponent;
        current.gainEnergy();
        fire(new GameEvent<>(GameEvent.EventType.TURN_START, current));
    }

    public boolean isPlayerTurn() { return playerTurn; }
    public boolean isBattleOver() { return battleOver; }
    public PlayerState getPlayer() { return player; }
    public PlayerState getOpponent() { return opponent; }

    public static class AttackResult {
        private final Attack attack;
        private final int damage;
        private final double multiplier;
        private final boolean targetFainted;
        private final String effectivenessText;

        public AttackResult(Attack attack, int damage, double multiplier,
                            boolean targetFainted, String effectivenessText) {
            this.attack = attack;
            this.damage = damage;
            this.multiplier = multiplier;
            this.targetFainted = targetFainted;
            this.effectivenessText = effectivenessText;
        }

        public Attack getAttack() { return attack; }
        public int getDamage() { return damage; }
        public double getMultiplier() { return multiplier; }
        public boolean isTargetFainted() { return targetFainted; }
        public String getEffectivenessText() { return effectivenessText; }
    }
}
