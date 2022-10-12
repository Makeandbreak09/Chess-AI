package com.example.chess;

import com.example.chess.model.Moves;
import com.example.chess.model.Rules;
import com.example.chess.model.pieces.Piece;

import java.util.ArrayList;

public class AI extends Player{

    private Rules rules;
    private Piece[][] board;

    public AI(boolean isWhite, Rules rules, Piece[][] board){
        super(isWhite);
        this.rules = rules;
        this.board = board;
    }

    public Moves play(ArrayList<Moves> allPosMoves){
        Moves o = null;
        if(!allPosMoves.isEmpty()){
            o = allPosMoves.get((int)(Math.random()*allPosMoves.size()));
        }

        return o;
    }
}
