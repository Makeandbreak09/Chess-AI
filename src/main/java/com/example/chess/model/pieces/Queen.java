package com.example.chess.model.pieces;

public class Queen extends Piece{

    public Queen(boolean white){
        super(white);
    }

    public static Queen convert(Pawn pawn){
        Queen o = new Queen(pawn.isWhite());

        return o;
    }
}
