package com.wecib.engine;

public class GameEvent<T> {

    public enum EventType {
        STATE_CHANGE,
        DRAFT_CHOICES_READY,
        CARD_DRAFTED,
        DRAFT_COMPLETE,
        BATTLE_START,
        TURN_START,
        ATTACK_PERFORMED,
        CARD_FAINTED,
        CARD_SWITCHED,
        BATTLE_WON,
        BATTLE_LOST
    }

    private final EventType type;
    private final T payload;

    public GameEvent(EventType type, T payload) {
        this.type = type;
        this.payload = payload;
    }

    public EventType getType() { return type; }
    public T getPayload() { return payload; }

    @Override
    public String toString() {
        return "GameEvent{type=" + type + ", payload=" + payload + "}";
    }
}
