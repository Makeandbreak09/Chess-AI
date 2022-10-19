package com.example.chess;

import com.example.chess.model.Moves;
import com.example.chess.model.pieces.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class ReplayController {

    private MainApplication mainApplication;

    @FXML
    private GridPane grid;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button restartButton;
    @FXML
    private Button backButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button pauseButton;

    private HBox[][] allHBoxes;
    private Image[] allImages;

    private Piece[][] board;
    private Player[] players;
    private int activePlayer;
    private ArrayList<Moves> lastMoves;
    private int winner;
    private boolean pause;

    public ReplayController() {

    }

    @FXML
    public void initialize() {
        loadHBoxes();
        loadImages();

        board = new Piece[8][8];
        players = new Player[2];
        activePlayer = 0;
        winner = -1;

        addEvents();
    }

    private void restart(){
        initialize();

        setPlayerData();
        setUpBoard();
        draw();
    }

    public void setMainApplication(MainApplication mainApplication){
        this.mainApplication = mainApplication;
    }

    public void setPlayerData(){
        players[0] = new Player();
        players[1] = new Player();

        players[0].white = true;
        players[1].white = false;
    }

    public void setUpBoard(){
        board[0][0] = new Rook(players[0], 0);
        board[1][0] = new Knight(players[0], 0);
        board[2][0] = new Bishop(players[0], 0);
        board[3][0] = new Queen(players[0], 0);
        board[4][0] = new King(players[0], 0);
        board[5][0] = new Bishop(players[0], 1);
        board[6][0] = new Knight(players[0], 1);
        board[7][0] = new Rook(players[0], 1);
        for(int i = 0; i<board[1].length; i++){
            board[i][1] = new Pawn(players[0], i);
        }

        board[0][7] = new Rook(players[1], 0);
        board[1][7] = new Knight(players[1], 0);
        board[2][7] = new Bishop(players[1], 0);
        board[3][7] = new Queen(players[1], 0);
        board[4][7] = new King(players[1], 0);
        board[5][7] = new Bishop(players[1], 1);
        board[6][7] = new Knight(players[1], 1);
        board[7][7] = new Rook(players[1], 1);
        for(int i = 0; i<board[6].length; i++){
            board[i][6] = new Pawn(players[1], i);
        }
    }

    public void setMoves(ArrayList<Moves> allMoves){
        this.lastMoves = allMoves;
    }

    public void start(){
        draw();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<lastMoves.size(); i++){
                    move(lastMoves.get(i));

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            draw();
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void addEvents() {
        //Sets Button Listeners
        mainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainApplication.startStartMenuView();
            }
        });
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        forwardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    private void move(Moves moves){
        for (int j = 0; j < moves.getMoves().size(); j++) {
            board[moves.getMoves().get(j).getOldPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
            board[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getNewPos()[1]] = moves.getMoves().get(j).getNewPiece();

            if(moves.isEnPassant()){
                board[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
            }

            moves.getMoves().get(j).getNewPiece().addMove();
        }
    }

    private void gameOver(){
        Alert alert = new Alert(Alert.AlertType.WARNING);

        if(winner == 0) {
            alert.setContentText("White wins!");
        }else if(winner == 1){
            alert.setContentText("Black wins!");
        }else{
            alert.setContentText("Draw!");
        }
        alert.show();
    }

    public void draw(){
        grid.getChildren().clear();
        grid.getStyleClass().clear();
        if(activePlayer == 0){
            grid.getStyleClass().add("white");
        }else if(activePlayer == 1){
            grid.getStyleClass().add("black");
        }

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                HBox b = allHBoxes[i][j];
                b.getChildren().clear();
                b.setStyle("");

                b.setMinSize(grid.getWidth()/8, grid.getHeight()/8);
                b.setAlignment(Pos.CENTER);
                b.setPadding(Insets.EMPTY);

                //Setze die Bilder der Pieces
                if(board[i][j] != null) {
                    ImageView imageView = new ImageView(getPieceImage(board[i][j]));
                    imageView.setFitWidth(grid.getWidth() / 8);
                    imageView.setFitHeight(grid.getHeight() / 8);

                    b.getChildren().add(imageView);
                    //Markiere markiertes Piece
                    if (board[i][j] != null && board[i][j].isHighlighted()) {
                        b.setStyle("-fx-border-style: solid inside; -fx-border-width: 3; -fx-border-color: rgb(225, 255, 0);");
                    }
                }

                grid.add(b, i, 8 - j);
            }
        }
    }

    private void loadHBoxes(){
        allHBoxes = new HBox[8][8];
        for(int i = 0; i < allHBoxes.length; i++) {
            for (int j = 0; j < allHBoxes[i].length; j++) {
                allHBoxes[i][j] = new HBox();
            }
        }
    }

    private void loadImages(){
        allImages = new Image[12];

        allImages[0] = new Image("com/example/chess/images/pieces/whiteKing.png");
        allImages[1] = new Image("com/example/chess/images/pieces/whiteQueen.png");
        allImages[2] = new Image("com/example/chess/images/pieces/whiteRook.png");
        allImages[3] = new Image("com/example/chess/images/pieces/whiteBishop.png");
        allImages[4] = new Image("com/example/chess/images/pieces/whiteKnight.png");
        allImages[5] = new Image("com/example/chess/images/pieces/whitePawn.png");

        allImages[6] = new Image("com/example/chess/images/pieces/blackKing.png");
        allImages[7] = new Image("com/example/chess/images/pieces/blackQueen.png");
        allImages[8] = new Image("com/example/chess/images/pieces/blackRook.png");
        allImages[9] = new Image("com/example/chess/images/pieces/blackBishop.png");
        allImages[10] = new Image("com/example/chess/images/pieces/blackKnight.png");
        allImages[11] = new Image("com/example/chess/images/pieces/blackPawn.png");
    }

    private Image getPieceImage(Piece piece){
        if(piece != null) {
            if (piece.isWhite()) {
                switch (piece.getClass().getSimpleName()) {
                    case "King":
                        return allImages[0];
                    case "Queen":
                        return allImages[1];
                    case "Rook":
                        return allImages[2];
                    case "Bishop":
                        return allImages[3];
                    case "Knight":
                        return allImages[4];
                    case "Pawn":
                        return allImages[5];
                    default:
                        return null;
                }
            } else {
                switch (piece.getClass().getSimpleName()) {
                    case "King":
                        return allImages[6];
                    case "Queen":
                        return allImages[7];
                    case "Rook":
                        return allImages[8];
                    case "Bishop":
                        return allImages[9];
                    case "Knight":
                        return allImages[10];
                    case "Pawn":
                        return allImages[11];
                    default:
                        return null;
                }
            }
        }else{
            return null;
        }
    }
}