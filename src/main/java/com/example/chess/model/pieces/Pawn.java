package com.example.chess.model.pieces;

import com.example.chess.Player;

public class Pawn extends Piece{

    public Pawn(Player player, int id){
        super(player, id);
        this.value = 1;
    }
}
