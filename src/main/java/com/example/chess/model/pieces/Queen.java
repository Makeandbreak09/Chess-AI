package com.example.chess.model.pieces;

import com.example.chess.Player;

public class Queen extends Piece{

    public Queen(boolean white, Player player){
        super(white, player);
    }

    public static Queen convert(Pawn pawn){
        Queen o = new Queen(pawn.isWhite(), pawn.getPlayer());

        return o;
    }
}
