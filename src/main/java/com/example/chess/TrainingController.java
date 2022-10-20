package com.example.chess;

import com.example.chess.model.Moves;
import com.example.chess.model.Rules;
import com.example.chess.model.pieces.*;

import java.util.ArrayList;

public class TrainingController {

    private Piece[][] board;
    private AI[] players;
    private int activePlayer;
    private int mutatedPlayer;
    private ArrayList<Moves> lastMoves;
    private ArrayList<Moves> allPosMoves;
    private boolean gameOn;
    private int winner;
    private Thread thread;

    private Rules rules;

    public TrainingController(AI[] players, int mutatedPlayer) {
        board = new Piece[8][8];
        this.players = players;
        activePlayer = 0;
        this.mutatedPlayer = mutatedPlayer;
        lastMoves = new ArrayList<Moves>();
        allPosMoves = new ArrayList<>();
        gameOn = true;
        winner = -1;

        rules = new Rules(lastMoves);

        setUpBoard();
        startThread();
    }

    public void startThread(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (gameOn) {
                    testComputer();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void setUpBoard(){
        board[0][0] = new Rook(players[0], 0);
        board[1][0] = new Knight(players[0], 0);
        board[2][0] = new Bishop(players[0], 0);
        board[3][0] = new Queen(players[0], 0);
        board[4][0] = new King(players[0], 0);
        board[5][0] = new Bishop(players[0], 1);
        board[6][0] = new Knight(players[0], 1);
        board[7][0] = new Rook(players[0], 1);
        for(int i = 0; i<board[1].length; i++){
            board[i][1] = new Pawn(players[0], i);
        }

        board[0][7] = new Rook(players[1], 0);
        board[1][7] = new Knight(players[1], 0);
        board[2][7] = new Bishop(players[1], 0);
        board[3][7] = new Queen(players[1], 0);
        board[4][7] = new King(players[1], 0);
        board[5][7] = new Bishop(players[1], 1);
        board[6][7] = new Knight(players[1], 1);
        board[7][7] = new Rook(players[1], 1);
        for(int i = 0; i<board[6].length; i++){
            board[i][6] = new Pawn(players[1], i);
        }

        allPosMoves = rules.getAllPosMoves(players[0], board);
    }

    private void move(Moves moves){
        if(moves != null) {
            for (int j = 0; j < moves.getMoves().size(); j++) {
                board[moves.getMoves().get(j).getOldPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
                board[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getNewPos()[1]] = moves.getMoves().get(j).getNewPiece();

                if(moves.isEnPassant()){
                    board[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
                }

                moves.getMoves().get(j).getNewPiece().addMove();
            }
            if(rules.lastMovesSame(lastMoves, 3) || rules.lastMovesCapture(lastMoves, 50)){
                gameOn = false;
            }
            lastMoves.add(moves);
        }
    }

    private void changeActivePlayer(){
        if(activePlayer == 0){
            activePlayer = 1;
        }else{
            activePlayer = 0;
        }

        allPosMoves = rules.getAllPosMoves(players[activePlayer], board);
        if(allPosMoves.isEmpty()){
            if(activePlayer == 0){
                winner = 1;
            }else{
                winner = 0;
            }
            gameOn = false;
        }
    }

    private void testComputer(){
        AI ai = players[activePlayer];
        Moves moves = ai.play(board, allPosMoves);
        move(moves);
        changeActivePlayer();
    }

    public boolean isDone(){
        return !gameOn;
    }

    public double getPoints(){
        return pointsForPieces()*1 + pointsForWin()*1;
    }

    public double pointsForWin(){
        double points = 0;

        if(winner == mutatedPlayer){
            points += 200;
        }else if(winner == -1){
            points -=10;
        }else{
            points -= 200;
        }

        return points;
    }

    public double pointsForPieces(){
        double points = 0;

        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                if(board[i][j] != null && !board[i][j].getClass().getSimpleName().equals("King")){
                    if(board[i][j].getPlayer() == players[mutatedPlayer]){
                        points += board[i][j].getValue();
                    }else{
                        points -= board[i][j].getValue()*1.3;
                    }
                }
            }
        }

        return points;
    }

    public ArrayList<Moves> getLastMoves(){
        return lastMoves;
    }

    public AI getMutatedAI(){
        return players[mutatedPlayer];
    }

    public void saveAI(){
        AI.saveAI(players[mutatedPlayer]);
    }
}