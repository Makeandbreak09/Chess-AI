package com.example.chess;

import com.example.chess.model.Piece;
import com.example.chess.model.Queen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private Button button;
    @FXML
    private GridPane grid;

    private Piece[][] board;

    public GameController() {

    }

    @FXML
    public void initialize() {
        board = new Piece[8][8];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                board[i][j] = new Queen(true);
            }
        }

        draw();
    }

    private void draw(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                ImageView imageView = new ImageView(getPieceImage(board[i][j]));
                imageView.setFitWidth(grid.getWidth()/8);
                imageView.setFitHeight(grid.getHeight()/8);
                grid.add(imageView, i, j);
            }
        }
    }

    private Image getPieceImage(Piece piece){
        if(piece != null) {
            if (piece.isWhite()) {
                switch (piece.getClass().getSimpleName()) {
                    case "Queen":
                        return new Image("com/example/chess/images/test.png");
                    default:
                        return null;
                }
            } else {
                switch (piece.getClass().getSimpleName()) {
                    case "Queen":
                        return new Image("com/example/chess/images/whiteQueen.png");
                    default:
                        return null;
                }
            }
        }else{
            return null;
        }
    }
}