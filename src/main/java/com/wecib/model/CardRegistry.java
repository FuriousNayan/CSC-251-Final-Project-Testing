package com.wecib.model;

import java.util.ArrayList;
import java.util.List;

public class CardRegistry {

    private static final List<Card> ALL_CARDS = new ArrayList<>();

    static {
        ALL_CARDS.add(new Card.Builder("Evan Yoinko", CardType.AURA, 200)
                .attack(new Attack("The Mace of Java", 50, 1, CardType.AURA))
                .attack(new Attack("Top 1", 120, 3, CardType.AURA))
                .image("/com/wecib/images/yoink.png")
                .build());

        ALL_CARDS.add(new Card.Builder("67 Wannabe", CardType.SHADOW, 100)
                .attack(new Attack("67 Punch", 30, 1, CardType.SHADOW))
                .attack(new Attack("The Skrilla Attack", 75, 2, CardType.SHADOW))
                .image("/com/wecib/images/67 Wannabe.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Karan", CardType.EARTH, 90)
                .attack(new Attack("Fist of the Rook", 25, 1, CardType.EARTH))
                .attack(new Attack("Checkmate", 70, 2, CardType.EARTH))
                .image("/com/wecib/images/karan.png")
                .build());

        ALL_CARDS.add(new Card.Builder("GC", CardType.AURA, 140)
                .attack(new Attack("Nationals Bash Bag", 45, 1, CardType.AURA))
                .attack(new Attack("Green Eyed Gaze", 100, 3, CardType.AURA))
                .image("/com/wecib/images/gabe.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Jacky Chan", CardType.LIGHT, 85)
                .attack(new Attack("Credit Card Slash", 30, 1, CardType.LIGHT))
                .attack(new Attack("Beach House", 75, 2, CardType.LIGHT))
                .image("/com/wecib/images/jack.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Dennis the Menace", CardType.FIRE, 95)
                .attack(new Attack("Mazda Spin", 30, 1, CardType.FIRE))
                .attack(new Attack("Triple Flip Reset", 70, 2, CardType.FIRE))
                .build());

        ALL_CARDS.add(new Card.Builder("Karim the Crop", CardType.DIRT, 110)
                .attack(new Attack("Light Chuckle", 25, 1, CardType.DIRT))
                .attack(new Attack("GMC at 80mph", 75, 3, CardType.DIRT))
                .image("/com/wecib/images/karim.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Master of Unicycle", CardType.WATER, 80)
                .attack(new Attack("The Ride", 15, 1, CardType.WATER))
                .attack(new Attack("Unicycle Rampage", 50, 2, CardType.WATER))
                .image("/com/wecib/images/Marcus Master of Unicycle.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Parthicia", CardType.AURA, 115)
                .attack(new Attack("Aura Blast", 20, 1, CardType.AURA))
                .attack(new Attack("Green Bean", 75, 3, CardType.AURA))
                .image("/com/wecib/images/parth.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Lewkie", CardType.DIRT, 130)
                .attack(new Attack("Dirt Punch", 20, 1, CardType.DIRT))
                .attack(new Attack("Brick by Brick", 70, 2, CardType.DIRT))
                .build());
        
        ALL_CARDS.add(new Card.Builder("Maxwell", CardType.FIRE, 100)
                .attack(new Attack("Words of Curse", 40, 1, CardType.FIRE))
                .attack(new Attack("F-BOMB", 70, 2, CardType.FIRE))
                .image("/com/wecib/images/maxwell.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Chris-Tofer", CardType.FIRE, 120)
                .attack(new Attack("The Mustache", 40, 1, CardType.FIRE))
                .attack(new Attack("Supreme Crashout", 90, 2, CardType.FIRE))
                .image("/com/wecib/images/chris.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Priestba", CardType.STEEL, 110)
                .attack(new Attack("No Steel Shirlock", 40, 1, CardType.STEEL))
                .attack(new Attack("Crucify", 90, 2, CardType.STEEL))
                .image("/com/wecib/images/Priestba.png")
                .build());

        ALL_CARDS.add(new Card.Builder("Quint Collins", CardType.WIND, 110)
                .attack(new Attack("I don't care anymore", 60, 1, CardType.WIND))
                .attack(new Attack("The Sticks of Collins", 90, 2, CardType.WIND))
                .image("/com/wecib/images/quint.png")
                .build());

    }

    public static List<Card> getAllCards() {
        return List.copyOf(ALL_CARDS);
    }

    public static int totalCards() {
        return ALL_CARDS.size();
    }
}
