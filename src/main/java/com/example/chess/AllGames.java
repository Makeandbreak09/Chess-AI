package com.example.chess;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AllGames {

    private MainApplication mainApplication;
    private Stage stage;

    private ReplayController replayController;
    private TrainingController[] allTrainingsControllers;
    private double[] allPoints;

    public AllGames(MainApplication mainApplication, Stage stage){
        this.mainApplication = mainApplication;
        this.stage = stage;
    }

    public void startGames(int generations, int population) {
        AI ai = null;
        File file = new File(AI.PATH);
        if (file.exists()) {
            try {
                ai = AI.loadAI();
                System.out.println("--AI loaded--");
            } catch (Exception e) {
                ai = AI.setUpNeuralNetwork(new int[]{500, 500});
                throw new RuntimeException(e);
            }
        } else {
            ai = AI.setUpNeuralNetwork(new int[]{500, 500});
        }
        System.out.println("--AI ready--");

        int index = -1;

        for (int g = 0; g < generations; g++) {
            System.out.println("--Generation "+g+" --");

            allTrainingsControllers = new TrainingController[population];
            allPoints = new double[population];

            for (int i = 0; i < allTrainingsControllers.length; i++) {
                AI[] AIs = new AI[]{AI.copy(ai), AI.copy(ai)};
                AIs[0].white = true;
                AIs[1].white = false;
                int mutatedPlayer = 0;
                AIs[mutatedPlayer].mutate();
                allTrainingsControllers[i] = new TrainingController(AIs, mutatedPlayer);
                System.out.println("--Game "+i+" started--");
            }
            int finished = 0;
            while (finished < population) {
                for (int i = 0; i < allTrainingsControllers.length; i++) {
                    if (allTrainingsControllers[i].isDone() && allPoints[i] == 0) {
                        allPoints[i] = allTrainingsControllers[i].getPoints();
                        finished++;
                        System.out.println("--Game finished--");
                    }
                }
            }

            index = 0;
            System.out.println(allPoints[index]);
            for (int i = 1; i < allPoints.length; i++) {
                System.out.println(allPoints[i]);
                if (allPoints[i] > allPoints[index]) {
                    index = i;
                }
            }

            ai = allTrainingsControllers[index].getMutatedAI();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("replay-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            stage.setScene(scene);
            stage.show();

            replayController = fxmlLoader.getController();
            replayController.setMainApplication(mainApplication);
            replayController.setPlayerData();
            replayController.setUpBoard();
            replayController.setMoves(allTrainingsControllers[index].getLastMoves());
            replayController.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AI.saveAI(ai);
        System.out.println("--AI saved--");
    }
}
