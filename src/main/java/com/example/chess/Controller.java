package com.example.chess;

import com.example.chess.model.pieces.*;

public class Controller {

    protected Piece[][] board;
    protected Player[] players;

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

        board[0][7] = new Rook(players[1], 1);
        board[1][7] = new Knight(players[1], 1);
        board[2][7] = new Bishop(players[1], 1);
        board[3][7] = new Queen(players[1], 0);
        board[4][7] = new King(players[1], 0);
        board[5][7] = new Bishop(players[1], 0);
        board[6][7] = new Knight(players[1], 0);
        board[7][7] = new Rook(players[1], 0);
        for(int i = 0; i<board[6].length; i++){
            board[7-i][6] = new Pawn(players[1], i);
        }
    }
}
