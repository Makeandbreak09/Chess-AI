package com.example.chess.model;

import com.example.chess.model.pieces.Piece;

import java.util.Arrays;

public class Move {

    private Piece oldPiece;
    private Piece newPiece;
    private int[] oldPos;
    private int[] newPos;

    public Move(Piece oldPiece, int[] oldPos, int[] newPos){
        this.oldPiece = oldPiece;
        this.newPiece = oldPiece;
        this.oldPos = oldPos;
        this.newPos = newPos;
    }

    public Piece getOldPiece() {
        return oldPiece;
    }

    public Piece getNewPiece() {
        return newPiece;
    }

    public void setNewPiece(Piece newPiece){
        this.newPiece = newPiece;
    }

    public int[] getOldPos() {
        return oldPos;
    }

    public int[] getNewPos() {
        return newPos;
    }

    public boolean equals(Move move){
        if(this.oldPiece == move.oldPiece && this.newPiece == move.newPiece && Arrays.equals(this.oldPos, move.oldPos) && Arrays.equals(this.newPos, move.newPos)){
            return true;
        }
        return false;
    }
}
