package com.example.chess.model.pieces;

import com.example.chess.Player;

public class Piece {

    protected boolean white;
    protected boolean highlighted;
    protected int moves = 0;
    protected Player player;

    public Piece(boolean white, Player player){
        this.white = white;
        this.player = player;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public int getMoves() {
        return moves;
    }

    public void addMove(){
        moves++;
    }

    public Player getPlayer() {
        return player;
    }
}
