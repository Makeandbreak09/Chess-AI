package com.example.chess;

import com.example.chess.model.pieces.*;
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

public class GameController {

    @FXML
    private Button button;
    @FXML
    private GridPane grid;

    private Piece[][] board;
    private Piece draggedPiece;
    private int[] draggedPos = new int[2];

    public GameController() {

    }

    @FXML
    public void initialize() {
        board = new Piece[8][8];

        addGridEvent();
        setUpBoard();
    }

    private void setUpBoard(){
        board[0][0] = new Rock(true);
        board[0][1] = new Knight(true);
        board[0][2] = new Bishop(true);
        board[0][3] = new Queen(true);
        board[0][4] = new King(true);
        board[0][5] = new Bishop(true);
        board[0][6] = new Knight(true);
        board[0][7] = new Rock(true);
        for(int i = 0; i<board[1].length; i++){
            board[1][i] = new Pawn(true);
        }

        board[7][0] = new Rock(false);
        board[7][1] = new Knight(false);
        board[7][2] = new Bishop(false);
        board[7][3] = new Queen(false);
        board[7][4] = new King(false);
        board[7][5] = new Bishop(false);
        board[7][6] = new Knight(false);
        board[7][7] = new Rock(false);
        for(int i = 0; i<board[6].length; i++){
            board[6][i] = new Pawn(false);
        }
    }

    private void addGridEvent() {
        grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int)((event.getX())/(grid.getWidth()/board.length));
                int y = (int)((grid.getHeight()-event.getY())/(grid.getHeight()/board[0].length));
                if(x>-1&&x<8&&y>-1&&y<8) {
                    if(draggedPiece!=null) {
                        if(board[y][x]==null || board[y][x].isWhite() != draggedPiece.isWhite()) {
                            board[draggedPos[1]][draggedPos[0]] = null;
                            board[y][x] = draggedPiece;
                        }
                        draggedPiece.setHighlighted(false);
                        draggedPiece = null;
                    }else if(draggedPiece == null && board[y][x] != null){
                        draggedPiece = board[y][x];
                        draggedPiece.setHighlighted(true);
                        draggedPos[0] = x;
                        draggedPos[1] = y;
                    }
                }

                draw();
            }
        });
    }

    public void draw(){
        grid.getChildren().clear();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                HBox b = new HBox();
                b.setMinSize(grid.getWidth()/8, grid.getHeight()/8);
                b.setAlignment(Pos.CENTER);
                b.setPadding(Insets.EMPTY);

                if(board[i][j] != null) {
                    ImageView imageView = new ImageView(getPieceImage(board[i][j]));
                    imageView.setFitWidth(grid.getWidth() / 8);
                    imageView.setFitHeight(grid.getHeight() / 8);

                    b.getChildren().add(imageView);
                    if (board[i][j].isHighlighted()) {
                        b.setStyle("-fx-border-style: solid inside; -fx-border-width: 3; -fx-border-color: rgb(225, 255, 0);");
                    }
                }
                grid.add(b, j, 8 - i);
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