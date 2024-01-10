package com.pku.model;

import java.util.*;
import com.pku.util.InvertedIndex;
public class Graph {
    private Set<Node> nodes;
    private Set<Edge> edges;
    public Map<String, Integer> nodeIdMap;
    public Map<Edge, Integer> edgeWeightMap;
    private InvertedIndex invertedIndex;

    public Graph() {
        nodes = new HashSet<>();
        edges = new HashSet<>();
        nodeIdMap = new HashMap<>();
        edgeWeightMap = new HashMap<>();
    }

    public void addNode(String nodeName, String category) {
        Node newNode = new Node(nodeName, category);
        nodes.add(newNode);
        this.createNodeId();
    }

    public void addNodes(List<Node> nodes) {
        this.nodes.addAll(nodes);
        this.createNodeId();
    }
    
    public void addEdge(String node1Name, String node2Name, int weight) {
        Node node1 = new Node(node1Name);
        Node node2 = new Node(node2Name);
        if((!this.nodes.contains(node1)) || (!this.nodes.contains(node2))) { 
            this.nodes.add(node1);
            this.nodes.add(node2);
        }
        Edge newEdge = new Edge(node1Name, node2Name, weight);
        edges.add(newEdge);
        edgeWeightMap.compute(newEdge, (k,v) -> weight);
        node1.neighbors.add(node2);
        node2.neighbors.add(node1);
        createNodeId();
    }

    public void setInvertedIndex() {
        invertedIndex = new InvertedIndex(nodes);
    }

    public Set<Node> getNodes() {
        return nodes;
    }
    public Set<Edge> getEdges() {
        return edges;
    }
    public void createNodeId(){
        List<Node> tempNodes = new ArrayList<Node>(this.nodes);
        for (int i = 0; i < tempNodes.size(); i++) {
            int curId = i;
            tempNodes.get(i).id = curId;
            nodeIdMap.compute(tempNodes.get(i).getName(), (k,v) -> curId);
        }
    }
    public Graph buildSubGraph(List<Node> nodes) {
        Graph subGraph = new Graph();
        subGraph.addNodes(nodes);
        for (Node node: nodes){
            for (Node neighbor: node.getNeighbors()){
                subGraph.addEdge(node.getName(), neighbor.getName(), 0);
            }
        }
        for (Edge edge : subGraph.getEdges()) {
            int weight = this.edgeWeightMap.get(edge);
            edge.setWeight(weight);
            subGraph.edgeWeightMap.compute(edge, (k,v) -> weight);
        }
        return subGraph;
    }
    public void buildInvertedIndex(){
        invertedIndex = new InvertedIndex(nodes);
    }
    public List<Node> search(String keyword) {
        return invertedIndex.search(keyword);
    }

}