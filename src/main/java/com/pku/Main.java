package com.pku;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.pku.algorithm.MiniSteinerTree;
import com.pku.model.Graph;
import com.pku.model.Node;
import com.pku.util.*;  
public class Main {
    public static void main(String[] args) {
        String dataDir = "src/main/resources/data/";
        String mashupInput = dataDir + "mashup.csv";
        String mashupOutput = dataDir + "api_calls.csv";
        String apisInput = dataDir + "api.csv";
        String apisOutput = dataDir + "api_cats.csv";
        
        CsvParser csvParser = new CsvParser();
        csvParser.getAPICalls(mashupInput, mashupOutput);
        csvParser.getAPICats(apisInput, apisOutput);
        try {
            Graph graph = csvParser.getGraph(mashupOutput, apisOutput);
            graph.buildInvertedIndex();
            while (true) {
                Scanner sc = new Scanner(System.in);
                System.out.println("输入关键词： ");
                String input = sc.nextLine();
                if(input.contains("end")) break;
                
                String[] keywords = input.strip().split(" ");
                List<List<Node>> searchLists = new ArrayList<>();
                for (String keyword: keywords){
                    searchLists.add(graph.search(keyword));
                }
                List<Node> terminals = new ArrayList<>();
                CollectionUtils collectionUtils = new CollectionUtils();
                List<Node> union = collectionUtils.getUnion(searchLists);
                // for(Node node : instersection){
                //     System.out.println(node.getName());
                // }
                // for(Node node : union){
                //     System.out.println(node.getName());
                // }
                for (List<Node> list: searchLists){
                    terminals.addAll(collectionUtils.selectRandomElements(list, 1));
                }
                Graph subgraph = graph.buildSubGraph(union);
                MiniSteinerTree miniSteinerTree = new MiniSteinerTree();
                int[] path = miniSteinerTree.getMiniSteinerTree(subgraph, terminals);
                System.out.print("Web APIs: ");
                for (int i : path) {
                    System.out.print(collectionUtils.getKeyByValue(subgraph.nodeIdMap, i) + ", ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}