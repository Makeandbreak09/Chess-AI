package com.example.chess;

import com.example.chess.model.*;
import com.example.chess.model.customAdapter.AllEdgesAdapter;
import com.example.chess.model.customAdapter.AllNeuronsAdapter;
import com.example.chess.model.pieces.Piece;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class AI extends Player implements Serializable {

    public static final String PATH = "src/main/java/com/example/chess/AISaves.txt";
    public static final String PATH2 = "src/main/java/com/example/chess/MovesDoneSaves.txt";

    private Neuron[][] allNeurons;
    private int generation = 1;

    public AI(int generation, Neuron[][] allNeurons){
        this.generation = generation;
        this.allNeurons = allNeurons;
    }

    public Moves play(Piece[][] board, ArrayList<Moves> allPosMoves){
        Moves o = null;
        if(!allPosMoves.isEmpty()){
            //Sets up the values of the InputNeurons
            setNeuronValues(board);

            //Sorts the OutputNeurons by their value
            ArrayList<Neuron> sortedNeurons = new ArrayList<>();
            sortedNeurons.add(allNeurons[allNeurons.length-1][0]);
            for(int i = 1; i<allNeurons[allNeurons.length-1].length; i++){
                for(int k = 0; k<sortedNeurons.size(); k++) {
                    if (allNeurons[allNeurons.length - 1][i].getValue() >= sortedNeurons.get(k).getValue()) {
                        sortedNeurons.add(k, allNeurons[allNeurons.length - 1][i]);
                        break;
                    }
                }
            }

            for(int k = 0; k<sortedNeurons.size(); k++){
                int id = sortedNeurons.get(k).getId()[1];

                int[][] startAndEndPos = getStartAndEndPos(board, id);
                int[] startPos = startAndEndPos[0];
                int[] endPos = startAndEndPos[1];

                for(int j = 0; j<allPosMoves.size(); j++){
                    if(Arrays.equals(allPosMoves.get(j).getMoves().get(0).getOldPos(), startPos) && Arrays.equals(allPosMoves.get(j).getMoves().get(0).getNewPos(), endPos)){
                        o = allPosMoves.get(j);
                        return o;
                    }
                }
            }
        }
        return o;
    }

    private int[][] getStartAndEndPos(Piece[][] board, int id){
        int[] startPos = new int[]{-1, -1};
        int[] endPos = new int[]{-1, -1};
        //König
        if(id>=0 && id<=9){
            for(int i = 0; i<board.length; i++){
                for(int j = 0; j<board[i].length; j++){
                    if(board[i][j] != null && board[i][j].getPlayer() == this && board[i][j].getClass().getSimpleName().equals("King")){
                        startPos = new int[]{i, j};
                        break;
                    }
                }
            }
            switch (id){
                case 0:
                    endPos = new int[]{startPos[0]-1, startPos[1]};
                    break;
                case 1:
                    endPos = new int[]{startPos[0]-1, startPos[1]+1};
                    break;
                case 2:
                    endPos = new int[]{startPos[0], startPos[1]+1};
                    break;
                case 3:
                    endPos = new int[]{startPos[0]+1, startPos[1]+1};
                    break;
                case 4:
                    endPos = new int[]{startPos[0]+1, startPos[1]};
                    break;
                case 5:
                    endPos = new int[]{startPos[0]+1, startPos[1]-1};
                    break;
                case 6:
                    endPos = new int[]{startPos[0], startPos[1]-1};
                    break;
                case 7:
                    endPos = new int[]{startPos[0]-1, startPos[1]-1};
                    break;
                case 8:
                    endPos = new int[]{startPos[0]+2, startPos[1]};
                    break;
                case 9:
                    endPos = new int[]{startPos[0]-2, startPos[1]};
                    break;
            }
        }
        //Dame
        else if(id>=10 && id<=513){
            for(int i = 0; i<board.length; i++){
                for(int j = 0; j<board[i].length; j++){
                    if(board[i][j] != null && board[i][j].getPlayer() == this && board[i][j].getClass().getSimpleName().equals("Queen") && board[i][j].getId()==(id-10)/(56)){
                        startPos = new int[]{i, j};
                        break;
                    }
                }
            }
            id = (id-10)%(56);
            switch (id/7){
                case 0:
                    endPos = new int[]{startPos[0]-id%7, startPos[1]};
                    break;
                case 1:
                    endPos = new int[]{startPos[0]-id%7, startPos[1]+id%7};
                    break;
                case 2:
                    endPos = new int[]{startPos[0], startPos[1]+id%7};
                    break;
                case 3:
                    endPos = new int[]{startPos[0]+id%7, startPos[1]+id%7};
                    break;
                case 4:
                    endPos = new int[]{startPos[0]+id%7, startPos[1]};
                    break;
                case 5:
                    endPos = new int[]{startPos[0]+id%7, startPos[1]-id%7};
                    break;
                case 6:
                    endPos = new int[]{startPos[0], startPos[1]-id%7};
                    break;
                case 7:
                    endPos = new int[]{startPos[0]-id%7, startPos[1]-id%7};
                    break;
            }
        }
        //Turm
        else if(id>=514 && id<=793){
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] != null && board[i][j].getPlayer() == this && board[i][j].getClass().getSimpleName().equals("Rook") && board[i][j].getId()==(id-10-9*56)/(28)) {
                        startPos = new int[]{i, j};
                        break;
                    }
                }
            }
            id = (id-10-9*56)%(28);
            switch (id/7){
                case 0:
                    endPos = new int[]{startPos[0]-id%7, startPos[1]};
                    break;
                case 1:
                    endPos = new int[]{startPos[0], startPos[1]+id%7};
                    break;
                case 2:
                    endPos = new int[]{startPos[0]+id%7, startPos[1]};
                    break;
                case 3:
                    endPos = new int[]{startPos[0], startPos[1]-id%7};
                    break;
            }
        }
        //Läufer
        else if(id>=794 && id<=1073){
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] != null && board[i][j].getPlayer() == this && board[i][j].getClass().getSimpleName().equals("Bishop") && board[i][j].getId()==(id-10-9*56-10*28)/(28)) {
                        startPos = new int[]{i, j};
                        break;
                    }
                }
            }
            id = (id-10-9*56-10*28)%(28);
            switch (id/7){
                case 0:
                    endPos = new int[]{startPos[0]-id%7, startPos[1]+id%7};
                    break;
                case 1:
                    endPos = new int[]{startPos[0]+id%7, startPos[1]+id%7};
                    break;
                case 2:
                    endPos = new int[]{startPos[0]+id%7, startPos[1]-id%7};
                    break;
                case 3:
                    endPos = new int[]{startPos[0]-id%7, startPos[1]-id%7};
                    break;
            }
        }
        //Springer
        else if(id>=1074 && id<=1153){
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] != null && board[i][j].getPlayer() == this && board[i][j].getClass().getSimpleName().equals("Knight") && board[i][j].getId()==(id-10-9*56-10*28-10*28)/(8)) {
                        startPos = new int[]{i, j};
                        break;
                    }
                }
            }
            id = (id-10-9*56-10*28-10*28)%(8);
            switch (id){
                case 0:
                    endPos = new int[]{startPos[0]-2, startPos[1]-1};
                    break;
                case 1:
                    endPos = new int[]{startPos[0]-2, startPos[1]+1};
                    break;
                case 2:
                    endPos = new int[]{startPos[0]-1, startPos[1]+2};
                    break;
                case 3:
                    endPos = new int[]{startPos[0]+1, startPos[1]+2};
                    break;
                case 4:
                    endPos = new int[]{startPos[0]+2, startPos[1]+1};
                    break;
                case 5:
                    endPos = new int[]{startPos[0]+2, startPos[1]-1};
                    break;
                case 6:
                    endPos = new int[]{startPos[0]+1, startPos[1]-2};
                    break;
                case 7:
                    endPos = new int[]{startPos[0]-1, startPos[1]-2};
                    break;
            }
        }
        //Bauer
        else if(id>=1154 && id<=1185){
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] != null && board[i][j].getPlayer() == this && board[i][j].getClass().getSimpleName().equals("Pawn") && board[i][j].getId()==(id-10-9*56-10*28-10*28-10*8)/(4)) {
                        startPos = new int[]{i, j};
                        break;
                    }
                }
            }
            id = (id-10-9*56-10*28-10*28-10*8)%(4);
            if(white) {
                switch (id) {
                    case 0:
                        endPos = new int[]{startPos[0]-1, startPos[1]+1};
                        break;
                    case 1:
                        endPos = new int[]{startPos[0], startPos[1]+1};
                        break;
                    case 2:
                        endPos = new int[]{startPos[0], startPos[1]+2};
                        break;
                    case 3:
                        endPos = new int[]{startPos[0]+1, startPos[1]+1};
                        break;
                }
            }else{
                switch (id) {
                    case 0:
                        endPos = new int[]{startPos[0]-1, startPos[1]-1};
                        break;
                    case 1:
                        endPos = new int[]{startPos[0], startPos[1]-1};
                        break;
                    case 2:
                        endPos = new int[]{startPos[0], startPos[1]-2};
                        break;
                    case 3:
                        endPos = new int[]{startPos[0]+1, startPos[1]-1};
                        break;
                }
            }
        }

        return new int[][]{startPos, endPos};
    }

    public static AI setUpNeuralNetwork(int[] hiddenLayers){
        Neuron[][] allNeurons = new Neuron[hiddenLayers.length + 2][];
        //Jedes Feld für jede Figur(Binär) für jede Farbe + die eigene Farbe(448 + 1)
        allNeurons[0] = new Neuron[(8*8)*(6+1) + 1];
        //1*König + (1+8)*Dame + (2+8)*Turm + (2+8)*Läufer + (2+8)*Springer + 8*Bauer (1186)
        allNeurons[hiddenLayers.length+2-1] = new Neuron[10+(1+8)*(7*8)+(2+8)*(7*4)+(2+8)*(7*4)+(2+8)*(4*2)+8*(4)];

        for(int i = 1; i<allNeurons.length-1; i++){
            allNeurons[i] = new Neuron[hiddenLayers[i-1]];
        }

        for(int i = 0; i<allNeurons.length; i++){
            for(int j = 0; j<allNeurons[i].length; j++){
                if(i==0) {
                    allNeurons[i][j] = new InputNeuron(new int[]{i,j},-1, new Edge[0]);
                } else {
                    Edge[] allEdges = new Edge[allNeurons[i-1].length];
                    for(int k = 0; k<allNeurons[i-1].length; k++){
                        Edge edge = new Edge(Math.random(), new int[]{i-1, k}, new int[]{i, j});
                        edge.setStart(allNeurons[i-1][k]);
                        edge.setEnd(allNeurons[i][j]);
                        allEdges[k] = edge;
                    }

                    if(i == allNeurons.length-1) {
                        allNeurons[i][j] = new OutputNeuron(new int[]{i,j},0, allEdges);
                    }else{
                        allNeurons[i][j] = new Neuron(new int[]{i,j}, 0, allEdges);
                    }
                }
            }
        }
        return new AI(1, allNeurons);
    }

    private void setNeuronValues(Piece[][] board){
        for(int i = 0; i<allNeurons.length; i++){
            for(int j = 0; j<allNeurons[i].length; j++){
                allNeurons[i][j].setValue(0);
                allNeurons[i][j].setCalculated(false);
            }
        }

        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                if(board[i][j] != null){
                    int[] b = getBinary(board[i][j]);
                    for(int k = 0; k<b.length; k++){
                        if(isWhite()) {
                            allNeurons[0][i + 8*j + k].setValue(b[k]);
                        }else {
                            allNeurons[0][allNeurons[0].length-1 - 1 - i - 8*j - 7 + k].setValue(b[k]);
                        }
                    }
                }
            }
        }

        if(white){
            allNeurons[0][allNeurons[0].length-1].setValue(1);
        }
    }

    private int[] getBinary(Piece piece){
        int a = 0;
        switch (piece.getClass().getSimpleName()) {
            case "King":
                a = 0+piece.getId();
                break;
            case "Queen":
                a = 1+piece.getId();
                break;
            case "Rook":
                a = 1+9+piece.getId();
                break;
            case "Bishop":
                a = 1+9+10*piece.getId();
                break;
            case "Knight":
                a = 1+9+10+10+piece.getId();
                break;
            case "Pawn":
                a = 1+9+10+10+10+piece.getId();
                break;
        }

        String b = Integer.toBinaryString(a);
        int[] o = new int[7];
        for(int i = 0; i<b.length(); i++){
            o[o.length-1-i] = Character.getNumericValue(b.getBytes()[b.length()-1-i]);
        }

        if(piece.isWhite()) {
            o[0] = 0;
        }else{
            o[0] = 1;
        }
        return o;
    }

    public void mutate(){
        generation++;
        for(int i = 0; i<allNeurons.length; i++){
            for(int j = 0; j<allNeurons[i].length; j++){
                allNeurons[i][j].mutate(0.5, 0.5);
            }
        }
    }

    public static AI loadAI(){
        File file = new File(AI.PATH);
        if (file.exists()) {
            try {
                InputStream stream = new FileInputStream(PATH);
                return deserialize(stream);
            }catch (Exception e){
                return AI.setUpNeuralNetwork(new int[]{500, 500, 500});
            }
        }else {
            return AI.setUpNeuralNetwork(new int[]{500, 500, 500});
        }
    }

    private static AI deserialize(InputStream stream){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Neuron[][].class, new AllNeuronsAdapter());
        builder.registerTypeAdapter(Edge[][][].class, new AllEdgesAdapter());
        Gson gson = builder.create();

        int generation = 0;
        Neuron[][] allNeurons = new Neuron[0][0];
        Edge[][][] allEdges = new Edge[0][0][0];

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));


                reader.beginObject();
                String fieldname = null;

                while (reader.hasNext()) {
                    JsonToken token = reader.peek();

                    if (token.equals(JsonToken.NAME)) {
                        //get the current token
                        fieldname = reader.nextName();
                    }

                    if ("generation".equals(fieldname)) {
                        //move to next token
                        token = reader.peek();
                        generation = (reader.nextInt());
                    }

                    if ("allNeurons".equals(fieldname)) {
                        //move to next token
                        token = reader.peek();
                        Type typeOfN = new TypeToken<Neuron[][]>() {
                        }.getType();
                        allNeurons = gson.fromJson(reader, typeOfN);
                    }

                    if ("allEdges".equals(fieldname)) {
                        //move to next token
                        token = reader.peek();
                        Type typeOfE = new TypeToken<Edge[][][]>() {
                        }.getType();
                        allEdges = gson.fromJson(reader, typeOfE);
                    }
                }

                reader.endObject();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i<allEdges.length; i++){
            for(int j = 0; j<allEdges[i].length; j++){
                allNeurons[i][j].setAllEdges(allEdges[i][j]);
            }
        }
        for(int i = 0; i<allNeurons.length; i++){
            for(int j = 0; j<allNeurons[i].length; j++){
                for(int k = 0; k<allNeurons[i][j].getAllEdges().length; k++){
                    Edge edge = allNeurons[i][j].getAllEdges()[k];
                    edge.setStart(allNeurons[i - 1][k]);
                    edge.setEnd(allNeurons[i][j]);
                }
            }
        }

        return new AI(generation, allNeurons);
    }

    public static void saveAI(AI ai){
        try {
            OutputStream stream = new FileOutputStream(PATH, true);
            serialize(stream, ai);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void serialize(OutputStream stream, AI ai){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Neuron[][].class, new AllNeuronsAdapter());
        builder.registerTypeAdapter(Edge[][][].class, new AllEdgesAdapter());
        Gson gson = builder.create();

        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(stream, StandardCharsets.UTF_8));
            writer.setIndent(" ");

            writer.beginObject();
            writer.name("generation");
            writer.value(ai.generation);

            writer.name("allNeurons");
            Type typeOfN = new TypeToken<Neuron[][]>(){}.getType();
            gson.toJson(ai.allNeurons, typeOfN, writer);


            Edge[][][] allEdges = new Edge[ai.allNeurons.length][][];
            for(int i = 0; i<allEdges.length; i++){
                allEdges[i] = new Edge[ai.allNeurons[i].length][];
                for(int j = 0; j<ai.allNeurons[i].length; j++){
                    allEdges[i][j] = ai.allNeurons[i][j].getAllEdges();
                }
            }

            writer.name("allEdges");
            Type typeOfE = new TypeToken<Edge[][][]>(){}.getType();
            gson.toJson(allEdges, typeOfE, writer);

            writer.endObject();
            writer.close();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Neuron[][] getAllNeurons() {
        return allNeurons;
    }

    public void setAllNeurons(Neuron[][] allNeurons) {
        this.allNeurons = allNeurons;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public static AI copy(AI ai){
        Neuron[][] allNeurons = new Neuron[ai.allNeurons.length][];
        for(int i = 0; i<allNeurons.length; i++){
            allNeurons[i] = new Neuron[ai.allNeurons[i].length];
        }
        for(int i = 0; i<allNeurons.length; i++){
            for(int j = 0; j<allNeurons[i].length; j++){
                allNeurons[i][j] = Neuron.copy(ai.allNeurons[i][j]);
                for(int k = 0; k<allNeurons[i][j].getAllEdges().length; k++){
                    Edge edge = allNeurons[i][j].getAllEdges()[k];
                    edge.setStart(allNeurons[i-1][k]);
                    edge.setEnd(allNeurons[i][j]);
                }
            }
        }

        AI o = new AI(ai.generation, allNeurons);
        return o;
    }
}
