package com.wecib;

import com.wecib.ui.SceneManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.getStyleClass().add("root-pane");

        SceneManager sceneManager = new SceneManager(root);

        Scene scene = new Scene(root, 1280, 900);
        scene.getStylesheets().add(
                getClass().getResource("/com/wecib/style.css").toExternalForm()
        );

        primaryStage.setTitle("The WECIB Card Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        sceneManager.showTitleScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
