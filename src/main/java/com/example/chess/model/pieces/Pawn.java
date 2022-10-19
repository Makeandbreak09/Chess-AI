package com.example.chess.model.pieces;

import com.example.chess.Player;

public class Pawn extends Piece{

    public Pawn(Player player, int id){
        super(player, id);
        this.value = 1;
    }

    public static Queen convertToQueen(Pawn pawn){
        Queen o = new Queen(pawn.getPlayer(), pawn.id+1);

        return o;
    }

    public static Rook convertToRook(Pawn pawn){
        Rook o = new Rook(pawn.getPlayer(), pawn.id+2);

        return o;
    }

    public static Bishop convertToBishop(Pawn pawn){
        Bishop o = new Bishop(pawn.getPlayer(), pawn.id+2);

        return o;
    }

    public static Knight convertToKnight(Pawn pawn){
        Knight o = new Knight(pawn.getPlayer(), pawn.id+2);

        return o;
    }
}
