package com.example.chess.model.customAdapter;

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

public class AllNeuronsAdapter extends TypeAdapter<Neuron[][]> {
    @Override
    public Neuron[][] read(JsonReader reader) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Neuron.class, new NeuronAdapter());
        Gson gson = builder.create();

        ArrayList<Neuron> allNeurons = new ArrayList<>();

        reader.beginObject();
        String fieldname = null;

        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.NAME)) {
                //get the current token
                fieldname = reader.nextName();
            }

            if("neuron".equals(fieldname)) {
                //move to next token
                token = reader.peek();

                Type typeOfN = new TypeToken<Neuron>(){}.getType();
                allNeurons.add(gson.fromJson(reader.nextString(), typeOfN));
            }
        }
        reader.endObject();

        int max = 0;
        for(int i = 0; i<allNeurons.size(); i++){
            if(allNeurons.get(i).getId()[0]>max) {
                max = allNeurons.get(i).getId()[0]+1;
            }
        }

        int[] max2 = new int[max];
        for(int i = 0; i<allNeurons.size(); i++){
            if(allNeurons.get(i).getId()[1]+1>max2[allNeurons.get(i).getId()[0]]) {
                max2[allNeurons.get(i).getId()[0]] = allNeurons.get(i).getId()[1]+1;
            }
        }

        Neuron[][] o = new Neuron[max][0];
        for(int i = 0; i<o.length; i++){
                o[i] = new Neuron[max2[i]];
        }

        for(int i = 0; i<allNeurons.size(); i++){
            o[allNeurons.get(i).getId()[0]][allNeurons.get(i).getId()[1]] = allNeurons.get(i);
        }

        return o;
    }

    @Override
    public void write(JsonWriter writer, Neuron[][] allNeurons) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Neuron.class, new NeuronAdapter());
        Gson gson = builder.create();
        Type typeOfT = new TypeToken<Neuron>() {}.getType();

        writer.beginObject();
        for(int i = 0; i<allNeurons.length; i++) {
            for(int j = 0; j<allNeurons[i].length; j++) {
                writer.name("neuron");

                writer.value(gson.toJson(allNeurons[i][j], typeOfT));
            }
        }
        writer.endObject();
    }
}