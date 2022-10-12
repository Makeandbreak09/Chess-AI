package com.example.chess;

import com.example.chess.model.Move;
import com.example.chess.model.Moves;
import com.example.chess.model.Rules;
import com.example.chess.model.pieces.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class GameController {

    private MainApplication mainApplication;

    @FXML
    private GridPane grid;

    private Piece[][] board;
    private ArrayList<Move> allMoves;
    private Piece draggedPiece;
    private int[] draggedPos = new int[2];
    private ArrayList<Moves> posMoves;

    private Rules rules;

    private int activePlayer = 0;
    private Player[] players;
    private boolean gameOn = true;

    public GameController() {

    }

    @FXML
    public void initialize() {
        board = new Piece[8][8];
        allMoves = new ArrayList<Move>();
        rules = new Rules(board);
        players = new Player[2];

        addGridEvent();
    }

    public void setMainApplication(MainApplication mainApplication){
        this.mainApplication = mainApplication;
    }

    public void setPlayerData(String playerData){
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
    }

    private void addGridEvent() {
        grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(testPlayer(event)){
                    if(gameOn) {
                        draw();
                    }else{
                        gameOver();
                    }
                }
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
        }else{
            gameOn = false;
        }
    }

    private void gameOver(){


        mainApplication.startStartMenuView();
    }

    private void changeActivePlayer(){
        if(activePlayer == 0){
            activePlayer = 1;
        }else{
            activePlayer = 0;
        }
    }

    private boolean testComputer(){
        if(players[activePlayer] != null && players[activePlayer].getClass().getSimpleName().equals("AI")){
            AI ai = (AI) players[activePlayer];
            move(ai.play());
            changeActivePlayer();

            return true;
        }
        return false;
    }

    private boolean testPlayer(MouseEvent event){
        if(players[activePlayer].getClass().getSimpleName().equals("Player")) {
            int x = (int) ((event.getX()) / (grid.getWidth() / board.length));
            int y = (int) ((grid.getHeight() - event.getY()) / (grid.getHeight() / board[0].length));
            if (x > -1 && x < 8 && y > -1 && y < 8) {
                handleClick(x, y);
            }
            return true;
        }
        return false;
    }

    public void handleClick(int x, int y){
        if(draggedPiece!=null) {
            int[] p = {x,y};
            for(int i = 0; posMoves!=null&&i<posMoves.size(); i++) {
                if (posMoves.get(i).getMoves().get(0).getNewPos()[0] == p[0] && posMoves.get(i).getMoves().get(0).getNewPos()[1] == p[1]) {
                    Moves moves = posMoves.get(i);
                    move(moves);
                    changeActivePlayer();
                    break;
                }
            }
            draggedPiece.setHighlighted(false);
            draggedPiece = null;
            posMoves = null;
        }else if(draggedPiece == null && board[x][y] != null){
            if(board[x][y].getPlayer() == players[activePlayer]) {
                draggedPiece = board[x][y];
                draggedPiece.setHighlighted(true);
                draggedPos[0] = x;
                draggedPos[1] = y;
                posMoves = rules.getPosPos(draggedPiece, draggedPos);
            }
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

                //Markiere mögliche Moves
                for(int k = 0; posMoves != null && k<posMoves.size(); k++) {
                    if (posMoves.get(k).getMoves().get(0).getNewPos()[0] == i && posMoves.get(k).getMoves().get(0).getNewPos()[1] == j) {
                        b.setStyle("-fx-background-color: rgb(0, 255, 0 , 0.2)");
                    }
                }
            }
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