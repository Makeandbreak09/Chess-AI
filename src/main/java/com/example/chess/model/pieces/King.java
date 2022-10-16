package com.example.chess.model.pieces;

import com.example.chess.Player;

public class King extends Piece{

    public King(Player player, int id){
        super(player, id);
        this.value = 9999;
    }
}
