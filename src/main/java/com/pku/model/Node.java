package com.pku.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node {
    public int id;
    private String name;
    private String category;
    public Set<Node> neighbors;
    public Set<Edge> neighborEdge;
    public Node(String name, String category) {
        this.name = name;
        this.category = category;
        this.neighbors = new HashSet<Node>();
    }
    public Node(String name, String category,int id) {
        this.name = name;
        this.category = category;
        this.id=id;
        this.neighborEdge = new HashSet<Edge>();
    }
    public Node(String name) {
        this.name = name;
        this.category = null;
        this.neighbors = new HashSet<Node>();
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
    public Set<Node> getNeighbors() {
        return this.neighbors;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    @Override
    public String toString(){
        return this.name;
    }
        @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Node other = (Node) o;
        return this.name.equals(other.getName());
    }
}
