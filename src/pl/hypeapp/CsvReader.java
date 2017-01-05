package pl.hypeapp;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CsvReader {
    private String csvFile;
    private FileReader fileReader;
    private List<String[]> csvReadResults = Collections.<String[]>emptyList();
    private CSVReader csvReader;

    public CsvReader(String csvFile) {
        this.csvFile = csvFile;
    }

    public List<String[]> getCsvReadResult() {
        return this.csvReadResults;
    }

    public void setCsvReadResults(List<String[]> csvReadResults) {
        this.csvReadResults = csvReadResults;
    }

    public List<String[]> readCsv() throws IOException {
        this.fileReader = new FileReader(csvFile);
        this.csvReader = new CSVReader(fileReader);
        this.csvReadResults = csvReader.readAll();
        return csvReadResults;
    }
}
