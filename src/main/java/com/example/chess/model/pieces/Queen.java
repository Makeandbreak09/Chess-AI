package com.example.chess.model.pieces;

import com.example.chess.Player;

public class Queen extends Piece{

    public Queen(Player player, int id){
        super(player, id);
        this.value = 9;
    }
}
