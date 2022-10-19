package com.example.chess.model;

import com.example.chess.Player;
import com.example.chess.model.pieces.Pawn;
import com.example.chess.model.pieces.Piece;
import com.example.chess.model.pieces.Queen;

import java.util.ArrayList;
import java.util.Arrays;

public class Rules {

    private ArrayList<Moves> lastMoves;

    public Rules(ArrayList<Moves> lastMoves){
        this.lastMoves = lastMoves;
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
            Moves moves = o.get(i);
            if(moves.getRochadeType() == 0) {
                if (checkCheck(board, player, moves, null)) {
                    o.remove(i);
                    i--;
                }
            }else if(moves.getRochadeType() == 1){
                if (checkCheck(board, player, null, null) || checkCheck(board, player, null, new int[]{1, moves.getMoves().get(0).getOldPos()[1]}) || checkCheck(board, player, null, new int[]{2, moves.getMoves().get(0).getOldPos()[1]}) || checkCheck(board, player, null, new int[]{3, moves.getMoves().get(0).getOldPos()[1]})) {
                    o.remove(i);
                    i--;
                }
            }else if(moves.getRochadeType() == -1) {
                if (checkCheck(board, player, null, null) || checkCheck(board, player, null, new int[]{6, moves.getMoves().get(0).getOldPos()[1]}) || checkCheck(board, player, null, new int[]{5, moves.getMoves().get(0).getOldPos()[1]})) {
                    o.remove(i);
                    i--;
                }
            }
        }

