package com.example.chess.model.pieces;

import com.example.chess.Player;

public class Rook extends Piece{

    public Rook(Player player, int id){
        super(player, id);
        this.value = 5;
    }
}
