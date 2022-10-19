package com.example.chess.model.pieces;

import com.example.chess.Player;

public abstract class Piece {

    protected boolean highlighted;
    protected int moves = 0;
    protected Player player;
    protected int id;
    protected int value;

    public Piece(Player player, int id){
        this.player = player;
        this.id = id;
    }

    public boolean isWhite() {
        return player.isWhite();
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

    public int getValue(){
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
