package com.example.chess.model.pieces;

public class Piece {

    protected boolean white;
    protected boolean highlighted;
    protected int moves = 0;

    public Piece(boolean white){
        this.white = white;
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
}
