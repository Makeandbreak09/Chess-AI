package com.example.chess.model.pieces;

import com.example.chess.Player;

public class Bishop extends Piece{

    public Bishop(Player player, int id){
        super(player, id);
        this.value = 3;
    }
}
