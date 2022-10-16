package com.example.chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private Stage stage;

    private GameController gameController;
    private StartMenuController startMenuController;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.stage.setTitle("Chess");

        startStartMenuView();
    }

    public static void main(String[] args) {
        launch();
    }

    public void startStartMenuView(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("start-menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            stage.setScene(scene);
            stage.show();

            startMenuController = fxmlLoader.getController();
            startMenuController.setMainApplication(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGameView(String playerData){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("game-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            stage.setScene(scene);
            stage.show();

            gameController = fxmlLoader.getController();
            gameController.setMainApplication(this);
            gameController.setPlayerData(playerData);
            gameController.setUpBoard();
            gameController.startThread();
            gameController.draw();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}