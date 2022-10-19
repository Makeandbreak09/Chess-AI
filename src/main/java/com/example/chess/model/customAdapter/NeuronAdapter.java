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

import java.io.*;
import java.lang.reflect.Type;

public class NeuronAdapter extends TypeAdapter<Neuron> {
    @Override
    public Neuron read(JsonReader reader) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type typeOfT = new TypeToken<int[]>(){}.getType();

        String fieldname = null;

        int[] id = new int[0];
        double bias = 0;
        Edge[] allEdges = new Edge[0];

        reader.beginObject();
        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.NAME)) {
                //get the current token
                fieldname = reader.nextName();
            }

            if ("id".equals(fieldname)) {
                //move to next token
                token = reader.peek();
                id = gson.fromJson(reader.nextString(), typeOfT);
            }

            if("bias".equals(fieldname)) {
                //move to next token
                token = reader.peek();
                bias = (reader.nextDouble());
            }
        }
        reader.endObject();
        Neuron neuron = new Neuron(id, bias, allEdges);

        return neuron;
    }

    @Override
    public void write(JsonWriter writer, Neuron neuron) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type typeOfT = new TypeToken<int[]>(){}.getType();

        writer.beginObject();

        writer.name("id");
        writer.value(gson.toJson(neuron.getId(), typeOfT));

        writer.name("bias");
        writer.value(neuron.getBias());

        writer.endObject();
    }
}