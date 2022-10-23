package com.example.chess.model;

import java.io.Serializable;

public class Neuron implements Serializable {

    protected int[] id;

    protected double value = -1;
    protected double bias;
    protected boolean calculated;

    protected Edge[] allEdges;

    public Neuron(int[] id, double bias, Edge[] allEdges){
        this.id = id;
        this.bias = bias;
        this.allEdges = allEdges;
    }

    public int[] getId() {
        return id;
    }

    public void setId(int[] id) {
        this.id = id;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public Edge[] getAllEdges() {
        return allEdges;
    }

    public void setAllEdges(Edge[] allEdges) {
        this.allEdges = allEdges;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public void setValue(double value){
        this.value = value;
    }

    public double getValue(){
        if(!calculated) {
            double tempValue = 0;
            for (int i = 0; i < allEdges.length; i++) {
                tempValue += allEdges[i].getValue();
            }
            tempValue -= bias;
            value = sigmoid(tempValue);

            calculated = true;
        }

        return value;
    }

    private double sigmoid(double value){
        return 1/(1+Math.pow(Math.E, -value));
    }

    public void mutate(double factor1, double factor2){
        bias = bias+1*(1-factor1/2+Math.random()*factor1);
        for(int i = 0; i<allEdges.length; i++){
            allEdges[i].mutate(factor2);
        }
    }

    public static Neuron copy(Neuron neuron){
        Edge[] allEdges = new Edge[neuron.allEdges.length];

        for(int i = 0; i<allEdges.length; i++){
            allEdges[i] = Edge.copy(neuron.allEdges[i]);
        }

        return new Neuron(new int[]{neuron.id[0], neuron.id[1]}, neuron.bias, allEdges);
    }
}
