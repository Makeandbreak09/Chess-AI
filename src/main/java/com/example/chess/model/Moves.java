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

    public boolean equals(Moves moves){
        for(int i = 0; i<this.moves.size() || i<moves.moves.size(); i++){
            if(this.moves.get(i) == null || moves.moves.get(i) == null || !this.moves.get(i).equals(moves.moves.get(i))){
                return false;
            }
        }
        return true;
    }
}
