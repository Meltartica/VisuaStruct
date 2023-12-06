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
import java.util.LinkedList;

public class Array {
    private Stage stage;
    private final LinkedList<Integer> array = new LinkedList<>();

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

    private Label[] arrayLabels;

    public void initialize() {
        arrayLabels = new Label[]{linearLabel0, linearLabel1, linearLabel2, linearLabel3, linearLabel4};

        for (Label label : arrayLabels) {
            label.setText(null);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addHandler() {
        if (array.size() >= arrayLabels.length) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Array Full");
            alert.setHeaderText(null);
            alert.setContentText("The array is full.");
            alert.showAndWait();
        } else if (addTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a number to add to the array.");
            alert.showAndWait();
            addTextField.setText(null);
        } else {
            try {
                int number = Integer.parseInt(addTextField.getText());
                array.add(number);
                arrayLabels[array.size() - 1].setText(Integer.toString(number));
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
        if (removeTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a number to remove from the array.");
            alert.showAndWait();
        } else {
            try {
                int number = Integer.parseInt(removeTextField.getText());
                if (array.contains(number)) {
                    array.removeFirstOccurrence(number);
                    for (int i = 0; i < arrayLabels.length; i++) {
                        if (i < array.size()) {
                            arrayLabels[i].setText(Integer.toString(array.get(i)));
                        } else {
                            arrayLabels[i].setText(null);
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Number Not Found");
                    alert.setHeaderText(null);
                    alert.setContentText("The number " + number + " is not in the array.");
                    alert.showAndWait();
                }
                removeTextField.setText(null);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number.");
                alert.showAndWait();
                removeTextField.setText(null);
            }
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
        array.clear();
        initialize();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Array Reset");
        alert.setHeaderText(null);
        alert.setContentText("The array has been reset.");
        alert.showAndWait();
    }
}