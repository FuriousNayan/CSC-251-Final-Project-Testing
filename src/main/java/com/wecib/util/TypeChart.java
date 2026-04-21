package com.wecib.util;

import com.wecib.model.CardType;

import java.util.EnumMap;
import java.util.Map;

public class TypeChart {

    private static final Map<CardType, Map<CardType, Double>> CHART = new EnumMap<>(CardType.class);

    static {
        for (CardType attacker : CardType.values()) {
            CHART.put(attacker, new EnumMap<>(CardType.class));
            for (CardType defender : CardType.values()) {
                CHART.get(attacker).put(defender, 1.0);
            }
        }

        setMatchup(CardType.FIRE, CardType.EARTH, 2.0);
        setMatchup(CardType.FIRE, CardType.WIND, 2.0);
        setMatchup(CardType.FIRE, CardType.WATER, 0.5);
        setMatchup(CardType.FIRE, CardType.SHADOW, 0.5);

        setMatchup(CardType.WATER, CardType.FIRE, 2.0);
        setMatchup(CardType.WATER, CardType.EARTH, 2.0);
        setMatchup(CardType.WATER, CardType.ELECTRIC, 0.5);
        setMatchup(CardType.WATER, CardType.WIND, 0.5);

        setMatchup(CardType.EARTH, CardType.ELECTRIC, 2.0);
        setMatchup(CardType.EARTH, CardType.FIRE, 0.5);
        setMatchup(CardType.EARTH, CardType.WIND, 0.5);

        setMatchup(CardType.ELECTRIC, CardType.WATER, 2.0);
        setMatchup(CardType.ELECTRIC, CardType.WIND, 2.0);
        setMatchup(CardType.ELECTRIC, CardType.EARTH, 0.5);

        setMatchup(CardType.WIND, CardType.EARTH, 2.0);
        setMatchup(CardType.WIND, CardType.SHADOW, 2.0);
        setMatchup(CardType.WIND, CardType.ELECTRIC, 0.5);
        setMatchup(CardType.WIND, CardType.FIRE, 0.5);

        setMatchup(CardType.SHADOW, CardType.LIGHT, 2.0);
        setMatchup(CardType.SHADOW, CardType.WIND, 2.0);
        setMatchup(CardType.SHADOW, CardType.FIRE, 0.5);

        setMatchup(CardType.LIGHT, CardType.SHADOW, 2.0);
        setMatchup(CardType.LIGHT, CardType.FIRE, 2.0);
        setMatchup(CardType.LIGHT, CardType.EARTH, 0.5);

        setMatchup(CardType.AURA, CardType.SHADOW, 2.0);
        setMatchup(CardType.AURA, CardType.LIGHT, 2.0);
        setMatchup(CardType.AURA, CardType.ELECTRIC, 0.5);
        setMatchup(CardType.AURA, CardType.EARTH, 0.5);

        setMatchup(CardType.DIRT, CardType.ELECTRIC, 2.0);
        setMatchup(CardType.DIRT, CardType.FIRE, 2.0);
        setMatchup(CardType.DIRT, CardType.WATER, 0.5);
        setMatchup(CardType.DIRT, CardType.WIND, 0.5);

        setMatchup(CardType.STEEL, CardType.EARTH, 2.0);
        setMatchup(CardType.STEEL, CardType.LIGHT, 2.0);
        setMatchup(CardType.STEEL, CardType.FIRE, 0.5);
        setMatchup(CardType.STEEL, CardType.ELECTRIC, 0.5);
    }

    private static void setMatchup(CardType attacker, CardType defender, double multiplier) {
        CHART.get(attacker).put(defender, multiplier);
    }

    public static double getMultiplier(CardType attackType, CardType defenderType) {
        return CHART.get(attackType).get(defenderType);
    }

    public static String getEffectivenessText(CardType attackType, CardType defenderType) {
        double mult = getMultiplier(attackType, defenderType);
        if (mult > 1.0) return "Super effective!";
        if (mult < 1.0) return "Not very effective...";
        return "";
    }
}
