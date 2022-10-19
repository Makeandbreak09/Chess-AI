package com.example.chess.model.customAdapter;

import com.example.chess.model.Edge;
import com.example.chess.model.Neuron;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AllEdgesAdapter extends TypeAdapter<Edge[][][]> {
    @Override
    public Edge[][][] read(JsonReader reader) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Edge.class, new EdgeAdapter());
        Gson gson = builder.create();

        ArrayList<Edge> allEdges = new ArrayList<>();
        reader.beginObject();
        String fieldname = null;

        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.NAME)) {
                //get the current token
                fieldname = reader.nextName();
            }
            if("edge".equals(fieldname)) {
                //move to next token
                token = reader.peek();
                Type typeOfN = new TypeToken<Edge>(){}.getType();
                allEdges.add(gson.fromJson(reader.nextString(), typeOfN));

            }
        }
        reader.endObject();

        int max = 0;
        for(int i = 0; i<allEdges.size(); i++){
            if(allEdges.get(i).getEndPos()[0]+1>max) {
                max = allEdges.get(i).getEndPos()[0]+1;
            }
        }

        int[] max2 = new int[max];
        for(int i = 0; i<allEdges.size(); i++){
            if(allEdges.get(i).getEndPos()[1]+1>max2[allEdges.get(i).getEndPos()[0]]) {
                max2[allEdges.get(i).getEndPos()[0]] = allEdges.get(i).getEndPos()[1]+1;
            }
            if(allEdges.get(i).getStartPos()[1]+1>max2[allEdges.get(i).getStartPos()[0]]) {
                max2[allEdges.get(i).getStartPos()[0]] = allEdges.get(i).getStartPos()[1]+1;
            }
        }

        Edge[][][] o = new Edge[max][][];
        for(int i = 0; i<o.length; i++){
            if(i-1<0) {
                o[i] = new Edge[max2[i]][0];
            }else {
                o[i] = new Edge[max2[i]][max2[i - 1]];
            }
        }

        for(int i = 0; i<allEdges.size(); i++){
            o[allEdges.get(i).getEndPos()[0]][allEdges.get(i).getEndPos()[1]][allEdges.get(i).getStartPos()[1]] = allEdges.get(i);
        }

        return o;
    }

    @Override
    public void write(JsonWriter writer, Edge[][][] allEdges) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Edge.class, new EdgeAdapter());
        Gson gson = builder.create();
        Type typeOfT = new TypeToken<Edge>() {}.getType();

        writer.beginObject();
        for(int i = 0; i<allEdges.length; i++) {
            for(int j = 0; j<allEdges[i].length; j++) {
                for(int k = 0; k<allEdges[i][j].length; k++) {
                    writer.name("edge");

                    writer.value(gson.toJson(allEdges[i][j][k], typeOfT));
                }
            }
        }

        writer.endObject();
    }
}