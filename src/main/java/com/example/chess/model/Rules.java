package com.example.chess.model;

import com.example.chess.model.pieces.Pawn;
import com.example.chess.model.pieces.Piece;
import com.example.chess.model.pieces.Queen;

import java.util.ArrayList;

public class Rules {

    private Piece[][] board;

    public Rules(Piece[][] board){
        this.board = board;
    }

    public ArrayList<Moves> getPosPos(Piece piece, int[] pos){
        ArrayList<Moves> o = new ArrayList<Moves>();

        addAllMoves(piece, pos, o);

        return o;
    }

    private void addAllMoves(Piece piece, int[] pos, ArrayList<Moves> o){
        switch (piece.getClass().getSimpleName()) {
            case "Queen":
                addRockMoves(piece, pos, o);
                addBishopMoves(piece, pos, o);
                break;
            case "King":
                addKingMoves(piece, pos, o);
                break;
            case "Rock":
                addRockMoves(piece, pos, o);
                break;
            case "Bishop":
                addBishopMoves(piece, pos, o);
                break;
            case "Knight":
                addKnightMoves(piece, pos, o);
                break;
            case "Pawn":
                addPawnMoves(piece, pos, o);
                break;
        }
    }

    private void addRockMoves(Piece piece, int[] pos, ArrayList<Moves> o){
        //Links
        for(int i = 1; pos[0]-i>-1; i++){
            if(board[pos[0]-i][pos[1]] == null) {
                int[] newPos = {pos[0]-i, pos[1]};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
            }else if(board[pos[0]-i][pos[1]].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]-i, pos[1]};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
                break;
            }else{
                break;
            }
        }
        //Rechts
        for(int i = 1; pos[0]+i<8; i++){
            if(board[pos[0]+i][pos[1]] == null) {
                int[] newPos = {pos[0]+i, pos[1]};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
            }else if(board[pos[0]+i][pos[1]].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]+i, pos[1]};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
                break;
            }else{
                break;
            }
        }
        //Unten
        for(int i = 1; pos[1]-i>-1; i++){
            if(board[pos[0]][pos[1]-i] == null) {
                int[] newPos = {pos[0], pos[1]-i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
            }else if(board[pos[0]][pos[1]-i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0], pos[1]-i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
                break;
            }else{
                break;
            }
        }
        //Oben
        for(int i = 1; pos[1]+i<8; i++){
            if(board[pos[0]][pos[1]+i] == null) {
                int[] newPos = {pos[0], pos[1]+i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
            }else if(board[pos[0]][pos[1]+i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0], pos[1]+i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
                break;
            }else{
                break;
            }
        }
    }

    private void addBishopMoves(Piece piece, int[] pos, ArrayList<Moves> o){
        //Links Unten
        for(int i = 1; pos[0]-i>-1&&pos[1]-i>-1; i++){
            if(board[pos[0]-i][pos[1]-i] == null) {
                int[] newPos = {pos[0]-i, pos[1]-i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
            }else if(board[pos[0]-i][pos[1]-i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]-i, pos[1]-i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
                break;
            }else{
                break;
            }
        }
        //Rechts Unten
        for(int i = 1; pos[0]+i<8&&pos[1]-i>-1; i++){
            if(board[pos[0]+i][pos[1]-i] == null) {
                int[] newPos = {pos[0]+i, pos[1]-i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
            }else if(board[pos[0]+i][pos[1]-i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]+i, pos[1]-i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
                break;
            }else{
                break;
            }
        }
        //Links Oben
        for(int i = 1; pos[0]-i>-1&&pos[1]+i<8; i++){
            if(board[pos[0]-i][pos[1]+i] == null) {
                int[] newPos = {pos[0]-i, pos[1]+i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
            }else if(board[pos[0]-i][pos[1]+i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]-i, pos[1]+i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
                break;
            }else{
                break;
            }
        }
        //Rechts Oben
        for(int i = 1; pos[0]+i<8&&pos[1]+i<8; i++){
            if(board[pos[0]+i][pos[1]+i] == null) {
                int[] newPos = {pos[0]+i, pos[1]+i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
            }else if(board[pos[0]+i][pos[1]+i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]+i, pos[1]+i};
                Move m = new Move(piece, pos, newPos);
                Moves ms = new Moves(m);
                o.add(ms);
                break;
            }else{
                break;
            }
        }
    }

    private void addKingMoves(Piece piece, int[] pos, ArrayList<Moves> o){
        //Links Unten
        if(pos[0]-1>-1&&pos[1]-1>-1&&(board[pos[0]-1][pos[1]-1] == null || board[pos[0]-1][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]-1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Links
        if(pos[0]-1>-1&&(board[pos[0]-1][pos[1]] == null || board[pos[0]-1][pos[1]].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Links Oben
        if(pos[0]-1>-1&&pos[1]+1<8&&(board[pos[0]-1][pos[1]+1] == null || board[pos[0]-1][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]+1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Unten
        if(pos[1]-1>-1&&(board[pos[0]][pos[1]-1] == null || board[pos[0]][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0], pos[1]-1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Oben
        if(pos[1]+1<8&&(board[pos[0]][pos[1]+1] == null || board[pos[0]][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0], pos[1]+1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Rechts Unten
        if(pos[0]+1<8&&pos[1]-1>-1&&(board[pos[0]+1][pos[1]-1] == null || board[pos[0]+1][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]-1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Rechts
        if(pos[0]+1<8&&(board[pos[0]+1][pos[1]] == null || board[pos[0]+1][pos[1]].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Rechts Oben
        if(pos[0]+1<8&&pos[1]+1<8&&(board[pos[0]+1][pos[1]+1] == null || board[pos[0]+1][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]+1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }

        //Rochade Rechts
        if(piece.getMoves() == 0 && board[7][pos[1]] != null && board[7][pos[1]].getMoves() == 0 && board[5][pos[1]] == null && board[6][pos[1]] == null) {
            int[] newPosK = {6, pos[1]};
            Move mk = new Move(piece, pos, newPosK);
            Moves ms = new Moves(mk);

            int[] newPosR = {5, pos[1]};
            int[] oldPosR = {7, pos[1]};
            Move mr = new Move(board[7][pos[1]], oldPosR, newPosR);
            ms.addMove(mr);
            o.add(ms);
        }
        //Rochade Links
        if(piece.getMoves() == 0 && board[0][pos[1]] != null && board[0][pos[1]].getMoves() == 0 && board[1][pos[1]] == null && board[2][pos[1]] == null && board[3][pos[1]] == null) {
            int[] newPosK = {2, pos[1]};
            Move m = new Move(piece, pos, newPosK);
            Moves ms = new Moves(m);

            int[] newPosR = {3, pos[1]};
            int[] oldPosR = {0, pos[1]};
            Move mr = new Move(board[7][pos[1]], oldPosR, newPosR);
            ms.addMove(mr);
            o.add(ms);
        }
    }

    private void addKnightMoves(Piece piece, int[] pos, ArrayList<Moves> o){
        //Links Unten
        if(pos[0]-2>-1&&pos[1]-1>-1&&(board[pos[0]-2][pos[1]-1] == null || board[pos[0]-2][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-2, pos[1]-1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Links Oben
        if(pos[0]-2>-1&&pos[1]+1<8&&(board[pos[0]-2][pos[1]+1] == null || board[pos[0]-2][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-2, pos[1]+1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Unten Links
        if(pos[0]-1>-1&&pos[1]-2>-1&&(board[pos[0]-1][pos[1]-2] == null || board[pos[0]-1][pos[1]-2].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]-2};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Unten Rechts
        if(pos[0]+1<8&&pos[1]-2>-1&&(board[pos[0]+1][pos[1]-2] == null || board[pos[0]+1][pos[1]-2].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]-2};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Oben Links
        if(pos[0]-1>-1&&pos[1]+2<8&&(board[pos[0]-1][pos[1]+2] == null || board[pos[0]-1][pos[1]+2].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]+2};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Oben Rechts
        if(pos[0]+1<8&&pos[1]+2<8&&(board[pos[0]+1][pos[1]+2] == null || board[pos[0]+1][pos[1]+2].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]+2};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Rechts Unten
        if(pos[0]+2<8&&pos[1]-1>-1&&(board[pos[0]+2][pos[1]-1] == null || board[pos[0]+2][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+2, pos[1]-1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
        //Rechts Oben
        if(pos[0]+2<8&&pos[1]+1<8&&(board[pos[0]+2][pos[1]+1] == null || board[pos[0]+2][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+2, pos[1]+1};
            Move m = new Move(piece, pos, newPos);
            Moves ms = new Moves(m);
            o.add(ms);
        }
    }

    private void addPawnMoves(Piece piece, int[] pos, ArrayList<Moves> o){
        if(piece.isWhite()){
            //Links Oben
            if(pos[0]-1>-1&&pos[1]+1<8&&(board[pos[0]-1][pos[1]+1] != null && board[pos[0]-1][pos[1]+1].isWhite()!=piece.isWhite())) {
                int[] newPos = {pos[0]-1, pos[1]+1};
                Move m = new Move(piece, pos, newPos);
                if(newPos[1] == 7) {
                    m.setNewPiece(Queen.convert((Pawn)piece));
                }
                Moves ms = new Moves(m);
                o.add(ms);
            }
            //Oben
            if(pos[1]+1<8&&(board[pos[0]][pos[1]+1] == null)) {
                int[] newPos = {pos[0], pos[1]+1};
                Move m = new Move(piece, pos, newPos);
                if(newPos[1] == 7) {
                    m.setNewPiece(Queen.convert((Pawn)piece));
                }
                Moves ms = new Moves(m);
                o.add(ms);
            }
            //Oben Oben
            if(pos[1] == 1) {
                if (pos[1] + 2 < 8 && (board[pos[0]][pos[1] + 2] == null && board[pos[0]][pos[1] + 1] == null)) {
                    int[] newPos = {pos[0], pos[1] + 2};
                    Move m = new Move(piece, pos, newPos);
                    Moves ms = new Moves(m);  
                    o.add(ms);
                }
            }
            //Rechts Oben
            if(pos[0]+1<8&&pos[1]+1<8&&(board[pos[0]+1][pos[1]+1] != null && board[pos[0]+1][pos[1]+1].isWhite()!=piece.isWhite())) {
                int[] newPos = {pos[0]+1, pos[1]+1};
                Move m = new Move(piece, pos, newPos);
                if(newPos[1] == 7) {
                    m.setNewPiece(Queen.convert((Pawn)piece));
                }
                Moves ms = new Moves(m);
                o.add(ms);
            }
        }else{
            //Links Unten
            if(pos[0]-1>-1&&pos[1]-1>-1&&(board[pos[0]-1][pos[1]-1] != null && board[pos[0]-1][pos[1]-1].isWhite()!=piece.isWhite())) {
                int[] newPos = {pos[0]-1, pos[1]-1};
                Move m = new Move(piece, pos, newPos);
                if(newPos[1] == 0) {
                    m.setNewPiece(Queen.convert((Pawn)piece));
                }
                Moves ms = new Moves(m);
                o.add(ms);
            }
            //Unten
            if(pos[1]-1>-1&&(board[pos[0]][pos[1]-1] == null)) {
                int[] newPos = {pos[0], pos[1]-1};
                Move m = new Move(piece, pos, newPos);
                if(newPos[1] == 0) {
                    m.setNewPiece(Queen.convert((Pawn)piece));
                }
                Moves ms = new Moves(m);
                o.add(ms);
            }
            //Unten Unten
            if(pos[1] == 6) {
                if (pos[1] - 2 > -1 && (board[pos[0]][pos[1] - 2] == null && board[pos[0]][pos[1] -1] == null)) {
                    int[] newPos = {pos[0], pos[1] - 2};
                    Move m = new Move(piece, pos, newPos);
                    Moves ms = new Moves(m);
                    o.add(ms);
                }
            }
            //Rechts Unten
            if(pos[0]+1<8&&pos[1]-1>-1&&(board[pos[0]+1][pos[1]-1] != null && board[pos[0]+1][pos[1]-1].isWhite()!=piece.isWhite())) {
                int[] newPos = {pos[0]+1, pos[1]-1};
                Move m = new Move(piece, pos, newPos);
                if(newPos[1] == 0) {
                    m.setNewPiece(Queen.convert((Pawn)piece));
                }
                Moves ms = new Moves(m);
                o.add(ms);
            }
        }
    }
}
