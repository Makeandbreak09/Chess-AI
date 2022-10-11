package com.example.chess.model;

import java.util.ArrayList;

public class Moves {

    ArrayList<Move> moves = new ArrayList<>();

    public Moves(Move move){
        moves.add(move);
    }

    public void addMove(Move move){
        moves.add(move);
    }

    public ArrayList<Move> getMoves(){
        return moves;
    }
}
