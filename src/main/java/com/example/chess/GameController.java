package com.example.chess;

import com.example.chess.model.Moves;
import com.example.chess.model.Rules;
import com.example.chess.model.pieces.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;

public class GameController {

    public static final String PvP = "PvP";
    public static final String PvE_white = "PvE(white)";
    public static final String PvE_black = "PvE(black)";
    public static final String EvE = "EvE";

    private MainApplication mainApplication;

    @FXML
    private GridPane grid;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button restartButton;

    private HBox[][] allHBoxes;
    private Image[] allImages;

    private Piece[][] board;
    private Player[] players;
    private int activePlayer;
    private ArrayList<Moves> lastMoves;
    private Piece draggedPiece;
    private int[] draggedPos;
    private ArrayList<Moves> allPosMoves;
    private boolean gameOn;
    private int winner;
    private String playerData;

    private Rules rules;

    public GameController() {

    }

    @FXML
    public void initialize() {
        loadHBoxes();
        loadImages();

        board = new Piece[8][8];
        players = new Player[2];
        activePlayer = 0;
        lastMoves = new ArrayList<Moves>();
        draggedPiece = null;
        draggedPos = null;
        allPosMoves = new ArrayList<>();
        gameOn = true;
        winner = -1;

        rules = new Rules(lastMoves);

        addEvents();
    }

    private void restart(String playerData){
        initialize();

        setPlayerData(playerData);
        setUpBoard();
        startThread();

        draw();
    }

    public void setMainApplication(MainApplication mainApplication){
        this.mainApplication = mainApplication;
    }

    public void setPlayerData(String playerData){
        this.playerData = playerData;
        switch (playerData){
            case PvP:
                players[0] = new Player(true);
                players[1] = new Player(false);
                break;
            case PvE_white:
                players[0] = new Player(true);
                players[1] = new AI(false, rules, board);
                break;
            case PvE_black:
                players[0] = new AI(true, rules, board);
                players[1] = new Player(false);
                break;
            case EvE:
                players[0] = new AI(true, rules, board);
                players[1] = new AI(false, rules, board);
                break;
        }
    }

    public void startThread(){
        if(!playerData.equals(PvP)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (gameOn) {
                        if (testComputer()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (gameOn) {
                                        draw();
                                    } else {
                                        gameOver();
                                    }
                                }
                            });
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
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
                restart(playerData);
            }
        });
    }

    private void move(Moves moves){
        if(moves != null) {
            for (int j = 0; j < moves.getMoves().size(); j++) {
                board[moves.getMoves().get(j).getOldPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
                board[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getNewPos()[1]] = moves.getMoves().get(j).getNewPiece();

                if(moves.isEnPassant()){
                    board[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
                }

                moves.getMoves().get(j).getNewPiece().addMove();
            }
            if(rules.lastMovesSame(lastMoves, 3) || rules.lastMovesCapture(lastMoves, 50)){
                gameOn = false;
            }
            lastMoves.add(moves);
        }else{
            if(activePlayer == 0){
                winner = 1;
            }else{
                winner = 0;
            }
            gameOn = false;
        }
    }

    private void gameOver(){
        draw();

        if(playerData.equals(EvE)){
            restart(playerData);
        }else{
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
    }

    private void changeActivePlayer(){
        if(activePlayer == 0){
            activePlayer = 1;
        }else{
            activePlayer = 0;
        }

        allPosMoves = rules.getAllPosMoves(players[activePlayer], board);
        if(allPosMoves.isEmpty()){
            if(activePlayer == 0){
                winner = 1;
            }else{
                winner = 0;
            }
            gameOn = false;
        }
    }

    private boolean testComputer(){
        if(players[activePlayer] != null && players[activePlayer].getClass().getSimpleName().equals("AI")){
            AI ai = (AI) players[activePlayer];
            Moves moves = ai.play(allPosMoves);
            move(moves);
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
        grid.getStyleClass().clear();
        if(activePlayer == 0){
            grid.getStyleClass().add("white");
        }else if(activePlayer == 1){
            grid.getStyleClass().add("black");
        }
        grid.setEffect(null);
        if(!gameOn){
            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-0.5);
            grid.setEffect(blackout);
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

                //Markiere mögliche Moves vom draggedPiece
                for(int k = 0; allPosMoves != null && k<allPosMoves.size(); k++) {
                    if (draggedPos != null && Arrays.equals(allPosMoves.get(k).getMoves().get(0).getOldPos(), draggedPos) && Arrays.equals(allPosMoves.get(k).getMoves().get(0).getNewPos(), new int[]{i, j})) {
                        b.setStyle("-fx-background-color: rgb(0, 255, 0 , 0.2)");
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

        allImages[0] = new Image("com/example/chess/images/pieces/whiteQueen.png");
        allImages[1] = new Image("com/example/chess/images/pieces/whiteKing.png");
        allImages[2] = new Image("com/example/chess/images/pieces/whiteRook.png");
        allImages[3] = new Image("com/example/chess/images/pieces/whiteBishop.png");
        allImages[4] = new Image("com/example/chess/images/pieces/whiteKnight.png");
        allImages[5] = new Image("com/example/chess/images/pieces/whitePawn.png");

        allImages[6] = new Image("com/example/chess/images/pieces/blackQueen.png");
        allImages[7] = new Image("com/example/chess/images/pieces/blackKing.png");
        allImages[8] = new Image("com/example/chess/images/pieces/blackRook.png");
        allImages[9] = new Image("com/example/chess/images/pieces/blackBishop.png");
        allImages[10] = new Image("com/example/chess/images/pieces/blackKnight.png");
        allImages[11] = new Image("com/example/chess/images/pieces/blackPawn.png");
    }

    private Image getPieceImage(Piece piece){
        if(piece != null) {
            if (piece.isWhite()) {
                switch (piece.getClass().getSimpleName()) {
                    case "Queen":
                        return allImages[0];
                    case "King":
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
                    case "Queen":
                        return allImages[6];
                    case "King":
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