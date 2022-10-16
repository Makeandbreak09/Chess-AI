package com.example.chess;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class StartMenuController {

    private MainApplication mainApplication;

    @FXML
    private Button startGame;
    @FXML
    private Button trainAI;
    @FXML
    private Button exit;
    @FXML
    private ChoiceBox choiceBox;

    public StartMenuController(){

    }

    @FXML
    public void initialize() {
        addButtonListeners();
    }

    public void setMainApplication(MainApplication mainApplication){
        this.mainApplication = mainApplication;
    }

    private void addButtonListeners() {
        choiceBox.setItems(FXCollections.observableArrayList(
                GameController.PvP, GameController.PvE_white, GameController.PvE_black)
        );
        choiceBox.getSelectionModel().select(0);

        startGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String s = (String) choiceBox.getSelectionModel().getSelectedItem();
                if(s != null && !s.isEmpty()) {
                    mainApplication.startGameView(s);
                }
            }
        });
        trainAI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApplication.startGameView(GameController.EvE);
            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) exit.getScene().getWindow();
                stage.close();
            }
        });
    }
}
