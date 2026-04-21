package com.wecib.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class TitleScreen extends VBox {

    public TitleScreen(Runnable onStart) {
        setAlignment(Pos.CENTER);
        setSpacing(0);
        getStyleClass().add("title-screen");

        Region topSpacer = new Region();
        topSpacer.setPrefHeight(80);

        HBox logoRow = new HBox(12);
        logoRow.setAlignment(Pos.CENTER);

        Circle c1 = new Circle(10);
        c1.getStyleClass().add("logo-dot-red");
        Circle c2 = new Circle(10);
        c2.getStyleClass().add("logo-dot-blue");
        Circle c3 = new Circle(10);
        c3.getStyleClass().add("logo-dot-gold");
        logoRow.getChildren().addAll(c1, c2, c3);

        Label title = new Label("WECIB");
        title.getStyleClass().add("title-text");

        Label tagline = new Label("CARD   GAME");
        tagline.getStyleClass().add("title-tagline");

        Region divider = new Region();
        divider.getStyleClass().add("title-divider");
        divider.setPrefWidth(280);
        divider.setMaxWidth(280);
        divider.setPrefHeight(3);

        Label subtitle = new Label("Draft your deck. Battle your rivals.\nBecome the WECIB Master.");
        subtitle.getStyleClass().add("subtitle-text");
        subtitle.setWrapText(true);

        Region midSpacer = new Region();
        midSpacer.setPrefHeight(45);

        Button startButton = new Button("START GAME");
        startButton.getStyleClass().add("primary-button");
        startButton.setOnAction(e -> onStart.run());

        ScaleTransition pulse = new ScaleTransition(Duration.millis(1200), startButton);
        pulse.setFromX(1.0); pulse.setToX(1.04);
        pulse.setFromY(1.0); pulse.setToY(1.04);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(ScaleTransition.INDEFINITE);
        pulse.play();

        Label hint = new Label("Pick cards in the draft, then battle head-to-head");
        hint.getStyleClass().add("hint-text");

        Region bottomSpacer = new Region();
        bottomSpacer.setPrefHeight(30);

        Label version = new Label("v1.0  |  CSC 251 Final Project");
        version.getStyleClass().add("version-text");

        getChildren().addAll(topSpacer, logoRow, title, tagline, divider, subtitle,
                midSpacer, startButton, hint, bottomSpacer, version);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
        fadeIn.setFromValue(0); fadeIn.setToValue(1);
        fadeIn.play();
    }
}
