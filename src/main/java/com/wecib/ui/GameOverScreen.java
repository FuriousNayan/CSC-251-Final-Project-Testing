package com.wecib.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameOverScreen extends VBox {

    public GameOverScreen(boolean won, String playerName, Runnable onRestart) {
        setAlignment(Pos.CENTER);
        setSpacing(16);
        getStyleClass().add("game-over-screen");

        Region topSpacer = new Region();
        topSpacer.setPrefHeight(60);

        Label badge = new Label(won ? "CHAMPION" : "DEFEATED");
        badge.getStyleClass().add("game-over-badge");
        badge.getStyleClass().add(won ? "badge-win" : "badge-lose");

        Label resultLabel = new Label(won ? "VICTORY!" : "DEFEAT");
        resultLabel.getStyleClass().add(won ? "victory-text" : "defeat-text");

        ScaleTransition pulse = new ScaleTransition(Duration.millis(1500), resultLabel);
        pulse.setFromX(1.0); pulse.setToX(1.06);
        pulse.setFromY(1.0); pulse.setToY(1.06);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(ScaleTransition.INDEFINITE);
        pulse.play();

        String message = won
                ? playerName + " is the new WECIB Master!"
                : "Better luck next time, " + playerName + "...";
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("game-over-message");

        Region midSpacer = new Region();
        midSpacer.setPrefHeight(35);

        Button restartBtn = new Button("PLAY AGAIN");
        restartBtn.getStyleClass().add("primary-button");
        restartBtn.setOnAction(e -> onRestart.run());

        getChildren().addAll(topSpacer, badge, resultLabel, messageLabel, midSpacer, restartBtn);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), this);
        fadeIn.setFromValue(0); fadeIn.setToValue(1);
        fadeIn.play();
    }
}
