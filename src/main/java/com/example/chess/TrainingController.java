package com.example.chess;

import com.example.chess.model.Moves;
import com.example.chess.model.Rules;
import com.example.chess.model.pieces.*;

import java.util.ArrayList;

public class TrainingController extends Controller{

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

        allPosMoves = rules.getAllPosMoves(players[activePlayer], board);
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
        AI ai = (AI) players[activePlayer];
        Moves moves = ai.play(board, allPosMoves);
        move(moves);
        changeActivePlayer();
    }

    public boolean isDone(){
        return !gameOn;
    }

    public double getPoints(){
        return pointsForPieces()*1 + pointsForWin()*1 - pointsForDuration()*1;
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

    public double pointsForDuration(){
        double points = 0;

        points += lastMoves.size();

        return points;
    }

    public ArrayList<Moves> getLastMoves(){
        return lastMoves;
    }

    public AI getMutatedAI(){
        return (AI) players[mutatedPlayer];
    }
}