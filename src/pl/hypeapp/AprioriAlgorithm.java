package pl.hypeapp;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class AprioriAlgorithm {
    private float minimumSupport;
    private int support;
    private float minimumConfidence;
    private List<String[]> transactionBase;
    private HashMap<String[], Integer> mostFrequentSets;
    private LinkedList<Pair<String[], Integer>> itemsWithMinSupport;
    private List<Pair<String, Float>> strongRules;
    private FrequentItems frequentItems;

    public AprioriAlgorithm(float minimumSupport, float minimumConfidence, List<String[]> transactionBase) {
        this.transactionBase = transactionBase;
        this.minimumConfidence = minimumConfidence / 100.0f;
        this.minimumSupport = minimumSupport / 100.0f;
        this.mostFrequentSets = new HashMap<>();
        this.itemsWithMinSupport = new LinkedList<>();
        this.strongRules = new ArrayList<>();
        this.support = (int) (this.transactionBase.size() * (minimumSupport / 100.0f));
    }

    public float getMinimumSupport() {
        return this.minimumSupport;
    }

    public void setMinimumSupport(float minimumSupport) {
        this.minimumSupport = minimumSupport;
    }

    public float getMinimumConfidence() {
        return this.minimumConfidence;
    }

    public void setMinimumConfidence(float minimumConfidence) {
        this.minimumConfidence = minimumConfidence;
    }

    public List<Pair<String, Float>> generateSuperFormula() {
        frequentItems = new FrequentItems(transactionBase);
        mostFrequentSets.putAll(getMostFrequentSets(frequentItems.getItemSetsFrequency()));
        return generateStrongRulesWithMinConfidence(mostFrequentSets);
    }

    private HashMap<String[], Integer> getMostFrequentSets(List<HashMap<String[], Integer>> itemSetsFrequency) {
        LinkedList<Pair<String[], Integer>> mostFrequentItemsWithMinSupport = getMostFrequentItemsWithMinSupport(itemSetsFrequency);
        HashMap<String[], Integer> mostFrequentSets = new HashMap<>();
        int lengthOfLastMostFrequentSet = mostFrequentItemsWithMinSupport.getLast().getLeft().length;
        mostFrequentItemsWithMinSupport.forEach((mostFrequentSet) -> {
            if (mostFrequentSet.getLeft().length == lengthOfLastMostFrequentSet) {
                mostFrequentSets.put(mostFrequentSet.getLeft(), mostFrequentSet.getRight());
            }
        });
        System.out.println("---- MOST FREQUENT SETS IN TRANSACTION BASE WITH MIN SUPPORT ----");
        mostFrequentSets.forEach((k, v) -> System.out.println(Arrays.toString(k) + " " + v));
        return mostFrequentSets;
    }

    private LinkedList<Pair<String[], Integer>> getMostFrequentItemsWithMinSupport(List<HashMap<String[], Integer>> itemSetsFrequency) {
        itemSetsFrequency.forEach((itemFrequency) ->
                itemFrequency.forEach((item, frequency) -> {
                    if (frequency >= support) {
                        Pair<String[], Integer> itemFrequencyMap = Pair.of(item, frequency);
                        itemsWithMinSupport.add(itemFrequencyMap);
                    }
                }));
        System.out.println("---- MOST FREQUENT ITEMS IN TRANSACTION BASE WITH MIN SUPPORT ----");
        itemsWithMinSupport.forEach((itemFrequencySet) -> System.out.println(Arrays.toString(itemFrequencySet.getLeft()) + " " + itemFrequencySet.getRight()));
        return itemsWithMinSupport;
    }

    private List<Pair<String, Float>> generateStrongRulesWithMinConfidence(HashMap<String[], Integer> mostFrequentSets) {
        List<HashMap<String[], String[]>> lattice = generateLattice(mostFrequentSets);
        int iterator = 0;
        for (Map.Entry<String[], Integer> mostFrequentSetFrequency : mostFrequentSets.entrySet()) {
            lattice.get(iterator).forEach((k, v) -> itemsWithMinSupport.forEach((itemFrequencyPair) -> {
                if (Arrays.asList(itemFrequencyPair.getLeft()).equals(Arrays.asList(k))) {
                    float confidence = calculateConfidence(mostFrequentSetFrequency.getValue(), itemFrequencyPair.getRight());
                    if (confidence >= minimumConfidence) {
                        String rule = createRule(k, v);
                        strongRules.add(Pair.of(rule, confidence));
                    }
                }
            }));
            iterator += 1;
        }
        System.out.println("---- STRONG RULES ----");
        strongRules.forEach((rule) -> {
            System.out.println(rule.getLeft() + " " + rule.getRight());
        });
        return strongRules;
    }

    private float calculateConfidence(Integer xy, Integer x) {
        return (float) xy / x;
    }

    private String createRule(String[] left, String[] right) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Arrays.toString(left)).append(" -> ").append(Arrays.toString(right));
        return stringBuffer.toString();
    }

    private List<HashMap<String[], String[]>> generateLattice(HashMap<String[], Integer> mostFrequentSetsWithFrequency) {
        List<LinkedList<String[]>> powerSets = new ArrayList<>();
        List<HashMap<String[], String[]>> lattice = new ArrayList<>();
        List<String[]> mostFrequentSets = new ArrayList<>();
        mostFrequentSetsWithFrequency.forEach((k, v) -> mostFrequentSets.add(k));
        mostFrequentSets.forEach((mostFrequentSet) -> {
            Set<String> set = new HashSet<>();
            Collections.addAll(set, mostFrequentSet);
            powerSets.add((LinkedList) frequentItems.generatePowerSet(set));
        });
        int iterator = 0;
        for (LinkedList<String[]> powerSet : powerSets) {
            powerSet.removeLast();
            Set<String> mostFrequentSet = new HashSet<>(Arrays.asList(mostFrequentSets.get(iterator)));
            HashMap<String[], String[]> latticeItem = new HashMap<>();
            for (String[] itemInPowerSet : powerSet) {
                Set<String> powerItemSet = new HashSet<>(Arrays.asList(itemInPowerSet));
                Set<String> differenceSet = Sets.difference(mostFrequentSet, powerItemSet);
                String[] diff = differenceSet.toArray(new String[differenceSet.size()]);
                latticeItem.put(itemInPowerSet, diff);
            }
            lattice.add(latticeItem);
            iterator += 1;
        }
        System.out.println("---- LATTICE ----");
        lattice.forEach((singleLattice) -> singleLattice.forEach((k, v) -> System.out.println(Arrays.toString(k) + " -> " + Arrays.toString(v))));
        return lattice;
    }
}
