package com.visustruct;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Queue {
    private Stage stage;
    private final java.util.Queue<Integer> queue = new LinkedList<>();
    private int headIndex = 0;
    private int tailIndex = 0;

    @FXML
    private TextField addTextField;

    @FXML
    private TextField removeTextField;

    @FXML
    private Label linearLabel0;

    @FXML
    private Label linearLabel1;

    @FXML
    private Label linearLabel2;

    @FXML
    private Label linearLabel3;

    @FXML
    private Label linearLabel4;

    @FXML
    private Label headLabel0;

    @FXML
    private Label headLabel1;

    @FXML
    private Label headLabel2;

    @FXML
    private Label headLabel3;

    @FXML
    private Label headLabel4;

    @FXML
    private Label tailLabel0;

    @FXML
    private Label tailLabel1;

    @FXML
    private Label tailLabel2;

    @FXML
    private Label tailLabel3;

    @FXML
    private Label tailLabel4;

    private Label[] queueLabels, tailLabels, headLabels;

    public void initialize() {
        queueLabels = new Label[]{linearLabel0, linearLabel1, linearLabel2, linearLabel3, linearLabel4};
        headLabels = new Label[]{headLabel0, headLabel1, headLabel2, headLabel3, headLabel4};
        tailLabels = new Label[]{tailLabel0, tailLabel1, tailLabel2, tailLabel3, tailLabel4};

        List<Label[]> allLabels = Arrays.asList(queueLabels, headLabels, tailLabels);
        for (Label[] labels : allLabels) {
            for (Label label : labels) {
                label.setText(null);
            }
        }

        headLabels[0].setText("Head");
        tailLabels[0].setText("Tail");

        removeTextField.setText("No Input for Queue");
        removeTextField.setStyle("-fx-border-color: black; -fx-border-radius: 7; -fx-background-color: white; -fx-text-fill: red;");
        removeTextField.setEditable(false);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addHandler() {
        if (tailIndex >= queueLabels.length) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Queue Full");
            alert.setHeaderText(null);
            alert.setContentText("The queue is full.");
            alert.showAndWait();
        } else if (addTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a number to add to the queue.");
            alert.showAndWait();
            addTextField.setText(null);
        } else {
            try {
                int number = Integer.parseInt(addTextField.getText());
                queue.add(number);
                queue.remove();
                for (Label label : tailLabels) {
                    label.setText(null);
                }
                tailLabels[tailIndex].setText("Tail");
                queueLabels[tailIndex].setText(Integer.toString(number));
                tailIndex++;
                addTextField.setText(null);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number.");
                alert.showAndWait();
                addTextField.setText(null);
            }
        }
    }

    public void removeHandler() {
        if (tailIndex == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Queue Empty");
            alert.setHeaderText(null);
            alert.setContentText("The queue is empty.");
            alert.showAndWait();
        } else if (headIndex == 4) {
            headLabels[headIndex].setText("Head");
            queueLabels[headIndex].setText(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Queue Reset");
            alert.setHeaderText(null);
            alert.setContentText("Please Reset to Continue Playing with the Queue");
            alert.showAndWait();
        } else {
            for (Label label : headLabels) {
                label.setText(null);
            }
            queueLabels[headIndex].setText(null);
            headIndex++;
            headLabels[headIndex].setText("Head");
        }
    }

    public void backHandler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("datastructures.fxml"));
            Parent root = loader.load();

            DataStructuresMenu dsmenu = loader.getController();
            dsmenu.setStage(stage);
            stage.setTitle("VisuaStruct - Data Structures Menu");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetHandler() {
        queue.clear();
        headIndex = 0;
        tailIndex = 0;
        initialize();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Queue Reset");
        alert.setHeaderText(null);
        alert.setContentText("The queue has been reset.");
        alert.showAndWait();
    }
}