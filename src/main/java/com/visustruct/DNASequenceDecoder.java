package com.visustruct;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Queue;

public class DNASequenceDecoder {
    private Stage stage;
    private List<String> dnaSequenceHistory = new ArrayList<>();
    private static final Map<String, String> codonToAminoAcid = new HashMap<>();
    @FXML
    private TextField dnaSequenceInput;
    @FXML
    private TextArea aminoAcidOutput;

    public void setStage(Stage stage) {
        this.stage = stage;

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void initialize() {
        importHistory();
    }

    public void backHandler() {
        try {
            exportHistory();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("applications.fxml"));
            Parent root = loader.load();

            ApplicationsMenu applicationsMenu = loader.getController();
            applicationsMenu.setStage(stage);
            stage.setTitle("VisuaStruct - Applications Menu");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        codonToAminoAcid.put("AAA", "Lysine");
        codonToAminoAcid.put("AAC", "Asparagine");
        codonToAminoAcid.put("AAG", "Lysine");
        codonToAminoAcid.put("AAT", "Asparagine");

        codonToAminoAcid.put("ACA", "Threonine");
        codonToAminoAcid.put("ACC", "Threonine");
        codonToAminoAcid.put("ACG", "Threonine");
        codonToAminoAcid.put("ACT", "Threonine");

        codonToAminoAcid.put("AGA", "Arginine");
        codonToAminoAcid.put("AGC", "Serine");
        codonToAminoAcid.put("AGG", "Arginine");
        codonToAminoAcid.put("AGT", "Serine");

        codonToAminoAcid.put("ATA", "Isoleucine");
        codonToAminoAcid.put("ATC", "Isoleucine");
        codonToAminoAcid.put("ATG", "Methionine");
        codonToAminoAcid.put("ATT", "Isoleucine");

        codonToAminoAcid.put("CAA", "Glutamine");
        codonToAminoAcid.put("CAC", "Histidine");
        codonToAminoAcid.put("CAG", "Glutamine");
        codonToAminoAcid.put("CAT", "Histidine");

        codonToAminoAcid.put("CCA", "Proline");
        codonToAminoAcid.put("CCC", "Proline");
        codonToAminoAcid.put("CCG", "Proline");
        codonToAminoAcid.put("CCT", "Proline");

        codonToAminoAcid.put("CGA", "Arginine");
        codonToAminoAcid.put("CGC", "Arginine");
        codonToAminoAcid.put("CGG", "Arginine");
        codonToAminoAcid.put("CGT", "Arginine");

        codonToAminoAcid.put("CTA", "Leucine");
        codonToAminoAcid.put("CTC", "Leucine");
        codonToAminoAcid.put("CTG", "Leucine");
        codonToAminoAcid.put("CTT", "Leucine");

        codonToAminoAcid.put("GAA", "Glutamic acid");
        codonToAminoAcid.put("GAC", "Aspartic acid");
        codonToAminoAcid.put("GAG", "Glutamic acid");
        codonToAminoAcid.put("GAT", "Aspartic acid");

        codonToAminoAcid.put("GCA", "Alanine");
        codonToAminoAcid.put("GCC", "Alanine");
        codonToAminoAcid.put("GCG", "Alanine");
        codonToAminoAcid.put("GCT", "Alanine");

        codonToAminoAcid.put("GGA", "Glycine");
        codonToAminoAcid.put("GGC", "Glycine");
        codonToAminoAcid.put("GGG", "Glycine");
        codonToAminoAcid.put("GGT", "Glycine");

        codonToAminoAcid.put("GTA", "Valine");
        codonToAminoAcid.put("GTC", "Valine");
        codonToAminoAcid.put("GTG", "Valine");
        codonToAminoAcid.put("GTT", "Valine");

        codonToAminoAcid.put("TAA", "Stop");
        codonToAminoAcid.put("TAC", "Tyrosine");
        codonToAminoAcid.put("TAG", "Stop");
        codonToAminoAcid.put("TAT", "Tyrosine");

        codonToAminoAcid.put("TCA", "Serine");
        codonToAminoAcid.put("TCC", "Serine");
        codonToAminoAcid.put("TCG", "Serine");
        codonToAminoAcid.put("TCT", "Serine");

        codonToAminoAcid.put("TGA", "Stop");
        codonToAminoAcid.put("TGC", "Cysteine");
        codonToAminoAcid.put("TGG", "Tryptophan");
        codonToAminoAcid.put("TGT", "Cysteine");

        codonToAminoAcid.put("TTA", "Leucine");
        codonToAminoAcid.put("TTC", "Phenylalanine");
        codonToAminoAcid.put("TTG", "Leucine");
        codonToAminoAcid.put("TTT", "Phenylalanine");
    }

    @FXML
    public void decodeDNASequence() {
        String dnaSequence = dnaSequenceInput.getText();

        dnaSequenceHistory.add(dnaSequence);
        String aminoAcidSequence = decodeDNASequence(dnaSequence);
        aminoAcidOutput.setText(aminoAcidSequence);
    }

    private void showInvalidInputDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Invalid Input");
        dialog.setHeaderText("The DNA sequence is invalid. It should only contain 'A', 'T', 'C', or 'G' and its length should be a multiple of 3.");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.showAndWait();
    }

    public void searchHistory() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("DNA Sequence History");
        dialog.setHeaderText("Here is your DNA sequence history:");

        StringBuilder history = new StringBuilder();
        for (String dnaSequence : dnaSequenceHistory) {
            history.append(dnaSequence).append("\n");
        }

        TextArea textArea = new TextArea(history.toString());
        textArea.setEditable(false);
        dialog.getDialogPane().setContent(textArea);

        ButtonType clearButtonType = new ButtonType("Clear History");
        dialog.getDialogPane().getButtonTypes().addAll(clearButtonType, ButtonType.CLOSE);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == clearButtonType) {
                dnaSequenceHistory.clear();
            }
            return null;
        });

        dialog.showAndWait();
    }

    public void stop() {
        exportHistory();
    }

    public void exportHistory() {
        Path path = Paths.get("src/main/java/assets/history.txt");
        try {
            Files.write(path, dnaSequenceHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importHistory() {
        Path path = Paths.get("src/main/java/assets/history.txt");
        try {
            dnaSequenceHistory = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String decodeDNASequence(String dnaSequence) {
        Queue<String> codonQueue = new LinkedList<>();

        for (int i = 0; i < dnaSequence.length(); i += 3) {
            if (i + 3 <= dnaSequence.length()) {
                String codon = dnaSequence.substring(i, i + 3);
                codonQueue.offer(codon);
            }
        }

        StringBuilder aminoAcidSequence = new StringBuilder();
        while (!codonQueue.isEmpty()) {
            String codon = codonQueue.poll();
            String aminoAcid = codonToAminoAcid.getOrDefault(codon, "Unknown");
            aminoAcidSequence.append(aminoAcid).append(" ");
        }

        return aminoAcidSequence.toString().trim();
    }
}
