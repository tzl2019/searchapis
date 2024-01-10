package com.pku.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionUtils {
    public <T> List<T> getIntersection(List<List<T>> listOfLists) {
        if (listOfLists == null || listOfLists.isEmpty()) {
            return new ArrayList<>();
        }

        Set<T> intersection = listOfLists.stream()
                .flatMap(List::stream)
                .distinct()
                .filter(element -> listOfLists.stream().allMatch(list -> list.contains(element)))
                .collect(Collectors.toSet());

        return new ArrayList<>(intersection);
    }
    public <T> List<T> getUnion(List<List<T>> listOfLists) {
        if (listOfLists == null || listOfLists.isEmpty()) {
            return new ArrayList<>();
        }

        return listOfLists.stream()
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
        // 从列表中随机选择指定数量的元素
    public <T> List<T> selectRandomElements(List<T> list, int count) {
        Random random = new Random();
        if(count > list.size()){count = 1;}
        List<T> selectedElements = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(list.size());
            selectedElements.add(list.get(randomIndex));
        }

        return selectedElements;
    }

    // 通过值获取对应的键
    public <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
