package com.example.chess;

import com.example.chess.model.Rules;
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

import java.util.ArrayList;

public class GameController {

    @FXML
    private Button button;
    @FXML
    private GridPane grid;

    private Piece[][] board;
    private Piece draggedPiece;
    private int[] draggedPos = new int[2];
    private ArrayList<int[]> posPos;

    private Rules rules;

    public GameController() {

    }

    @FXML
    public void initialize() {
        board = new Piece[8][8];
        rules = new Rules(board);

        addGridEvent();
        setUpBoard();
    }

    private void setUpBoard(){
        board[0][0] = new Rock(true);
        board[1][0] = new Knight(true);
        board[2][0] = new Bishop(true);
        board[3][0] = new Queen(true);
        board[4][0] = new King(true);
        board[5][0] = new Bishop(true);
        board[6][0] = new Knight(true);
        board[7][0] = new Rock(true);
        for(int i = 0; i<board[1].length; i++){
            board[i][1] = new Pawn(true);
        }

        board[0][7] = new Rock(false);
        board[1][7] = new Knight(false);
        board[2][7] = new Bishop(false);
        board[3][7] = new Queen(false);
        board[4][7] = new King(false);
        board[5][7] = new Bishop(false);
        board[6][7] = new Knight(false);
        board[7][7] = new Rock(false);
        for(int i = 0; i<board[6].length; i++){
            board[i][6] = new Pawn(false);
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
                        int[] p = {x,y};
                        for(int i = 0; posPos!=null&&i<posPos.size(); i++) {
                            if (posPos.get(i)[0] == p[0] && posPos.get(i)[1] == p[1]) {
                                board[draggedPos[0]][draggedPos[1]] = null;
                                board[x][y] = draggedPiece;
                                break;
                            }
                        }
                        draggedPiece.setHighlighted(false);
                        draggedPiece = null;
                        posPos = null;
                    }else if(draggedPiece == null && board[x][y] != null){
                        draggedPiece = board[x][y];
                        draggedPiece.setHighlighted(true);
                        draggedPos[0] = x;
                        draggedPos[1] = y;
                        posPos = rules.getPosPos(draggedPiece, draggedPos);
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

                //Setze die Bilder der Pieces
                if(board[i][j] != null) {
                    ImageView imageView = new ImageView(getPieceImage(board[i][j]));
                    imageView.setFitWidth(grid.getWidth() / 8);
                    imageView.setFitHeight(grid.getHeight() / 8);

                    b.getChildren().add(imageView);
                    //Markiere markiertes Piece
                    if (board[i][j].isHighlighted()) {
                        b.setStyle("-fx-border-style: solid inside; -fx-border-width: 3; -fx-border-color: rgb(225, 255, 0);");
                    }
                }
                grid.add(b, i, 8 - j);

                //Markiere mögliche Moves
                for(int k = 0; posPos != null && k<posPos.size(); k++) {
                    if (posPos.get(k)[0] == i && posPos.get(k)[1] == j) {
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