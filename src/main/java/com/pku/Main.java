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
        String mashupOutput = dataDir + "api_relationship.csv";
        String apisInput = dataDir + "api.csv";
        String apisOutput = dataDir + "api_category.csv";
        
        CsvParser csvParser = new CsvParser();
        csvParser.getAPICalls(mashupInput, mashupOutput);
        csvParser.getAPICats(apisInput, apisOutput);
        try {
            Graph graph = csvParser.getGraph(mashupOutput, apisOutput);

            // graph.buildInvertedIndex();
            Scanner sc = new Scanner(System.in);
            while (true) {
                
                System.out.println("结束测试请输入end，继续测试请输入关键词(以,分隔)： ");
                String input = sc.nextLine();
                if(input.contains("end")) break;
                
                String[] keywords = input.strip().split(",");
             
                MiniSteinerTree miniSteinerTree = new MiniSteinerTree(graph,keywords);
            }
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}