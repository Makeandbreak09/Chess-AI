package com.example.chess.model;

import com.example.chess.model.pieces.Piece;

import java.util.ArrayList;

public class Rules {

    private Piece[][] board;

    public Rules(Piece[][] board){
        this.board = board;
    }

    public ArrayList<int[]> getPosPos(Piece piece, int[] pos){
        ArrayList<int[]> o = new ArrayList<int[]>();

        addAllMoves(piece, pos, o);

        return o;
    }

    private void addAllMoves(Piece piece, int[] pos, ArrayList<int[]> o){
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

    private void addRockMoves(Piece piece, int[] pos, ArrayList<int[]> o){
        //Links
        for(int i = 1; pos[0]-i>-1; i++){
            if(board[pos[0]-i][pos[1]] == null) {
                int[] p = {pos[0]-i, pos[1]};
                o.add(p);
            }else if(board[pos[0]-i][pos[1]].isWhite() != piece.isWhite()){
                int[] p = {pos[0]-i, pos[1]};
                o.add(p);
                break;
            }else{
                break;
            }
        }
        //Rechts
        for(int i = 1; pos[0]+i<8; i++){
            if(board[pos[0]+i][pos[1]] == null) {
                int[] p = {pos[0]+i, pos[1]};
                o.add(p);
            }else if(board[pos[0]+i][pos[1]].isWhite() != piece.isWhite()){
                int[] p = {pos[0]+i, pos[1]};
                o.add(p);
                break;
            }else{
                break;
            }
        }
        //Unten
        for(int i = 1; pos[1]-i>-1; i++){
            if(board[pos[0]][pos[1]-i] == null) {
                int[] p = {pos[0], pos[1]-i};
                o.add(p);
            }else if(board[pos[0]][pos[1]-i].isWhite() != piece.isWhite()){
                int[] p = {pos[0], pos[1]-i};
                o.add(p);
                break;
            }else{
                break;
            }
        }
        //Oben
        for(int i = 1; pos[1]+i<8; i++){
            if(board[pos[0]][pos[1]+i] == null) {
                int[] p = {pos[0], pos[1]+i};
                o.add(p);
            }else if(board[pos[0]][pos[1]+i].isWhite() != piece.isWhite()){
                int[] p = {pos[0], pos[1]+i};
                o.add(p);
                break;
            }else{
                break;
            }
        }
    }

    private void addBishopMoves(Piece piece, int[] pos, ArrayList<int[]> o){
        //Links Unten
        for(int i = 1; pos[0]-i>-1&&pos[1]-i>-1; i++){
            if(board[pos[0]-i][pos[1]-i] == null) {
                int[] p = {pos[0]-i, pos[1]-i};
                o.add(p);
            }else if(board[pos[0]-i][pos[1]-i].isWhite() != piece.isWhite()){
                int[] p = {pos[0]-i, pos[1]-i};
                o.add(p);
                break;
            }else{
                break;
            }
        }
        //Rechts Unten
        for(int i = 1; pos[0]+i<8&&pos[1]-i>-1; i++){
            if(board[pos[0]+i][pos[1]-i] == null) {
                int[] p = {pos[0]+i, pos[1]-i};
                o.add(p);
            }else if(board[pos[0]+i][pos[1]-i].isWhite() != piece.isWhite()){
                int[] p = {pos[0]+i, pos[1]-i};
                o.add(p);
                break;
            }else{
                break;
            }
        }
        //Links Oben
        for(int i = 1; pos[0]-i>-1&&pos[1]+i<8; i++){
            if(board[pos[0]-i][pos[1]+i] == null) {
                int[] p = {pos[0]-i, pos[1]+i};
                o.add(p);
            }else if(board[pos[0]-i][pos[1]+i].isWhite() != piece.isWhite()){
                int[] p = {pos[0]-i, pos[1]+i};
                o.add(p);
                break;
            }else{
                break;
            }
        }
        //Rechts Oben
        for(int i = 1; pos[0]+i<8&&pos[1]+i<8; i++){
            if(board[pos[0]+i][pos[1]+i] == null) {
                int[] p = {pos[0]+i, pos[1]+i};
                o.add(p);
            }else if(board[pos[0]+i][pos[1]+i].isWhite() != piece.isWhite()){
                int[] p = {pos[0]+i, pos[1]+i};
                o.add(p);
                break;
            }else{
                break;
            }
        }
    }

    private void addKingMoves(Piece piece, int[] pos, ArrayList<int[]> o){
        //Links Unten
        if(pos[0]-1>-1&&pos[1]-1>-1&&(board[pos[0]-1][pos[1]-1] == null || board[pos[0]-1][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]-1, pos[1]-1};
            o.add(p);
        }
        //Links
        if(pos[0]-1>-1&&(board[pos[0]-1][pos[1]] == null || board[pos[0]-1][pos[1]].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]-1, pos[1]};
            o.add(p);
        }
        //Links Oben
        if(pos[0]-1>-1&&pos[1]+1<8&&(board[pos[0]-1][pos[1]+1] == null || board[pos[0]-1][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]-1, pos[1]+1};
            o.add(p);
        }
        //Unten
        if(pos[1]-1>-1&&(board[pos[0]][pos[1]-1] == null || board[pos[0]][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0], pos[1]-1};
            o.add(p);
        }
        //Oben
        if(pos[1]+1<8&&(board[pos[0]][pos[1]+1] == null || board[pos[0]][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0], pos[1]+1};
            o.add(p);
        }
        //Rechts Unten
        if(pos[0]+1<8&&pos[1]-1>-1&&(board[pos[0]+1][pos[1]-1] == null || board[pos[0]+1][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]+1, pos[1]-1};
            o.add(p);
        }
        //Rechts
        if(pos[0]+1<8&&(board[pos[0]+1][pos[1]] == null || board[pos[0]+1][pos[1]].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]+1, pos[1]};
            o.add(p);
        }
        //Rechts Oben
        if(pos[0]+1<8&&pos[1]+1<8&&(board[pos[0]+1][pos[1]+1] == null || board[pos[0]+1][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]+1, pos[1]+1};
            o.add(p);
        }
    }

    private void addKnightMoves(Piece piece, int[] pos, ArrayList<int[]> o){
        //Links Unten
        if(pos[0]-2>-1&&pos[1]-1>-1&&(board[pos[0]-2][pos[1]-1] == null || board[pos[0]-2][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]-2, pos[1]-1};
            o.add(p);
        }
        //Links Oben
        if(pos[0]-2>-1&&pos[1]+1<8&&(board[pos[0]-2][pos[1]+1] == null || board[pos[0]-2][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]-2, pos[1]+1};
            o.add(p);
        }
        //Unten Links
        if(pos[0]-1>-1&&pos[1]-2>-1&&(board[pos[0]-1][pos[1]-2] == null || board[pos[0]-1][pos[1]-2].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]-1, pos[1]-2};
            o.add(p);
        }
        //Unten Rechts
        if(pos[0]+1<8&&pos[1]-2>-1&&(board[pos[0]+1][pos[1]-2] == null || board[pos[0]+1][pos[1]-2].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]+1, pos[1]-2};
            o.add(p);
        }
        //Oben Links
        if(pos[0]-1>-1&&pos[1]+2<8&&(board[pos[0]-1][pos[1]+2] == null || board[pos[0]-1][pos[1]+2].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]-1, pos[1]+2};
            o.add(p);
        }
        //Oben Rechts
        if(pos[0]+1<8&&pos[1]+2<8&&(board[pos[0]+1][pos[1]+2] == null || board[pos[0]+1][pos[1]+2].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]+1, pos[1]+2};
            o.add(p);
        }
        //Rechts Unten
        if(pos[0]+2<8&&pos[1]-1>-1&&(board[pos[0]+2][pos[1]-1] == null || board[pos[0]+2][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]+2, pos[1]-1};
            o.add(p);
        }
        //Rechts Oben
        if(pos[0]+2<8&&pos[1]+1<8&&(board[pos[0]+2][pos[1]+1] == null || board[pos[0]+2][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] p = {pos[0]+2, pos[1]+1};
            o.add(p);
        }
    }

    private void addPawnMoves(Piece piece, int[] pos, ArrayList<int[]> o){
        if(piece.isWhite()){
            //Links Oben
            if(pos[0]-1>-1&&pos[1]+1<8&&(board[pos[0]-1][pos[1]+1] != null && board[pos[0]-1][pos[1]+1].isWhite()!=piece.isWhite())) {
                int[] p = {pos[0]-1, pos[1]+1};
                o.add(p);
            }
            //Oben
            if(pos[1]+1<8&&(board[pos[0]][pos[1]+1] == null)) {
                int[] p = {pos[0], pos[1]+1};
                o.add(p);
            }
            //Oben Oben
            if(pos[1] == 1) {
                if (pos[1] + 2 < 8 && (board[pos[0]][pos[1] + 2] == null && board[pos[0]][pos[1] + 1] == null)) {
                    int[] p = {pos[0], pos[1] + 2};
                    o.add(p);
                }
            }
            //Rechts Oben
            if(pos[0]+1<8&&pos[1]+1<8&&(board[pos[0]+1][pos[1]+1] != null && board[pos[0]+1][pos[1]+1].isWhite()!=piece.isWhite())) {
                int[] p = {pos[0]+1, pos[1]+1};
                o.add(p);
            }
        }else{
            //Links Unten
            if(pos[0]-1>-1&&pos[1]-1>-1&&(board[pos[0]-1][pos[1]-1] != null && board[pos[0]-1][pos[1]-1].isWhite()!=piece.isWhite())) {
                int[] p = {pos[0]-1, pos[1]-1};
                o.add(p);
            }
            //Unten
            if(pos[1]-1>-1&&(board[pos[0]][pos[1]-1] == null)) {
                int[] p = {pos[0], pos[1]-1};
                o.add(p);
            }
            //Unten Unten
            if(pos[1] == 6) {
                if (pos[1] - 2 > -1 && (board[pos[0]][pos[1] - 2] == null && board[pos[0]][pos[1] -1] == null)) {
                    int[] p = {pos[0], pos[1] - 2};
                    o.add(p);
                }
            }
            //Rechts Unten
            if(pos[0]+1<8&&pos[1]-1>-1&&(board[pos[0]+1][pos[1]-1] != null && board[pos[0]+1][pos[1]-1].isWhite()!=piece.isWhite())) {
                int[] p = {pos[0]+1, pos[1]-1};
                o.add(p);
            }
        }
    }
}
