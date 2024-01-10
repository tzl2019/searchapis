package com.pku.util;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.*;
import com.pku.model.*;
public class CsvParser {
    private static List<String> readSpecColumn(String inputFile, int columnIndex) {

        List<String> elems = new ArrayList<>();
        try (Reader reader = new FileReader(inputFile);
                CSVParser csvParser = CSVFormat.DEFAULT.parse(reader)) {

            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.size() >= columnIndex + 1) {
                    String value = csvRecord.get(columnIndex).strip();
                    elems.add(value);
                }
            }
            elems.remove(0); //列头

        } catch (IOException e) {
            e.printStackTrace();
        }

        return elems;
    }
    public boolean getAPICalls(String inputFile, String outputFile) {
        if (Files.exists(Paths.get(outputFile))){ return true;}
        
        List<String> mashupApis = readSpecColumn(inputFile, 2);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for(String apisString: mashupApis){
                List<String> apis = Arrays.asList(apisString.split(","));
                for(int i = 0; i < apis.size(); i++){
                    for(int j = i + 1; j < apis.size(); j++){
                        writer.write(apis.get(i) + "," + apis.get(j));
                        writer.newLine();
                    }
                }      
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }   
        return true;
    }
    public boolean getAPICats(String inputFile, String outputFile) {
        if (Files.exists(Paths.get(outputFile))){ return true;}
        
        List<String> apis = readSpecColumn(inputFile, 1);
        List<String> categories = readSpecColumn(inputFile, 3);
        if (apis.size() != categories.size()) {
            System.err.println("Error when parsing csv file");
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("apis,cats");
            writer.newLine();
            for(int i = 0; i < apis.size(); i++){
                writer.write(apis.get(i) + "," + categories.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }   
        return true;
    }

    public Graph getGraph(String apiCallsFile, String apiCatsFile) {
        List<String> apisLeft = readSpecColumn(apiCallsFile, 0);
        List<String> apisRight = readSpecColumn(apiCallsFile, 1);
        List<String> apis = readSpecColumn(apiCatsFile, 0);
        List<String> categories = readSpecColumn(apiCatsFile, 1);
        if ((apisLeft.size() != apisRight.size()) || (apis.size() != categories.size())){
            System.err.println("Error when parsing csv file");
            return null;
        }
        Graph graph = new Graph();
        Map<String, String> apiCatMap = new HashMap<>();
        Map<Edge, Integer> edgeMap= new HashMap<Edge, Integer>();
        for (int i = 0; i < apisLeft.size(); i++) {
            Edge newEdge = new Edge(apisLeft.get(i), apisRight.get(i), 1);
            edgeMap.compute(newEdge, (key, value) -> (value == null)? 100: value - 1);
        }
        edgeMap.forEach((key, value) -> {
            graph.addEdge(key.getSource(), key.getTarget(), value);
        });
        for (int i = 0; i < apis.size(); i++) {
            int index = i;
            apiCatMap.compute(apis.get(index), (k,v) -> categories.get(index));
        }
        for (Node node: graph.getNodes()){
            node.setCategory(apiCatMap.get(node.getName()));
        }

        return graph;
    }
}
