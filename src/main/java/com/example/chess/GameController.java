package com.example.chess;

import com.example.chess.model.Move;
import com.example.chess.model.Moves;
import com.example.chess.model.Rules;
import com.example.chess.model.pieces.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;

public class GameController {

    private MainApplication mainApplication;

    @FXML
    private GridPane grid;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button restartButton;

    private Player[] players;
    private int activePlayer = 0;
    private Piece[][] board;
    private ArrayList<Moves> allMoves;
    private Piece draggedPiece;
    private int[] draggedPos = new int[2];
    private ArrayList<Moves> allPosMoves;
    private boolean gameOn = true;
    private String playerData;

    private Rules rules;

    public GameController() {

    }

    @FXML
    public void initialize() {
        board = new Piece[8][8];
        allMoves = new ArrayList<Moves>();
        rules = new Rules();
        players = new Player[2];

        addEvents();
    }

    public void setMainApplication(MainApplication mainApplication){
        this.mainApplication = mainApplication;
    }

    public void setPlayerData(String playerData){
        this.playerData = playerData;
        switch (playerData){
            case "PvP":
                players[0] = new Player(true);
                players[1] = new Player(false);
                break;
            case "PvE(white)":
                players[0] = new Player(true);
                players[1] = new AI(false, rules, board);

                startThread();
                break;
            case "PvE(black)":
                players[0] = new AI(true, rules, board);
                players[1] = new Player(false);

                startThread();
                break;
            case "EvE":
                players[0] = new AI(true, rules, board);
                players[1] = new AI(false, rules, board);

                startThread();
                break;
        }
    }

