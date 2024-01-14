package com.pku.model;

import java.util.*;

public class Graph {

    public Map<String, Node> apiNodeMap;
    public Map<Edge, Integer> edgeWeightMap;
    public Map<String, List<Node>> categoryNodeMap;
    public List<Node> nodeList;
    public int edgeWeightMaximum = 100;
    public int nodeCount;

    public Graph() {
        nodeList = new ArrayList<Node>();
        edgeWeightMap = new HashMap<>();
        apiNodeMap=new HashMap<>();
        categoryNodeMap=new HashMap<>();
    }

    public Graph(List<String> apis, List<String> categories, List<String> edge_source, List<String> edge_target) {

        nodeCount = apis.size();
        nodeList = new ArrayList<Node>();
        edgeWeightMap = new HashMap<>();
        apiNodeMap=new HashMap<>();
        categoryNodeMap=new HashMap<>();

        for (int i = 0; i < nodeCount; i++) {
            Node node=new Node(apis.get(i), categories.get(i),i);
            nodeList.add(node);

            apiNodeMap.put(apis.get(i), node);
            categoryNodeMap.computeIfAbsent(categories.get(i), k -> new ArrayList<>()).add(node);
        }
        
        for (int i = 0; i < edge_source.size(); i++) {

            if (apiNodeMap.containsKey(edge_source.get(i)) == false ||
                    apiNodeMap.containsKey(edge_target.get(i)) == false) {
                continue;
            }
            Node source = apiNodeMap.get(edge_source.get(i));
            Node target = apiNodeMap.get(edge_target.get(i));

            edgeWeightMap.compute(new Edge(source, target), (key, value) -> (value == null) ? 1 : value + 1);
            edgeWeightMap.compute(new Edge(target, source), (key, value) -> (value == null) ? 1 : value + 1);
        }
        edgeWeightMap.values().forEach((value) -> edgeWeightMaximum =
        Math.max(edgeWeightMaximum, value));

        edgeWeightMap.forEach((edge, weight) -> {
            Node source = edge.getSource();
            Node target = edge.getTarget();
            Edge newEdge=new Edge(source,target,edgeWeightMaximum - weight);
            source.neighborEdge.add(newEdge);
        });
        System.out.println("关键字个数："+categoryNodeMap.size());
        System.out.println("API个数："+nodeCount);
        System.out.println("共同出现的API对："+edgeWeightMap.entrySet().size());
    }

    //getter
    public Map<String, Node> getApiNodeMap(){
        return apiNodeMap;
    }
    public Map<Edge, Integer> getEdgeWeightMap(){
        return edgeWeightMap;
    }
    public int getNodeCount(){
        return nodeCount;
    }
}
