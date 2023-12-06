package com.visustruct;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.*;

import java.io.IOException;

public class Encryptonado {
    private Stage stage;

    @FXML
    private TextArea inputTextArea;
    @FXML
    private TextArea outputTextArea;
    @FXML
    private Label logOutput;
    @FXML
    private TextArea logOutputTest;

    public void initialize() {
        outputTextArea.setEditable(false);
        outputTextArea.setText("Output will be displayed here.\n");
        logOutputTest.setText("Log will be displayed here.\n");
    }

    public void setStage(Stage stage) {
        this.stage = stage;

    }

    public void backHandler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-menu.fxml"));
            Parent root = loader.load();

            Controller controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("VisuaStruct - Main Menu");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void encryptHandler(ActionEvent actionEvent) {
        logOutputTest.clear();
        StringBuilder logBuilder = new StringBuilder();
        String inputText = inputTextArea.getText();
        StringBuilder encryptedText = new StringBuilder();
        StringBuilder encryptedTextCypher = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        char encryptedChar;

        for (char c : inputText.toCharArray()) {
            stack.push(c);
        }

        logBuilder.append("Stack and Queue operations completed.\n");


        while (!stack.isEmpty()) {
                char c = stack.poll();
                if (Character.isUpperCase(c)) {
                    encryptedChar = (char) ((c - 'A' + 10*356) % 26 + 'A');
                } else if (Character.isLowerCase(c)) {
                    encryptedChar = (char) ((c - 'a' + 7*356) % 26 + 'a');
                } else {
                    encryptedChar = (char) ((c + 5*356) % 128);
                }
                encryptedText.append(encryptedChar);
                logBuilder.append("Converted ").append(c).append(" to ").append(encryptedChar).append("\n");
        }

        logBuilder.append("Modified Cipher operation completed.\n");

        int i = inputText.length();
        while (i > 0) {
            char c = encryptedText.charAt(i-1);
            if (Character.isUpperCase(c)) {
                encryptedChar = (char) ((c - 'A' + 10*356) % 26 + 'A');
            } else if (Character.isLowerCase(c)) {
                encryptedChar = (char) ((c - 'a' + 7*356) % 26 + 'a');
            } else {
                encryptedChar = (char) ((c + 5*356) % 128);
            }
            encryptedTextCypher.append(encryptedChar);
            logBuilder.append("Converted ").append(c).append(" to ").append(encryptedChar).append("\n");
            i--;
        }

        logBuilder.append("Modified Cipher operation completed.\n");

        outputTextArea.setText(encryptedTextCypher.toString());

        logBuilder.append("Encryption completed.\n");
        logOutput.setText(logBuilder.toString());
        logOutputTest.setText(logOutput.getText());
    }

    @FXML
    public void decryptHandler(ActionEvent actionEvent) {
        logOutputTest.clear();
        StringBuilder logBuilder = new StringBuilder();
        String inputText = inputTextArea.getText();
        StringBuilder decryptedText = new StringBuilder();
        StringBuilder decryptedTextCypher = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        char decryptedChar;

        for (char c : inputText.toCharArray()) {
            stack.push(c);
        }

        logBuilder.append("Stack and Queue operations completed.\n");

        while (!stack.isEmpty()) {
            char c = stack.poll();
            if (Character.isUpperCase(c)) {
                decryptedChar = (char) ((c - 'A' + 16 * 356) % 26 + 'A');
            } else if (Character.isLowerCase(c)) {
                decryptedChar = (char) ((c - 'a' + 19 * 356) % 26 + 'a');
            } else {
                decryptedChar = (char) ((c - 5 * 356) % 128);
            }
            decryptedText.append(decryptedChar);
            logBuilder.append("Converted ").append(c).append(" to ").append(decryptedChar).append("\n");
        }

        logBuilder.append("Modified Cipher operation completed.\n");

        int i = inputText.length();
        while (i > 0) {
            char c = decryptedText.charAt(i - 1);
            if (Character.isUpperCase(c)) {
                decryptedChar = (char) ((c - 'A' + 16 * 356) % 26 + 'A');
            } else if (Character.isLowerCase(c)) {
                decryptedChar = (char) ((c - 'a' + 19 * 356) % 26 + 'a');
            } else {
                decryptedChar = (char) ((c - 5 * 356) % 128);
            }
            decryptedTextCypher.append(decryptedChar);
            logBuilder.append("Converted ").append(c).append(" to ").append(decryptedChar).append("\n");
            i--;
        }

        logBuilder.append("Modified Decipher operation completed.\n");

        outputTextArea.setText(decryptedTextCypher.toString());

        logBuilder.append("Decryption completed.\n");
        logOutput.setText(logBuilder.toString());
        logOutputTest.setText(logOutput.getText());
    }
}