    public void startThread(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(gameOn) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if(testComputer()){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if(gameOn) {
                                    draw();
                                }else{
                                    gameOver();
                                }
                            }
                        });
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void setUpBoard(){
        board[0][0] = new Rock(true, players[0]);
        board[1][0] = new Knight(true, players[0]);
        board[2][0] = new Bishop(true, players[0]);
        board[3][0] = new Queen(true, players[0]);
        board[4][0] = new King(true, players[0]);
        board[5][0] = new Bishop(true, players[0]);
        board[6][0] = new Knight(true, players[0]);
        board[7][0] = new Rock(true, players[0]);
        for(int i = 0; i<board[1].length; i++){
            board[i][1] = new Pawn(true, players[0]);
        }

        board[0][7] = new Rock(false, players[1]);
        board[1][7] = new Knight(false, players[1]);
        board[2][7] = new Bishop(false, players[1]);
        board[3][7] = new Queen(false, players[1]);
        board[4][7] = new King(false, players[1]);
        board[5][7] = new Bishop(false, players[1]);
        board[6][7] = new Knight(false, players[1]);
        board[7][7] = new Rock(false, players[1]);
        for(int i = 0; i<board[6].length; i++){
            board[i][6] = new Pawn(false, players[1]);
        }

        allPosMoves = rules.getAllPosMoves(players[0], board);
    }

    private void addEvents() {
        //Sets grid Listener
        grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(handleClick(event)){
                    if(gameOn) {
                        draw();
                    }else{
                        gameOver();
                    }
                }
            }
        });

        //Sets Button Listeners
        mainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameOn = false;
                mainApplication.startStartMenuView();
            }
        });
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameOn = false;
                mainApplication.startGameView(playerData);
            }
        });
    }

    private void move(Moves moves){
        if(moves != null) {
            for (int j = 0; j < moves.getMoves().size(); j++) {
                board[moves.getMoves().get(j).getOldPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
                board[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getNewPos()[1]] = moves.getMoves().get(j).getNewPiece();

                moves.getMoves().get(j).getNewPiece().addMove();
            }
            if(rules.lastMovesSame(allMoves, 3) || rules.lastMovesCapture(allMoves, 30)){
                gameOn = false;
            }
        }else{
            gameOn = false;
        }
    }

    private void gameOver(){
        draw();

        //mainApplication.startStartMenuView();
    }

    private void changeActivePlayer(){
        if(activePlayer == 0){
            activePlayer = 1;
        }else{
            activePlayer = 0;
        }

        allPosMoves = rules.getAllPosMoves(players[activePlayer], board);
        if(allPosMoves.isEmpty()){
            gameOn = false;
        }
    }

    private boolean testComputer(){
        if(players[activePlayer] != null && players[activePlayer].getClass().getSimpleName().equals("AI")){
            AI ai = (AI) players[activePlayer];
            Moves moves = ai.play(allPosMoves);
            move(moves);
            allMoves.add(moves);
            changeActivePlayer();

            return true;
        }
        return false;
    }

    private boolean handleClick(MouseEvent event){
        if(gameOn && players[activePlayer] != null && players[activePlayer].getClass().getSimpleName().equals("Player")) {
            int x = (int) ((event.getX()) / (grid.getWidth() / board.length));
            int y = (int) ((grid.getHeight() - event.getY()) / (grid.getHeight() / board[0].length));
            if (x > -1 && x < 8 && y > -1 && y < 8) {
                testPlayer(new int[]{x,y});
            }
            return true;
        }
        return false;
    }

    public void testPlayer(int[] p){
        if(draggedPiece!=null) {
            for(int i = 0; allPosMoves!=null && i<allPosMoves.size(); i++) {
                if (Arrays.equals(allPosMoves.get(i).getMoves().get(0).getNewPos(), p) && allPosMoves.get(i).getMoves().get(0).getOldPiece().equals(draggedPiece)) {
                    Moves moves = allPosMoves.get(i);
                    move(moves);
                    allMoves.add(moves);
                    changeActivePlayer();
                    break;
                }
            }
            draggedPiece.setHighlighted(false);
            draggedPiece = null;
            draggedPos = null;
        }else if(board[p[0]][p[1]] != null && board[p[0]][p[1]].getPlayer() == players[activePlayer]) {
            draggedPiece = board[p[0]][p[1]];
            draggedPiece.setHighlighted(true);
            draggedPos = p;
        }
    }

    public void draw(){
        grid.getChildren().clear();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                HBox b = new HBox();
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

                //Markiere mögliche Moves vom draggedPiece
                for(int k = 0; allPosMoves != null && k<allPosMoves.size(); k++) {
                    if (draggedPos != null && Arrays.equals(allPosMoves.get(k).getMoves().get(0).getOldPos(), draggedPos) && Arrays.equals(allPosMoves.get(k).getMoves().get(0).getNewPos(), new int[]{i, j})) {
                        b.setStyle("-fx-background-color: rgb(0, 255, 0 , 0.2)");
                    }
                }
            }
        }

        grid.getStyleClass().clear();
        if(activePlayer == 0){
            grid.getStyleClass().add("white");
        }else if(activePlayer == 1){
            grid.getStyleClass().add("black");
        }
    }

    private Image getPieceImage(Piece piece){
        if(piece != null) {
            if (!piece.isWhite()) {
                switch (piece.getClass().getSimpleName()) {
                    case "Queen":
                        return new Image("com/example/chess/images/pieces/blackQueen.png");
                    case "King":
                        return new Image("com/example/chess/images/pieces/blackKing.png");
                    case "Rock":
                        return new Image("com/example/chess/images/pieces/blackRock.png");
                    case "Bishop":
                        return new Image("com/example/chess/images/pieces/blackBishop.png");
                    case "Knight":
                        return new Image("com/example/chess/images/pieces/blackKnight.png");
                    case "Pawn":
                        return new Image("com/example/chess/images/pieces/blackPawn.png");
                    default:
                        return null;
                }
            } else {
                switch (piece.getClass().getSimpleName()) {
                    case "Queen":
                        return new Image("com/example/chess/images/pieces/whiteQueen.png");
                    case "King":
                        return new Image("com/example/chess/images/pieces/whiteKing.png");
                    case "Rock":
                        return new Image("com/example/chess/images/pieces/whiteRock.png");
                    case "Bishop":
                        return new Image("com/example/chess/images/pieces/whiteBishop.png");
                    case "Knight":
                        return new Image("com/example/chess/images/pieces/whiteKnight.png");
                    case "Pawn":
                        return new Image("com/example/chess/images/pieces/whitePawn.png");
                    default:
                        return null;
                }
            }
        }else{
            return null;
        }
    }
}