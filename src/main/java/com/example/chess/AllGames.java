package com.example.chess;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AllGames {

    public static final int MAX_RUNNING = 5;

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

        TrainingController best = null;
        for (int g = 0; g < generations; g++) {
            System.out.println("--Generation "+g+" --");

            allTrainingsControllers = new TrainingController[population];
            allPoints = new double[population];
            for(int i = 0; i<allPoints.length; i++){
                allPoints[i] = Double.MIN_VALUE;
            }
            int mutatedPlayer = (int)(Math.random()*2);

            int finished = 0;
            int running = 0;
            best = null;
            while(finished < population) {
                for (int i = 0; i<finished+MAX_RUNNING && i < allTrainingsControllers.length; i++) {
                    if(running<finished+MAX_RUNNING && allPoints[i] == Double.MIN_VALUE && allTrainingsControllers[i] == null) {
                        AI[] AIs = new AI[]{AI.copy(ai), AI.copy(ai)};
                        AIs[0].white = true;
                        AIs[1].white = false;
                        AIs[mutatedPlayer].mutate();
                        allTrainingsControllers[i] = new TrainingController(AIs, mutatedPlayer);
                        running++;
                        System.out.println("--Game " + i + " started--");
                    }

                    if (allTrainingsControllers[i] != null && allTrainingsControllers[i].isDone() && allPoints[i] == Double.MIN_VALUE) {
                        allPoints[i] = allTrainingsControllers[i].getPoints();
                        if (best == null || best.getPoints() < allTrainingsControllers[i].getPoints()) {
                            best = allTrainingsControllers[i];
                        }
                        allTrainingsControllers[i] = null;
                        finished++;
                        running--;
                        System.out.println("--Game " + i + " finished--");
                    }
                }
            }

            for (int i = 0; i < allPoints.length; i++) {
                System.out.println("--Points: "+allPoints[i]);
            }

            ai = best.getMutatedAI();
        }

        AI.saveAI(ai);
        System.out.println("--AI saved--");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("replay-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            stage.setScene(scene);
            stage.show();

            replayController = fxmlLoader.getController();
            replayController.setMainApplication(mainApplication);
            replayController.setPlayerData();
            replayController.setUpBoard();
            replayController.setMoves(best.getLastMoves());
            replayController.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
