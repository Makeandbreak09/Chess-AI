package com.example.chess;

public class Player {

    protected boolean white;

    public Player(boolean white){
        this.white = white;
    }

    public boolean isWhite(){
        return white;
    }
}