        return o;
    }

    private boolean checkCheck(Piece[][] board, Player ownPlayer, Moves moves, int[] kingPos){//Sets up a board copy
        Piece[][] boardCopy = new Piece[8][8];
        for(int i = 0; i<boardCopy.length; i++){
            for(int j = 0; j<boardCopy[i].length; j++){
                boardCopy[i][j] = board[i][j];
            }
        }

        //Does the move on the board copy
        if(moves != null) {
            for (int j = 0; j < moves.getMoves().size(); j++) {
                boardCopy[moves.getMoves().get(j).getOldPos()[0]][moves.getMoves().get(j).getOldPos()[1]] = null;
                boardCopy[moves.getMoves().get(j).getNewPos()[0]][moves.getMoves().get(j).getNewPos()[1]] = moves.getMoves().get(j).getNewPiece();
            }
        }

        //Gets all possible moves of the other player on that board copy
        ArrayList<Moves> posMovesBoardCopy = new ArrayList<>();
        for(int i = 0; i<boardCopy.length; i++){
            for(int j = 0; j<boardCopy[i].length; j++){
                if(boardCopy[i][j] != null && boardCopy[i][j].getPlayer() != ownPlayer){
                    posMovesBoardCopy.addAll(getPosMoves(boardCopy[i][j], new int[]{i, j}, boardCopy));
                }

                //Gets the pos of the own king
                if(kingPos == null) {
                    if (boardCopy[i][j] != null && boardCopy[i][j].getClass().getSimpleName().equals("King") && boardCopy[i][j].getPlayer() == ownPlayer) {
                        kingPos = new int[]{i, j};
                    }
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

    public boolean lastMovesSame(ArrayList<Moves> allMoves, int count){
        if(allMoves.size()>=count*2*2) {
            for (int i = 0; i < count*2*2-4; i++) {
                if (!allMoves.get(allMoves.size() - 1 - i).equals(allMoves.get(allMoves.size() - 1 - i - 4))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean lastMovesCapture(ArrayList<Moves> allMoves, int count){
        if(allMoves.size()>=count*2) {
            for (int i = 0; i < count*2; i++) {
                if (allMoves.get(allMoves.size() - 1 - i).isCapture()){
                    return false;
                }
            }
            return true;
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
                addRookMoves(piece, pos, o, board);
                addBishopMoves(piece, pos, o, board);
                break;
            case "King":
                addKingMoves(piece, pos, o, board);
                break;
            case "Rook":
                addRookMoves(piece, pos, o, board);
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

    private void addRookMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
        //Links
        for(int i = 1; pos[0]-i>-1; i++){
            if(board[pos[0]-i][pos[1]] == null) {
                int[] newPos = {pos[0]-i, pos[1]};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }else if(board[pos[0]-i][pos[1]].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]-i, pos[1]};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
                break;
            }else{
                break;
            }
        }
        //Rechts
        for(int i = 1; pos[0]+i<8; i++){
            if(board[pos[0]+i][pos[1]] == null) {
                int[] newPos = {pos[0]+i, pos[1]};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }else if(board[pos[0]+i][pos[1]].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]+i, pos[1]};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
                break;
            }else{
                break;
            }
        }
        //Unten
        for(int i = 1; pos[1]-i>-1; i++){
            if(board[pos[0]][pos[1]-i] == null) {
                int[] newPos = {pos[0], pos[1]-i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }else if(board[pos[0]][pos[1]-i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0], pos[1]-i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
                break;
            }else{
                break;
            }
        }
        //Oben
        for(int i = 1; pos[1]+i<8; i++){
            if(board[pos[0]][pos[1]+i] == null) {
                int[] newPos = {pos[0], pos[1]+i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }else if(board[pos[0]][pos[1]+i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0], pos[1]+i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
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
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }else if(board[pos[0]-i][pos[1]-i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]-i, pos[1]-i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
                break;
            }else{
                break;
            }
        }
        //Rechts Unten
        for(int i = 1; pos[0]+i<8&&pos[1]-i>-1; i++){
            if(board[pos[0]+i][pos[1]-i] == null) {
                int[] newPos = {pos[0]+i, pos[1]-i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }else if(board[pos[0]+i][pos[1]-i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]+i, pos[1]-i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
                break;
            }else{
                break;
            }
        }
        //Links Oben
        for(int i = 1; pos[0]-i>-1&&pos[1]+i<8; i++){
            if(board[pos[0]-i][pos[1]+i] == null) {
                int[] newPos = {pos[0]-i, pos[1]+i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }else if(board[pos[0]-i][pos[1]+i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]-i, pos[1]+i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
                break;
            }else{
                break;
            }
        }
        //Rechts Oben
        for(int i = 1; pos[0]+i<8&&pos[1]+i<8; i++){
            if(board[pos[0]+i][pos[1]+i] == null) {
                int[] newPos = {pos[0]+i, pos[1]+i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }else if(board[pos[0]+i][pos[1]+i].isWhite() != piece.isWhite()){
                int[] newPos = {pos[0]+i, pos[1]+i};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
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
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Links
        if(pos[0]-1>-1&&(board[pos[0]-1][pos[1]] == null || board[pos[0]-1][pos[1]].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Links Oben
        if(pos[0]-1>-1&&pos[1]+1<8&&(board[pos[0]-1][pos[1]+1] == null || board[pos[0]-1][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]+1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Unten
        if(pos[1]-1>-1&&(board[pos[0]][pos[1]-1] == null || board[pos[0]][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0], pos[1]-1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Oben
        if(pos[1]+1<8&&(board[pos[0]][pos[1]+1] == null || board[pos[0]][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0], pos[1]+1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Rechts Unten
        if(pos[0]+1<8&&pos[1]-1>-1&&(board[pos[0]+1][pos[1]-1] == null || board[pos[0]+1][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]-1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Rechts
        if(pos[0]+1<8&&(board[pos[0]+1][pos[1]] == null || board[pos[0]+1][pos[1]].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Rechts Oben
        if(pos[0]+1<8&&pos[1]+1<8&&(board[pos[0]+1][pos[1]+1] == null || board[pos[0]+1][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]+1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }

        //Rochade Rechts
        if(piece.getMoves() == 0 && board[7][pos[1]] != null && board[7][pos[1]].getMoves() == 0 && board[5][pos[1]] == null && board[6][pos[1]] == null) {
            int[] newPosK = {6, pos[1]};

            int[] oldPosR = {7, pos[1]};
            int[] newPosR = {5, pos[1]};

            ArrayList<Move> moves = new ArrayList<>();
            moves.add(generateMove(piece, pos, newPosK));
            moves.add(generateMove(board[oldPosR[0]][oldPosR[1]], oldPosR, newPosR));

            o.add(generateMoves(moves));
        }
        //Rochade Links
        if(piece.getMoves() == 0 && board[0][pos[1]] != null && board[0][pos[1]].getMoves() == 0 && board[1][pos[1]] == null && board[2][pos[1]] == null && board[3][pos[1]] == null) {
            int[] newPosK = {2, pos[1]};

            int[] newPosR = {3, pos[1]};
            int[] oldPosR = {0, pos[1]};

            ArrayList<Move> moves = new ArrayList<>();
            moves.add(generateMove(piece, pos, newPosK));
            moves.add(generateMove(board[oldPosR[0]][oldPosR[1]], oldPosR, newPosR));

            o.add(generateMoves(moves));
        }
    }

    private void addKnightMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
        //Links Unten
        if(pos[0]-2>-1&&pos[1]-1>-1&&(board[pos[0]-2][pos[1]-1] == null || board[pos[0]-2][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-2, pos[1]-1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Links Oben
        if(pos[0]-2>-1&&pos[1]+1<8&&(board[pos[0]-2][pos[1]+1] == null || board[pos[0]-2][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-2, pos[1]+1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Unten Links
        if(pos[0]-1>-1&&pos[1]-2>-1&&(board[pos[0]-1][pos[1]-2] == null || board[pos[0]-1][pos[1]-2].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]-2};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Unten Rechts
        if(pos[0]+1<8&&pos[1]-2>-1&&(board[pos[0]+1][pos[1]-2] == null || board[pos[0]+1][pos[1]-2].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]-2};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Oben Links
        if(pos[0]-1>-1&&pos[1]+2<8&&(board[pos[0]-1][pos[1]+2] == null || board[pos[0]-1][pos[1]+2].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]-1, pos[1]+2};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Oben Rechts
        if(pos[0]+1<8&&pos[1]+2<8&&(board[pos[0]+1][pos[1]+2] == null || board[pos[0]+1][pos[1]+2].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+1, pos[1]+2};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Rechts Unten
        if(pos[0]+2<8&&pos[1]-1>-1&&(board[pos[0]+2][pos[1]-1] == null || board[pos[0]+2][pos[1]-1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+2, pos[1]-1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
        //Rechts Oben
        if(pos[0]+2<8&&pos[1]+1<8&&(board[pos[0]+2][pos[1]+1] == null || board[pos[0]+2][pos[1]+1].isWhite()!=piece.isWhite())) {
            int[] newPos = {pos[0]+2, pos[1]+1};
            o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
        }
    }

    private void addPawnMoves(Piece piece, int[] pos, ArrayList<Moves> o, Piece[][] board){
        if(piece.isWhite()){
            //Links Oben
            if(pos[0]-1>-1&&pos[1]+1<8&&(board[pos[0]-1][pos[1]+1] != null && board[pos[0]-1][pos[1]+1].isWhite()!=piece.isWhite())) {
                int[] newPos = {pos[0]-1, pos[1]+1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }
            //Links Oben en passant
            else if(!lastMoves.isEmpty() && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPiece().getClass().getSimpleName().equals("Pawn") && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[0]+1==pos[0] && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[1]==pos[1] && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[1]+2==lastMoves.get(lastMoves.size()-1).getMoves().get(0).getOldPos()[1]) {
                int[] newPos = {pos[0]-1, pos[1]+1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), true));
            }
            //Oben
            if(pos[1]+1<8&&(board[pos[0]][pos[1]+1] == null)) {
                int[] newPos = {pos[0], pos[1]+1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }
            //Oben Oben
            if(pos[1] == 1) {
                if (pos[1] + 2 < 8 && (board[pos[0]][pos[1] + 2] == null && board[pos[0]][pos[1] + 1] == null)) {
                    int[] newPos = {pos[0], pos[1] + 2};
                    o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
                }
            }
            //Rechts Oben
            if(pos[0]+1<8&&pos[1]+1<8&&(board[pos[0]+1][pos[1]+1] != null && board[pos[0]+1][pos[1]+1].isWhite()!=piece.isWhite())) {
                int[] newPos = {pos[0]+1, pos[1]+1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }
            //Rechts Oben en passant
            else if(!lastMoves.isEmpty() && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPiece().getClass().getSimpleName().equals("Pawn") && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[0]-1==pos[0] && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[1]==pos[1] && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[1]+2==lastMoves.get(lastMoves.size()-1).getMoves().get(0).getOldPos()[1]) {
                int[] newPos = {pos[0]+1, pos[1]+1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), true));
            }
        }else{
            //Links Unten
            if(pos[0]-1>-1&&pos[1]-1>-1&&(board[pos[0]-1][pos[1]-1] != null && board[pos[0]-1][pos[1]-1].isWhite()!=piece.isWhite())) {
                int[] newPos = {pos[0]-1, pos[1]-1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }
            //Links Unten en passant
            else if(!lastMoves.isEmpty() && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPiece().getClass().getSimpleName().equals("Pawn") && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[0]+1==pos[0] && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[1]==pos[1] && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[1]-2==lastMoves.get(lastMoves.size()-1).getMoves().get(0).getOldPos()[1]) {
                int[] newPos = {pos[0]-1, pos[1]-1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), true));
            }
            //Unten
            if(pos[1]-1>-1&&(board[pos[0]][pos[1]-1] == null)) {
                int[] newPos = {pos[0], pos[1]-1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }
            //Unten Unten
            if(pos[1] == 6) {
                if (pos[1] - 2 > -1 && (board[pos[0]][pos[1] - 2] == null && board[pos[0]][pos[1] -1] == null)) {
                    int[] newPos = {pos[0], pos[1] - 2};
                    o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
                }
            }
            //Rechts Unten
            if(pos[0]+1<8&&pos[1]-1>-1&&(board[pos[0]+1][pos[1]-1] != null && board[pos[0]+1][pos[1]-1].isWhite()!=piece.isWhite())) {
                int[] newPos = {pos[0]+1, pos[1]-1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), false));
            }
            //Rechts Unten en passant
            else if(!lastMoves.isEmpty() && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPiece().getClass().getSimpleName().equals("Pawn") && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[0]-1==pos[0] && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[1]==pos[1] && lastMoves.get(lastMoves.size()-1).getMoves().get(0).getNewPos()[1]-2==lastMoves.get(lastMoves.size()-1).getMoves().get(0).getOldPos()[1]) {
                int[] newPos = {pos[0]+1, pos[1]-1};
                o.add(generateMoves(board, generateMove(piece, pos, newPos), true));
            }
        }
    }
    
    private Moves generateMoves(Piece[][] board, Move move, boolean enPassant){
        if(enPassant) {
            Moves ms = new Moves(move, true, enPassant);
            return ms;
        }
        if(board[move.getNewPos()[0]][move.getNewPos()[1]] != null) {
            Moves ms = new Moves(move, true, false);
            return ms;
        }else{
            Moves ms = new Moves(move, false, false);
            return ms;
        }
    }

    private Moves generateMoves(ArrayList<Move> moves){
        Moves ms = new Moves(moves);
        return ms;
    }

    private Move generateMove(Piece piece, int[] oldPos, int[] newPos){
        Move m = new Move(piece, oldPos, newPos);
        if(piece.getClass().getSimpleName().equals("Pawn") && (newPos[1] == 0 || newPos[1] == 7)) {
            m.setNewPiece(Pawn.convertToQueen((Pawn)piece));
        }
        return m;
    }
}
