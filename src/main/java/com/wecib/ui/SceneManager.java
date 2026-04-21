package com.wecib.ui;

import com.wecib.engine.BattleEngine;
import com.wecib.engine.DraftEngine;
import com.wecib.engine.PlayerState;
import javafx.scene.layout.StackPane;

public class SceneManager {

    private final StackPane root;
    private PlayerState player;
    private PlayerState opponent;

    public SceneManager(StackPane root) {
        this.root = root;
    }

    public void showTitleScreen() {
        root.getChildren().clear();
        TitleScreen title = new TitleScreen(this::startDraft);
        root.getChildren().add(title);
    }

    private void startDraft() {
        player = new PlayerState("Player");
        opponent = new PlayerState("Rival");
        DraftEngine draftEngine = new DraftEngine(player, opponent);

        root.getChildren().clear();
        DraftScreen draftScreen = new DraftScreen(draftEngine, v -> startBattle());
        root.getChildren().add(draftScreen);
    }

    private void startBattle() {
        BattleEngine battleEngine = new BattleEngine(player, opponent);

        root.getChildren().clear();
        BattleScreen battleScreen = new BattleScreen(battleEngine, this::showGameOver);
        root.getChildren().add(battleScreen);
    }

    private void showGameOver(boolean won) {
        root.getChildren().clear();
        GameOverScreen gameOver = new GameOverScreen(won, player.getName(), this::showTitleScreen);
        root.getChildren().add(gameOver);
    }
}
