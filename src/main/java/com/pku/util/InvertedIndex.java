package com.pku.util;

import java.util.*;
import com.pku.model.*;

public class InvertedIndex {
    private Map<String, List<Node>> invertedIndex;

    public InvertedIndex(Set<Node> nodes) {
        invertedIndex = new HashMap<>();
        buildInvertedIndex(nodes);
    }

    private void buildInvertedIndex(Set<Node> nodes) {
        for (Node node : nodes) {
            String category = node.getCategory();
            invertedIndex.computeIfAbsent(category, k -> new ArrayList<>()).add(node);
        }
    }

    public List<Node> search(String keyword) {
        if (invertedIndex.containsKey(keyword)) {
            return invertedIndex.get(keyword);
        }
        return Collections.emptyList();
    }
}