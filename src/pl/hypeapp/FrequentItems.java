package pl.hypeapp;

import com.google.common.collect.Sets;

import java.util.*;

import static java.util.Arrays.asList;

public class FrequentItems {
    private List<String[]> inputItems;
    private Set<String> uniqueItems;
    private List<String[]> powerSet;

    public FrequentItems(List<String[]> items) {
        this.inputItems = items;
        this.uniqueItems = new HashSet<>();
        this.powerSet = new ArrayList<>();
    }

    public List<HashMap<String[], Integer>> getItemSetsFrequency() {
        uniqueItems = generateAllUniqueItems();
        powerSet = generatePowerSet(uniqueItems);
        return generateFrequencySets(powerSet);
    }

    private Set<String> generateAllUniqueItems() {
        inputItems.forEach((row) -> {
            asList(row).forEach((item) -> uniqueItems.add(item));
        });
        System.out.println("---- UNIQUE ITEMS IN SET ----");
        uniqueItems.forEach(System.out::println);
        return uniqueItems;
    }

    private List<String[]> generatePowerSet(Set<String> set) {
        List<String[]> powerSet = new ArrayList<>();
        Sets.powerSet(set).forEach((powerSetRow) -> {
            powerSet.add(powerSetRow.toArray(new String[powerSetRow.size()]));
        });
        powerSet.sort((o1, o2) -> o1.length > o2.length ? 1 : (o1.length < o2.length) ? -1 : 0);
        System.out.println("---- POWER SET ----");
        powerSet.forEach((item) -> System.out.println(Arrays.toString(item)));
        return powerSet;
    }

    private List<HashMap<String[], Integer>> generateFrequencySets(List<String[]> powerSet) {
        List<HashMap<String[], Integer>> itemFrequencySets = new ArrayList<>();
        powerSet.forEach((powerItemRow) -> {
            itemFrequencySets.add(calculateFrequency(powerItemRow));
        });
        System.out.println("---- FREQUNECY OF POWERSET RELATIVE TO INPUT ITEMSET ----");
        itemFrequencySets.forEach((i) -> i.forEach((k, v) -> System.out.println(Arrays.toString(k) + " FREQ " + v)));
        return itemFrequencySets;
    }

    private HashMap<String[], Integer> calculateFrequency(String[] powerRow) {
        HashMap<String[], Integer> frequencyOfRow = new HashMap<>();
        int frequency = 0;
        for (String[] inputRow : inputItems) {
            if (isContains(inputRow, powerRow)) {
                frequency += 1;
            }
        }
        frequencyOfRow.put(powerRow, frequency);
        return frequencyOfRow;
    }

    private boolean isContains(String[] inputRow, String[] powerRow) {
        int occurrence = 0;
        for (String inputItem : inputRow) {
            if (asList(powerRow).contains(inputItem)) {
                occurrence += 1;
            }
        }
        return occurrence == powerRow.length;
    }
}
