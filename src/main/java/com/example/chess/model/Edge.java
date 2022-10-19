package com.example.chess.model;

import java.io.Serializable;

public class Edge implements Serializable {

    private double weight;
    private int[] startPos, endPos;

    private Neuron start;
    private Neuron end;

    public Edge(double weight, int[] startPos, int[] endPos){
        this.weight = weight;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public double getValue(){
        return start.getValue()*weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Neuron getStart() {
        return start;
    }

    public void setStart(Neuron start) {
        this.start = start;
    }

    public Neuron getEnd() {
        return end;
    }

    public void setEnd(Neuron end) {
        this.end = end;
    }

    public int[] getStartPos() {
        return startPos;
    }

    public void setStartPos(int[] startPos) {
        this.startPos = startPos;
    }

    public int[] getEndPos() {
        return endPos;
    }

    public void setEndPos(int[] endPos) {
        this.endPos = endPos;
    }

    public void mutate(double factor){
        weight = weight*(1-factor/2+Math.random()*factor);
    }

    public static Edge copy(Edge edge){
        return new Edge(edge.weight, new int[]{edge.getStartPos()[0], edge.getStartPos()[1]}, new int[]{edge.getEndPos()[0], edge.getEndPos()[1]});
    }
}
