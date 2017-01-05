package pl.hypeapp;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AprioriAlgorithm {
    private float minimumSupport;
    private int support;
    private float confidence;
    private List<String[]> transactionBase;
    private List<String> mostFrequentOneItemSet;

    public AprioriAlgorithm(float minimumSupport, float confidence, List<String[]> transactionBase) {
        this.transactionBase = transactionBase;
        this.confidence = confidence;
        this.minimumSupport = minimumSupport;
        this.support = (int) (this.transactionBase.size() * (minimumSupport / 100.0f));
    }

    public float getMinimumSupport() {
        return this.minimumSupport;
    }

    public void setMinimumSupport(float minimumSupport) {
        this.minimumSupport = minimumSupport;
    }

    public float getConfidence() {
        return this.confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public void generateSuperFormula() {
        mostFrequentOneItemSet = getUniqueItemsWithSupport(support);
        mostFrequentOneItemSet.forEach(System.out::println);
    }

    private List<String> getUniqueItemsWithSupport(int support) {
        List<String> uniqueItems = new ArrayList<>();
        HashMap<String, Integer> frequentOneItemSet = generateFrequentOneItemSet();
        frequentOneItemSet.forEach((item, frequency) -> {
            if (frequency >= support) uniqueItems.add(item);
        });
        return uniqueItems;
    }

    private HashMap<String, Integer> generateFrequentOneItemSet() {
        List<String> allItems = getAllItems();
        HashMap<String, Integer> frequentOfItems = new HashMap<>();
        allItems.forEach((item) -> {
            if (!frequentOfItems.containsKey(item)) {
                frequentOfItems.put(item, Collections.frequency(allItems, item));
            }
        });
        frequentOfItems.forEach((item, freq) -> System.out.println(item + " " + freq));
        return frequentOfItems;
    }

    private List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        transactionBase.forEach((transactionItem) -> {
            for (String item : transactionItem) {
                items.add(item);
            }
        });
        return items;
    }

    private List<Pair<String, String>> generatePairs(List<String> items) {
        List<Pair<String, String>> pairs = new ArrayList<>();
        items.forEach((item) -> {

        });
        return null;
    }
}
