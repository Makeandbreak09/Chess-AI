package com.example.chess.model.pieces;

import com.example.chess.Player;

public class Queen extends Piece{

    public Queen(Player player, int id){
        super(player, id);
        this.value = 9;
    }

    public static Queen convert(Pawn pawn){
        Queen o = new Queen(pawn.getPlayer(), pawn.id);

        return o;
    }
}
