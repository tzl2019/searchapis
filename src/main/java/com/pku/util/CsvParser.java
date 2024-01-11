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
            elems.remove(0); // 列头

        } catch (IOException e) {
            e.printStackTrace();
        }

        return elems;
    }

    public boolean getAPICalls(String inputFile, String outputFile) {
        if (Files.exists(Paths.get(outputFile))) {
            return true;
        }

        List<String> mashupApis = readSpecColumn(inputFile, 2);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String apisString : mashupApis) {
                List<String> apis = Arrays.asList(apisString.split(","));
                for (int i = 0; i < apis.size(); i++) {
                    for (int j = i + 1; j < apis.size(); j++) {
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
        if (Files.exists(Paths.get(outputFile))) {
            return true;
        }

        List<String> apis = readSpecColumn(inputFile, 1);
        List<String> categories = readSpecColumn(inputFile, 3);
        if (apis.size() != categories.size()) {
            System.err.println("Error when parsing csv file");
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("apis,cats");
            writer.newLine();
            for (int i = 0; i < apis.size(); i++) {
                writer.write(apis.get(i) + "," + categories.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Graph getGraph(String apiRelationshipFile, String apiCategoryFile) {

        Graph graph = new Graph(readSpecColumn(apiCategoryFile, 0), readSpecColumn(apiCategoryFile, 1),
                readSpecColumn(apiRelationshipFile, 0), readSpecColumn(apiRelationshipFile, 1));

        return graph;
    }
}
