package pl.hypeapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main extends Application {
    static List<String[]> transactionBaseCsv;
    Stage stage;
    static AprioriAlgorithm aprioriAlgorithm;
    private static String FILE = "D:\\programy\\strong-rules.pdf";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage = primaryStage;
        primaryStage.setTitle("Apriori Algorithm");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public void openCsvFile(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
        }
    }

    public static void main(String[] args) {
//        launch(args);
        CsvReader csvReader = new CsvReader("D:\\programy\\transaction-base.csv");
        try {
            transactionBaseCsv = csvReader.readCsv();
        } catch (IOException e) {
            e.printStackTrace();
        }
        aprioriAlgorithm = new AprioriAlgorithm(50.0f, 50.0f, transactionBaseCsv);
        savePdf(aprioriAlgorithm.generateSuperFormula(), Pair.of(aprioriAlgorithm.getMinimumSupport(), aprioriAlgorithm.getConfidence()));
    }

    public static void savePdf(List<Pair<String, Float>> strongRules, Pair<Float, Float> supportConfidencePair) {
        try {
            AprioriPdfGenerator aprioriPdfGenerator = new AprioriPdfGenerator(strongRules, supportConfidencePair);
            aprioriPdfGenerator.generateDocument(new FileOutputStream(FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
