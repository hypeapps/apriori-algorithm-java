package pl.hypeapp;

import org.apache.commons.lang3.tuple.Pair;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    private static List<String[]> transactionBaseCsv;
    private static AprioriAlgorithm aprioriAlgorithm;
    private static String PDF_SAVE_FOLDER = "C:\\";

    public static void main(String[] args) {
        //Change constructor parameter to your localization of .csv file
        CsvReader csvReader = new CsvReader("D:\\programy\\transaction-base.csv");
        float minimumSupport = 50.0f;
        float minimumConfidence = 50.0f;
        try {
            transactionBaseCsv = csvReader.readCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }
        aprioriAlgorithm = new AprioriAlgorithm(minimumSupport, minimumConfidence, transactionBaseCsv);
        savePdf(aprioriAlgorithm.generateSuperFormula(), Pair.of(aprioriAlgorithm.getMinimumSupport(), aprioriAlgorithm.getMinimumConfidence()));
    }

    public static void savePdf(List<Pair<String, Float>> strongRules, Pair<Float, Float> supportConfidencePair) {
        AprioriPdfGenerator aprioriPdfGenerator = new AprioriPdfGenerator(strongRules, supportConfidencePair);
        try {
            aprioriPdfGenerator.generateDocument(new FileOutputStream(PDF_SAVE_FOLDER));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
