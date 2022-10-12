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

    public Moves play(){
        ArrayList<Moves> allPosMoves = new ArrayList<>();
        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                if(board[i][j] != null && board[i][j].isWhite()==this.isWhite){
                    allPosMoves.addAll(rules.getPosPos(board[i][j], new int[]{i, j}));
                }
            }
        }

        Moves o = null;
        if(!allPosMoves.isEmpty()){
            o = allPosMoves.get((int)(Math.random()*allPosMoves.size()));
        }

        return o;
    }
}
