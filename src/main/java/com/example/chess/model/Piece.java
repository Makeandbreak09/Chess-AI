package com.example.chess.model;

public class Piece {

    protected boolean white;

    public Piece(boolean white){
        this.white = white;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }
}
