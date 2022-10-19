package com.example.chess.model.customAdapter;

import com.example.chess.model.Edge;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class EdgeAdapter extends TypeAdapter<Edge> {
    @Override
    public Edge read(JsonReader reader) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type typeOfT = new TypeToken<int[]>(){}.getType();

        reader.beginObject();
        String fieldname = null;

        double weight = 0;
        int[] startPos = null;
        int[] endPos = null;

        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.NAME)) {
                //get the current token
                fieldname = reader.nextName();
            }

            if ("weight".equals(fieldname)) {
                //move to next token
                token = reader.peek();
                weight = (reader.nextDouble());
            }

            if("startPos".equals(fieldname)) {
                //move to next token
                token = reader.peek();
                startPos = gson.fromJson(reader.nextString(), typeOfT);
            }

            if("endPos".equals(fieldname)) {
                //move to next token
                token = reader.peek();
                endPos = gson.fromJson(reader.nextString(), typeOfT);
            }
        }
        Edge edge = new Edge(weight, startPos, endPos);

        reader.endObject();
        return edge;
    }

    @Override
    public void write(JsonWriter writer, Edge edge) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type typeOfT = new TypeToken<int[]>(){}.getType();

        writer.beginObject();

        writer.name("weight");
        writer.value(edge.getWeight());

        writer.name("startPos");
        writer.value(gson.toJson(edge.getStartPos(), typeOfT));

        writer.name("endPos");
        writer.value(gson.toJson(edge.getEndPos(), typeOfT));

        writer.endObject();
    }
}