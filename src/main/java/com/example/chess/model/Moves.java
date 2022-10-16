package com.example.chess.model;

import java.util.ArrayList;

public class Moves {

    private ArrayList<Move> moves = new ArrayList<>();

    private boolean capture;
    private boolean enPassant;
    private int rochadeType;

    public Moves(Move move, boolean capture, boolean enPassant){
        moves.add(move);
        this.capture = capture;
        this.enPassant = enPassant;
        this.rochadeType = 0;
    }

    public Moves(ArrayList<Move> moves){
        this.moves = moves;
        this.capture = false;
        if(moves.get(1).getOldPos()[0] == 0) {
            this.rochadeType = 1;
        }else{
            this.rochadeType = 2;
        }
    }

    public ArrayList<Move> getMoves(){
        return moves;
    }

    public boolean isCapture() {
        return capture;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public int getRochadeType() {
        return rochadeType;
    }

    public boolean equals(Moves moves){
        for(int i = 0; i<this.moves.size() || i<moves.moves.size(); i++){
            if(this.moves.get(i) == null || moves.moves.get(i) == null || !this.moves.get(i).equals(moves.moves.get(i))){
                return false;
            }
        }
        return true;
    }
}
