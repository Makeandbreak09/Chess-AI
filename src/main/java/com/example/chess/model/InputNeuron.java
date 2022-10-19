package com.example.chess.model;

public class InputNeuron extends Neuron{

    public InputNeuron(int[] id, double bias, Edge[] allEdges) {
        super(id, bias, allEdges);
    }

    public double getValue(){
        return value;
    }
}
