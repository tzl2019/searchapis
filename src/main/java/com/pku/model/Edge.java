package com.pku.model;

import java.util.Objects;

public class Edge {
    private Node source;
    private Node target;
    private int weight;

    public Edge(Node source, Node target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }
    public Edge(Node source, Node target) {
        this.source = source;
        this.target = target;
    }
    public Node getSource() {
        return source;
    }
    public Node getTarget() {
        return target;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.source) + Objects.hash(this.target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge other = (Edge) o;
        return (this.source.equals(other.source) && this.target.equals(other.target)) || (this.source.equals(other.target) && this.target.equals(other.source));
    }

}
