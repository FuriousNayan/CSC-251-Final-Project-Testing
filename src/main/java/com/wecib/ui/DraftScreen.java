package com.wecib.ui;

import com.wecib.engine.DraftEngine;
import com.wecib.model.Card;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Consumer;

public class DraftScreen extends VBox {

    private final DraftEngine draftEngine;
    private final Consumer<Void> onDraftComplete;
    private final Label roundLabel;
    private final HBox choicesBox;
    private final HBox deckPreviewRow;
    private final Label counterLabel;

    public DraftScreen(DraftEngine draftEngine, Consumer<Void> onDraftComplete) {
        this.draftEngine = draftEngine;
        this.onDraftComplete = onDraftComplete;

        setAlignment(Pos.TOP_CENTER);
        setSpacing(8); // smaller vertical spacing
        setPadding(new Insets(16, 18, 12, 18)); // smaller paddings
        getStyleClass().add("draft-screen");

        Label header = new Label("DRAFT YOUR DECK");
        header.getStyleClass().add("screen-header");
        header.setWrapText(true);
        header.setMaxWidth(Double.MAX_VALUE);
        header.setAlignment(Pos.CENTER);
        // Bind max width to this VBox width so it never overflows
        header.maxWidthProperty().bind(widthProperty().subtract(getPadding() != null ? (getPadding().getLeft() + getPadding().getRight()) : 36));

        roundLabel = new Label();
        roundLabel.getStyleClass().add("round-label");

        Label instruction = new Label("Choose a card to add to your deck");
        instruction.getStyleClass().add("instruction-text");

        choicesBox = new HBox(18); // smaller horizontal spacing
        choicesBox.setAlignment(Pos.CENTER);
        choicesBox.setPadding(new Insets(8, 0, 8, 0));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox deckHeader = new HBox(10);
        deckHeader.setAlignment(Pos.CENTER);
        Label deckLabel = new Label("YOUR DECK");
        deckLabel.getStyleClass().add("deck-label");
        counterLabel = new Label("0 / " + draftEngine.getDeckSize());
        counterLabel.getStyleClass().add("deck-counter");
        deckHeader.getChildren().addAll(deckLabel, counterLabel);

        deckPreviewRow = new HBox(7); // smaller spacing for deck preview
        deckPreviewRow.setAlignment(Pos.CENTER);
        deckPreviewRow.setPadding(new Insets(4, 0, 4, 0));

        getChildren().addAll(header, roundLabel, instruction, choicesBox, spacer, deckHeader, deckPreviewRow);

        // Responsive scaling: listen to scene width and update card sizes
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((o, oldW, newW) -> updateCardSizes(newW.doubleValue()));
                updateCardSizes(newScene.getWidth());
            }
        });
        showNextRound();

    }

    // Dynamically update card sizes based on scene width
    private void updateCardSizes(double sceneWidth) {
        // Calculate available width for choices row (subtract paddings)
        double leftPad = getPadding() != null ? getPadding().getLeft() : 40;
        double rightPad = getPadding() != null ? getPadding().getRight() : 40;
        double availableWidth = sceneWidth - leftPad - rightPad;
        int numCards = choicesBox.getChildren().size();
        if (numCards == 0) return;
        double spacing = choicesBox.getSpacing();
        // Card base width (should match CardView BASE_DIMENSIONS for LARGE)
        double cardBaseWidth = 250.0;
        // Calculate max scale so all cards + spacing fit in available width
        double maxScale = (availableWidth - (numCards - 1) * spacing) / (numCards * cardBaseWidth);
        // Clamp scale between 0.35 and 0.85 for compactness
        double scale = Math.max(0.35, Math.min(0.85, maxScale));
        for (int i = 0; i < numCards; i++) {
            VBox cardSlot = (VBox) choicesBox.getChildren().get(i);
            StackPane flipContainer = (StackPane) cardSlot.getChildren().get(0);
            for (javafx.scene.Node node : flipContainer.getChildren()) {
                if (node instanceof Region && node.getStyleClass().contains("card-back")) {
                    ((Region) node).setPrefSize(cardBaseWidth * scale, 370 * scale);
                    ((Region) node).setMinSize(cardBaseWidth * scale, 370 * scale);
                    ((Region) node).setMaxSize(cardBaseWidth * scale, 370 * scale);
                } else if (node instanceof CardView) {
                    ((CardView) node).setScaledSize(scale);
                }
            }
            flipContainer.setPrefSize(cardBaseWidth * scale, 370 * scale);
            flipContainer.setMaxSize(cardBaseWidth * scale, 370 * scale);
        }
    }
    }

    private void showNextRound() {
        if (draftEngine.isDraftComplete()) {
            draftEngine.autoFillOpponent();
            onDraftComplete.accept(null);
            return;
        }

        roundLabel.setText("Round " + (draftEngine.getRound() + 1) + " of " + draftEngine.getDeckSize());
        List<Card> choices = draftEngine.nextChoices();
        choicesBox.getChildren().clear();

        for (int i = 0; i < choices.size(); i++) {
            Card card = choices.get(i);
            VBox cardSlot = new VBox(10);
            cardSlot.setAlignment(Pos.CENTER);
            cardSlot.getStyleClass().add("draft-card-slot");

            Region cardBack = new Region();
            cardBack.setPrefSize(250, 370);
            cardBack.setMinSize(250, 370);
            cardBack.setMaxSize(250, 370);
            cardBack.getStyleClass().add("card-back");

            CardView cardView = new CardView(card, CardView.Size.LARGE);
            cardView.setScaleX(0);

            StackPane flipContainer = new StackPane(cardBack, cardView);
            flipContainer.setPrefSize(250, 370);
            flipContainer.setMaxSize(250, 370);

            Button pickBtn = new Button("PICK");
            pickBtn.getStyleClass().add("pick-button");
            pickBtn.setVisible(false);

            final int index = i;
            pickBtn.setOnAction(e -> {
                draftEngine.playerPicks(index);
                updateDeckPreview();
                animateNewRound();
            });

            cardSlot.getChildren().addAll(flipContainer, pickBtn);
            choicesBox.getChildren().add(cardSlot);

            flipCard(cardBack, cardView, pickBtn, i * 200);
        }
    }

    private void flipCard(Region back, CardView front, Button pickBtn, int delayMs) {
        Timeline flipOut = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(back.scaleXProperty(), 1.0, Interpolator.EASE_BOTH)),
            new KeyFrame(Duration.millis(250), new KeyValue(back.scaleXProperty(), 0.0, Interpolator.EASE_IN))
        );

        Timeline flipIn = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(front.scaleXProperty(), 0.0, Interpolator.EASE_OUT)),
            new KeyFrame(Duration.millis(250), new KeyValue(front.scaleXProperty(), 1.0, Interpolator.EASE_BOTH))
        );

        flipOut.setDelay(Duration.millis(delayMs + 300));
        flipOut.setOnFinished(e -> {
            back.setVisible(false);
            flipIn.play();
        });
        flipIn.setOnFinished(e -> {
            pickBtn.setVisible(true);
            FadeTransition btnFade = new FadeTransition(Duration.millis(200), pickBtn);
            btnFade.setFromValue(0); btnFade.setToValue(1);
            btnFade.play();
        });

        flipOut.play();
    }

    private void animateNewRound() {
        FadeTransition ft = new FadeTransition(Duration.millis(200), choicesBox);
        ft.setFromValue(1); ft.setToValue(0);
        ft.setOnFinished(e -> {
            showNextRound();
            FadeTransition fi = new FadeTransition(Duration.millis(200), choicesBox);
            fi.setFromValue(0); fi.setToValue(1);
            fi.play();
        });
        ft.play();
    }

    private void updateDeckPreview() {
        deckPreviewRow.getChildren().clear();
        List<Card> drafted = draftEngine.getDraftedCards();
        counterLabel.setText(drafted.size() + " / " + draftEngine.getDeckSize());
        for (Card c : drafted) {
            CardView mini = new CardView(c, CardView.Size.SMALL);
            mini.getStyleClass().add("deck-preview-card");
            deckPreviewRow.getChildren().add(mini);
        }
    }
}
