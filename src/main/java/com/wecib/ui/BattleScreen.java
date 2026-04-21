package com.wecib.ui;

import com.wecib.engine.BattleEngine;
import com.wecib.engine.GameEvent;
import com.wecib.model.Attack;
import com.wecib.model.Card;
import com.wecib.model.CardType;
import com.wecib.util.TypeChart;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class BattleScreen extends VBox {

    private final BattleEngine battleEngine;
    private final Consumer<Boolean> onBattleEnd;

    private final CardView playerCardView;
    private final CardView opponentCardView;
    private final HBox playerBench;
    private final HBox opponentBench;
    private final Label battleLog;
    private final Label effectivenessLabel;
    private final HBox attackButtons;
    private final Label turnBanner;
    private final HBox energyDots;
    private final Label damagePopup;
    private final StackPane boardPane;
    private final Rectangle flashOverlay;
    private final Label matchupLabel;
    private final Random random = new Random();

    private Timeline playerPulse;
    private Timeline opponentPulse;

    public BattleScreen(BattleEngine battleEngine, Consumer<Boolean> onBattleEnd) {
        this.battleEngine = battleEngine;
        this.onBattleEnd = onBattleEnd;
        getStyleClass().add("battle-screen");
        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);

        boardPane = new StackPane();
        boardPane.getStyleClass().add("game-board");
        VBox.setVgrow(boardPane, Priority.ALWAYS);

        VBox boardContent = new VBox(10);
        boardContent.setAlignment(Pos.CENTER);
        boardContent.setPadding(new Insets(15, 25, 15, 25));

        // Opponent zone
        VBox oppZone = new VBox(10);
        oppZone.setAlignment(Pos.CENTER);
        oppZone.getStyleClass().add("opponent-zone");
        oppZone.setPadding(new Insets(12, 20, 12, 20));

        HBox oppTopRow = new HBox(12);
        oppTopRow.setAlignment(Pos.CENTER_LEFT);
        Label oppName = new Label(battleEngine.getOpponent().getName());
        oppName.getStyleClass().add("opponent-name-label");
        Region s1 = new Region();
        HBox.setHgrow(s1, Priority.ALWAYS);
        Label oppBenchTitle = new Label("BENCH");
        oppBenchTitle.getStyleClass().add("zone-title");
        oppTopRow.getChildren().addAll(oppName, s1, oppBenchTitle);

        HBox oppCardRow = new HBox(20);
        oppCardRow.setAlignment(Pos.CENTER);
        opponentCardView = new CardView(null, CardView.Size.MEDIUM);
        opponentCardView.getStyleClass().add("active-card-opponent");
        Region s2 = new Region();
        HBox.setHgrow(s2, Priority.ALWAYS);
        opponentBench = new HBox(10);
        opponentBench.setAlignment(Pos.CENTER);
        oppCardRow.getChildren().addAll(opponentCardView, s2, opponentBench);

        oppZone.getChildren().addAll(oppTopRow, oppCardRow);

        // Center strip
        HBox centerStrip = new HBox(20);
        centerStrip.setAlignment(Pos.CENTER);
        centerStrip.getStyleClass().add("center-strip");
        centerStrip.setPadding(new Insets(10, 20, 10, 20));

        turnBanner = new Label("YOUR TURN");
        turnBanner.getStyleClass().add("turn-banner");

        effectivenessLabel = new Label("");
        effectivenessLabel.getStyleClass().add("effectiveness-label");

        battleLog = new Label("Battle Start!");
        battleLog.getStyleClass().add("battle-log");
        battleLog.setWrapText(true);
        HBox.setHgrow(battleLog, Priority.ALWAYS);

        centerStrip.getChildren().addAll(turnBanner, effectivenessLabel, battleLog);

        damagePopup = new Label();
        damagePopup.getStyleClass().add("damage-popup");
        damagePopup.setVisible(false);
        damagePopup.setMouseTransparent(true);

        // Player zone
        VBox plrZone = new VBox(10);
        plrZone.setAlignment(Pos.CENTER);
        plrZone.getStyleClass().add("player-zone");
        plrZone.setPadding(new Insets(12, 20, 12, 20));

        HBox plrCardRow = new HBox(20);
        plrCardRow.setAlignment(Pos.CENTER);
        playerBench = new HBox(10);
        playerBench.setAlignment(Pos.CENTER);
        Region s3 = new Region();
        HBox.setHgrow(s3, Priority.ALWAYS);
        playerCardView = new CardView(null, CardView.Size.MEDIUM);
        playerCardView.getStyleClass().add("active-card-player");
        plrCardRow.getChildren().addAll(playerBench, s3, playerCardView);

        HBox plrBottomRow = new HBox(12);
        plrBottomRow.setAlignment(Pos.CENTER_LEFT);
        Label plrBenchTitle = new Label("BENCH");
        plrBenchTitle.getStyleClass().add("zone-title");
        Region s4 = new Region();
        HBox.setHgrow(s4, Priority.ALWAYS);
        Label plrName = new Label(battleEngine.getPlayer().getName());
        plrName.getStyleClass().add("player-name-label");
        plrBottomRow.getChildren().addAll(plrBenchTitle, s4, plrName);

        plrZone.getChildren().addAll(plrCardRow, plrBottomRow);

        boardContent.getChildren().addAll(oppZone, centerStrip, plrZone);

        flashOverlay = new Rectangle();
        flashOverlay.setMouseTransparent(true);
        flashOverlay.widthProperty().bind(boardPane.widthProperty());
        flashOverlay.heightProperty().bind(boardPane.heightProperty());
        flashOverlay.setFill(Color.WHITE);
        flashOverlay.setOpacity(0);

        boardPane.getChildren().addAll(boardContent, flashOverlay, damagePopup);

        // Action panel
        VBox actionPanel = new VBox(5);
        actionPanel.setAlignment(Pos.CENTER);
        actionPanel.getStyleClass().add("action-panel");
        actionPanel.setPadding(new Insets(8, 30, 10, 30));
        actionPanel.setMinHeight(140);

        HBox energyRow = new HBox(8);
        energyRow.setAlignment(Pos.CENTER);
        Label energyTitle = new Label("ENERGY");
        energyTitle.getStyleClass().add("energy-title");
        energyDots = new HBox(6);
        energyDots.setAlignment(Pos.CENTER);
        energyRow.getChildren().addAll(energyTitle, energyDots);

        attackButtons = new HBox(14);
        attackButtons.setAlignment(Pos.CENTER);
        attackButtons.setMinHeight(44);

        matchupLabel = new Label("");
        matchupLabel.getStyleClass().add("matchup-preview");
        matchupLabel.setMinHeight(24);
        matchupLabel.setPrefHeight(24);
        matchupLabel.setVisible(false);
        matchupLabel.setManaged(true);

        actionPanel.getChildren().addAll(energyRow, attackButtons, matchupLabel);

        getChildren().addAll(boardPane, actionPanel);

        battleEngine.addEventListener(this::handleEvent);
        battleEngine.startBattle();
        refreshUI();
        startIdlePulse();
    }

    // ===== IDLE GLOW PULSE =====
    private void startIdlePulse() {
        playerPulse = createPulse(playerCardView, Color.rgb(59, 130, 246));
        opponentPulse = createPulse(opponentCardView, Color.rgb(239, 68, 68));
        playerPulse.play();
        opponentPulse.play();
    }

    private Timeline createPulse(CardView card, Color glowColor) {
        DropShadow glow = new DropShadow(18, glowColor);
        glow.setSpread(0.15);
        card.setEffect(glow);

        Timeline pulse = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glow.radiusProperty(), 14)),
            new KeyFrame(Duration.ZERO, new KeyValue(glow.spreadProperty(), 0.1)),
            new KeyFrame(Duration.millis(1200), new KeyValue(glow.radiusProperty(), 26)),
            new KeyFrame(Duration.millis(1200), new KeyValue(glow.spreadProperty(), 0.25))
        );
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Timeline.INDEFINITE);
        return pulse;
    }

    private void handleEvent(GameEvent<?> event) {
        switch (event.getType()) {
            case ATTACK_PERFORMED -> {
                BattleEngine.AttackResult result = (BattleEngine.AttackResult) event.getPayload();
                battleLog.setText(result.getAttack().getName() + " deals " + result.getDamage() + " damage!");

                String eff = result.getEffectivenessText();
                effectivenessLabel.setText(eff);
                effectivenessLabel.getStyleClass().removeAll("super-effective", "not-effective");
                if (eff.contains("Super")) effectivenessLabel.getStyleClass().add("super-effective");
                else if (eff.contains("Not")) effectivenessLabel.getStyleClass().add("not-effective");
                if (!eff.isEmpty()) flashLabel(effectivenessLabel);

                playAttackAnimation(result.getDamage(), result.getMultiplier(), result.getAttack().getType());
            }
            case CARD_FAINTED -> {
                Card fainted = (Card) event.getPayload();
                battleLog.setText(fainted.getName() + " has been knocked out!");
                effectivenessLabel.setText("");
                playKOAnimation();
            }
            case BATTLE_WON -> {
                turnBanner.setText("VICTORY!");
                turnBanner.getStyleClass().add("victory-banner");
                battleLog.setText("All opponent cards knocked out!");
                attackButtons.getChildren().clear();
                scheduleEnd(true);
            }
            case BATTLE_LOST -> {
                turnBanner.setText("DEFEAT");
                turnBanner.getStyleClass().add("defeat-banner");
                battleLog.setText("All your cards knocked out...");
                attackButtons.getChildren().clear();
                scheduleEnd(false);
            }
            default -> {}
        }
    }

    // ===== KO ANIMATION =====
    private void playKOAnimation() {
        CardView target = battleEngine.isPlayerTurn() ? opponentCardView : playerCardView;

        RotateTransition spin = new RotateTransition(Duration.millis(600), target);
        spin.setByAngle(720);

        ScaleTransition shrink = new ScaleTransition(Duration.millis(600), target);
        shrink.setToX(0); shrink.setToY(0);

        FadeTransition fade = new FadeTransition(Duration.millis(600), target);
        fade.setToValue(0);

        TranslateTransition flyOff = new TranslateTransition(Duration.millis(600), target);
        flyOff.setByY(battleEngine.isPlayerTurn() ? -80 : 80);

        ParallelTransition ko = new ParallelTransition(spin, shrink, fade, flyOff);
        ko.setOnFinished(e -> {
            target.setRotate(0);
            target.setScaleX(1); target.setScaleY(1);
            target.setOpacity(1);
            target.setTranslateX(0); target.setTranslateY(0);

            PauseTransition wait = new PauseTransition(Duration.millis(300));
            wait.setOnFinished(ev -> refreshUI());
            wait.play();
        });
        ko.play();
    }

    private void refreshUI() {
        Card playerCard = battleEngine.getPlayer().getActiveCard();
        Card opponentCard = battleEngine.getOpponent().getActiveCard();

        playerCardView.setCard(playerCard);
        opponentCardView.setCard(opponentCard);

        refreshBench(playerBench, battleEngine.getPlayer().getBench(), true);
        refreshBench(opponentBench, battleEngine.getOpponent().getBench(), false);

        int energy = battleEngine.getPlayer().getEnergy();
        refreshEnergyDots(energy);

        attackButtons.setDisable(false);
        attackButtons.getChildren().clear();
        matchupLabel.setVisible(false);

        if (battleEngine.isPlayerTurn() && playerCard != null && !battleEngine.isBattleOver()) {
            turnBanner.setText("YOUR TURN");
            turnBanner.getStyleClass().removeAll("opponent-turn", "victory-banner", "defeat-banner");

            Card oppCard = battleEngine.getOpponent().getActiveCard();

            for (int i = 0; i < playerCard.getAttacks().size(); i++) {
                Attack atk = playerCard.getAttacks().get(i);
                boolean canAfford = atk.getEnergyCost() <= energy;

                Button btn = new Button();
                btn.getStyleClass().add("attack-button");
                btn.setDisable(!canAfford);

                HBox btnContent = new HBox(8);
                btnContent.setAlignment(Pos.CENTER);
                btnContent.setMouseTransparent(true);

                for (int e = 0; e < atk.getEnergyCost(); e++) {
                    Circle ec = new Circle(6);
                    ec.setStyle("-fx-fill: " + CardView.getTypeColor(atk.getType()) + "; -fx-stroke: rgba(255,255,255,0.3); -fx-stroke-width: 1;");
                    btnContent.getChildren().add(ec);
                }

                Label atkName = new Label(atk.getName());
                atkName.getStyleClass().add("atk-btn-name");
                Label atkDmg = new Label(atk.getDamage() + "");
                atkDmg.getStyleClass().add("atk-btn-dmg");

                btnContent.getChildren().addAll(atkName, atkDmg);
                btn.setGraphic(btnContent);

                final int idx = i;
                btn.setOnAction(e -> {
                    matchupLabel.setVisible(false);
                    battleEngine.performAttack(idx);
                    refreshUI();
                    if (!battleEngine.isBattleOver() && !battleEngine.isPlayerTurn()) doOpponentTurn();
                });

                // ===== MATCHUP PREVIEW ON HOVER =====
                if (oppCard != null && canAfford) {
                    final Attack hoverAtk = atk;
                    btn.setOnMouseEntered(e -> showMatchupPreview(hoverAtk, oppCard));
                    btn.setOnMouseExited(e -> matchupLabel.setVisible(false));
                }

                attackButtons.getChildren().add(btn);
            }
        } else if (!battleEngine.isBattleOver()) {
            turnBanner.setText("OPPONENT'S TURN");
            turnBanner.getStyleClass().removeAll("victory-banner", "defeat-banner");
            if (!turnBanner.getStyleClass().contains("opponent-turn"))
                turnBanner.getStyleClass().add("opponent-turn");
        }

        startIdlePulse();
    }

    // ===== MATCHUP PREVIEW =====
    private void showMatchupPreview(Attack atk, Card defender) {
        double mult = TypeChart.getMultiplier(atk.getType(), defender.getType());
        int estimatedDmg = (int) (atk.getDamage() * mult);

        String effText;
        String styleClass;
        if (mult > 1.0) {
            effText = "Super effective! ~" + estimatedDmg + " dmg (2x)";
            styleClass = "matchup-super";
        } else if (mult < 1.0) {
            effText = "Not very effective ~" + estimatedDmg + " dmg (0.5x)";
            styleClass = "matchup-weak";
        } else {
            effText = "Normal effectiveness ~" + estimatedDmg + " dmg";
            styleClass = "matchup-neutral";
        }

        matchupLabel.setText(atk.getType().displayName() + " vs " + defender.getType().displayName() + "  —  " + effText);
        matchupLabel.getStyleClass().removeAll("matchup-super", "matchup-weak", "matchup-neutral");
        matchupLabel.getStyleClass().add(styleClass);
        matchupLabel.setVisible(true);
    }

    private void refreshBench(HBox benchBox, List<Card> benchCards, boolean isPlayer) {
        benchBox.getChildren().clear();
        for (int i = 0; i < benchCards.size(); i++) {
            Card c = benchCards.get(i);
            CardView mini = new CardView(c, CardView.Size.SMALL);
            mini.getStyleClass().add("bench-card");

            if (isPlayer && battleEngine.isPlayerTurn() && !battleEngine.isBattleOver()) {
                final int idx = i;
                mini.setOnMouseClicked(e -> {
                    if (battleEngine.playerSwitch(idx)) {
                        battleLog.setText("Switched to " + battleEngine.getPlayer().getActiveCard().getName() + "!");
                        effectivenessLabel.setText("");
                        refreshUI();
                        if (!battleEngine.isBattleOver() && !battleEngine.isPlayerTurn()) doOpponentTurn();
                    }
                });
                mini.getStyleClass().add("bench-card-clickable");
            }
            benchBox.getChildren().add(mini);
        }
    }

    private void refreshEnergyDots(int energy) {
        energyDots.getChildren().clear();
        for (int i = 0; i < 5; i++) {
            Circle dot = new Circle(11);
            dot.getStyleClass().add(i < energy ? "energy-dot-filled" : "energy-dot-empty");
            energyDots.getChildren().add(dot);
        }
    }

    private void showDamagePopup(String text, boolean hit) {
        damagePopup.setText(text);
        damagePopup.setVisible(true);
        damagePopup.getStyleClass().removeAll("damage-hit", "damage-miss");
        damagePopup.getStyleClass().add(hit ? "damage-hit" : "damage-miss");

        FadeTransition ft = new FadeTransition(Duration.millis(800), damagePopup);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setDelay(Duration.millis(400));
        ft.setOnFinished(e -> damagePopup.setVisible(false));
        ft.play();
    }

    // ===== ATTACK ANIMATION + PARTICLES =====
    private void playAttackAnimation(int damage, double multiplier, CardType attackType) {
        boolean isPlayer = !battleEngine.isPlayerTurn();
        CardView attacker = isPlayer ? playerCardView : opponentCardView;
        CardView target = isPlayer ? opponentCardView : playerCardView;
        double lungeDir = isPlayer ? -1 : 1;

        TranslateTransition lunge = new TranslateTransition(Duration.millis(120), attacker);
        lunge.setByY(lungeDir * 30);
        lunge.setAutoReverse(true);
        lunge.setCycleCount(2);

        if (damage > 0) {
            PauseTransition hitDelay = new PauseTransition(Duration.millis(120));
            hitDelay.setOnFinished(e -> {
                screenFlash(multiplier);
                shakeTarget(target, damage);
                redFlash(target);
                spawnParticles(target, attackType, damage);
                showDamagePopup("-" + damage, true);
            });

            lunge.play();
            hitDelay.play();
        } else {
            lunge.play();
            showDamagePopup("MISS", false);
        }
    }

    // ===== PARTICLE EFFECTS =====
    private void spawnParticles(CardView target, CardType type, int damage) {
        Color particleColor = Color.web(CardView.getTypeColor(type));
        int count = damage > 60 ? 16 : 10;

        for (int i = 0; i < count; i++) {
            Circle particle = new Circle(random.nextDouble() * 3 + 2);
            particle.setFill(particleColor);
            particle.setOpacity(0.9);
            particle.setMouseTransparent(true);

            boardPane.getChildren().add(particle);

            double angle = random.nextDouble() * 360;
            double distance = random.nextDouble() * 80 + 40;
            double dx = Math.cos(Math.toRadians(angle)) * distance;
            double dy = Math.sin(Math.toRadians(angle)) * distance;

            TranslateTransition move = new TranslateTransition(Duration.millis(random.nextDouble() * 300 + 300), particle);
            move.setByX(dx);
            move.setByY(dy);

            FadeTransition fade = new FadeTransition(Duration.millis(random.nextDouble() * 200 + 400), particle);
            fade.setFromValue(0.9);
            fade.setToValue(0);

            ScaleTransition grow = new ScaleTransition(Duration.millis(400), particle);
            grow.setToX(0.2); grow.setToY(0.2);

            ParallelTransition pt = new ParallelTransition(move, fade, grow);
            pt.setOnFinished(e -> boardPane.getChildren().remove(particle));
            pt.play();
        }
    }

    private void screenFlash(double multiplier) {
        double intensity = multiplier > 1.0 ? 0.35 : 0.15;
        flashOverlay.setFill(multiplier > 1.0
                ? Color.rgb(239, 68, 68, intensity)
                : Color.rgb(255, 255, 255, intensity));
        flashOverlay.setOpacity(1);

        FadeTransition flash = new FadeTransition(Duration.millis(200), flashOverlay);
        flash.setFromValue(1);
        flash.setToValue(0);
        flash.play();
    }

    private void shakeTarget(CardView target, int damage) {
        int shakeCount = damage > 60 ? 4 : 2;
        double shakeAmount = damage > 60 ? 10 : 5;

        Timeline shake = new Timeline();
        for (int i = 0; i < shakeCount; i++) {
            double offset = (i % 2 == 0) ? shakeAmount : -shakeAmount;
            shake.getKeyFrames().add(
                new KeyFrame(Duration.millis(50 * i), new KeyValue(target.translateXProperty(), offset))
            );
        }
        shake.getKeyFrames().add(
            new KeyFrame(Duration.millis(50 * shakeCount), new KeyValue(target.translateXProperty(), 0))
        );

        ScaleTransition recoil = new ScaleTransition(Duration.millis(100), target);
        recoil.setToX(0.92); recoil.setToY(0.92);
        recoil.setAutoReverse(true);
        recoil.setCycleCount(2);

        shake.play();
        recoil.play();
    }

    private void redFlash(CardView target) {
        ColorAdjust colorAdjust = new ColorAdjust();
        target.setEffect(colorAdjust);

        Timeline redPulse = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(colorAdjust.hueProperty(), 0)),
            new KeyFrame(Duration.millis(80), new KeyValue(colorAdjust.hueProperty(), -0.05)),
            new KeyFrame(Duration.millis(80), new KeyValue(colorAdjust.brightnessProperty(), 0.3)),
            new KeyFrame(Duration.millis(300), new KeyValue(colorAdjust.hueProperty(), 0)),
            new KeyFrame(Duration.millis(300), new KeyValue(colorAdjust.brightnessProperty(), 0))
        );
        redPulse.setOnFinished(e -> target.setEffect(null));
        redPulse.play();
    }

    private void flashLabel(Label label) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), label);
        ft.setFromValue(0); ft.setToValue(1);
        ft.play();
    }

    private void doOpponentTurn() {
        turnBanner.setText("OPPONENT'S TURN");
        turnBanner.getStyleClass().removeAll("victory-banner", "defeat-banner");
        if (!turnBanner.getStyleClass().contains("opponent-turn"))
            turnBanner.getStyleClass().add("opponent-turn");
        attackButtons.setDisable(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
        pause.setOnFinished(e -> {
            battleEngine.performOpponentTurn();
            attackButtons.setDisable(false);
            refreshUI();
            if (!battleEngine.isBattleOver() && !battleEngine.isPlayerTurn()) doOpponentTurn();
        });
        pause.play();
    }

    private void scheduleEnd(boolean won) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished(e -> onBattleEnd.accept(won));
        pause.play();
    }
}
