package com.example.chess.model;

import com.example.chess.Player;
import com.example.chess.model.pieces.Pawn;
import com.example.chess.model.pieces.Piece;
import com.example.chess.model.pieces.Queen;

import java.util.ArrayList;
import java.util.Arrays;

public class Rules {

    public Rules(){

    }

    public ArrayList<Moves> getAllPosMoves(Player player, Piece[][] board){
        ArrayList<Moves> o = new ArrayList<>();

        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                if(board[i][j] != null && board[i][j].getPlayer() == player){
                    o.addAll(getPosMoves(board[i][j], new int[]{i, j}, board));
                }
            }
        }

        for(int i = 0; i<o.size(); i++){
            if(checkCheck(o.get(i), board)){
                o.remove(i);
                i--;
            }
        }

        return o;
    }

    private boolean checkCheck(Moves moves, Piece[][] board){
        //Own Player
        Player ownPlayer = moves.getMoves().get(0).getOldPiece().getPlayer();
        int[] kingPos = null;

        //Sets up a board copy
        Piece[][] boardCopy = new Piece[8][8];
        for(int i = 0; i<boardCopy.length; i++){
            for(int j = 0; j<boardCopy[i].length; j++){
                boardCopy[i][j] = board[i][j];
            }
        }

        //Does the move on the board copy
        for (int j = 0; j < moves.getMoves().size(); j++) {
            boardCopy[moves.getMoves().get(j).getOldPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
            boardCopy[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getNewPos()[1]] = moves.getMoves().get(j).getNewPiece();
        }

        //Gets all possible moves of the other player on that board copy
        ArrayList<Moves> posMovesBoardCopy = new ArrayList<>();
        for(int i = 0; i<boardCopy.length; i++){
            for(int j = 0; j<boardCopy[i].length; j++){
                if(boardCopy[i][j] != null && boardCopy[i][j].getPlayer() != ownPlayer){
                    posMovesBoardCopy.addAll(getPosMoves(boardCopy[i][j], new int[]{i, j}, boardCopy));
                }

                //Gets the pos of the own king
                if(boardCopy[i][j] != null && boardCopy[i][j].getClass().getSimpleName().equals("King") && boardCopy[i][j].getPlayer() == ownPlayer){
                    kingPos = new int[]{i, j};
                }
            }
        }

        //If one of these moves can kill the own king, return true
        for(int i = 0; i<posMovesBoardCopy.size(); i++){
            if(Arrays.equals(posMovesBoardCopy.get(i).getMoves().get(0).getNewPos(), kingPos)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Moves> getPosMoves(Piece piece, int[] pos, Piece[][] board){
        ArrayList<Moves> o = new ArrayList<Moves>();

        addAllMoves(piece, pos, o, board);

        return o;
    }

    private void addAllMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
        switch (piece.getClass().getSimpleName()) {
            case "Queen":
                addRockMoves(piece, pos, o, board);
                addBishopMoves(piece, pos, o, board);
                break;
            case "King":
                addKingMoves(piece, pos, o, board);
                break;
            case "Rock":
                addRockMoves(piece, pos, o, board);
                break;
            case "Bishop":
                addBishopMoves(piece, pos, o, board);
                break;
            case "Knight":
                addKnightMoves(piece, pos, o, board);
                break;
            case "Pawn":
                addPawnMoves(piece, pos, o, board);
                break;
        }
    }

    private void addRockMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
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

    private void addBishopMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
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

    private void addKingMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
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

    private void addKnightMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
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

    private void addPawnMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
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
