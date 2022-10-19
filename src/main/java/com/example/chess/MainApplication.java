package com.example.chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private AllGames allGames;

    private Stage stage;

    private GameController gameController;
    private StartMenuController startMenuController;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.stage.setTitle("Chess");
        this.stage.getIcons().add(new Image("com/example/chess/images/icon.png"));
        this.allGames = new AllGames(this, stage);

        startStartMenuView();
    }

    public static void main(String[] args) {
        launch();
    }

    public void startStartMenuView(){
        try {
            stage.close();

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

    public void startTraining(int generations, int population){
        allGames.startGames(generations, population);
    }
}