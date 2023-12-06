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
import java.util.Stack;

public class StackDS {
    private Stage stage;
    private final Stack<Integer> stack = new Stack<>();

    @FXML
    private TextField pushTextField;

    @FXML
    private TextField popTextField;

    @FXML
    private Label stackLabel0;

    @FXML
    private Label stackLabel1;

    @FXML
    private Label stackLabel2;

    @FXML
    private Label stackLabel3;

    @FXML
    private Label stackLabel4;

    @FXML
    private Label topLabel0;

    @FXML
    private Label topLabel1;

    @FXML
    private Label topLabel2;

    @FXML
    private Label topLabel3;

    @FXML
    private Label topLabel4;

    private Label[] stackLabels;
    private Label[] topLabels;

    public void initialize() {
        stackLabels = new Label[]{stackLabel0, stackLabel1, stackLabel2, stackLabel3, stackLabel4};
        topLabels = new Label[]{topLabel0, topLabel1, topLabel2, topLabel3, topLabel4};

        for (Label label : stackLabels) {
            label.setText(null);
        }

        for (Label label : topLabels) {
            label.setText(null);
        }

        popTextField.setText("No Input for Stack");
        popTextField.setStyle("-fx-border-color: black; -fx-border-radius: 7; -fx-background-color: white; -fx-text-fill: red;");
        popTextField.setEditable(false);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void pushHandler() {
        if (stack.size() >= stackLabels.length) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Stack Full");
            alert.setHeaderText(null);
            alert.setContentText("The stack is full.");
            alert.showAndWait();
        } else if (pushTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a number to push to the stack.");
            alert.showAndWait();
            pushTextField.setText(null);
        } else {
            try {
                int number = Integer.parseInt(pushTextField.getText());
                stack.push(number);
                stackLabels[stack.size() - 1].setText(Integer.toString(number));
                topLabels[stack.size() - 1].setText("Top");
                pushTextField.setText(null);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number.");
                alert.showAndWait();
                pushTextField.setText(null);
            }
        }
    }

    public void popHandler() {
        if (stack.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Stack Empty");
            alert.setHeaderText(null);
            alert.setContentText("The stack is empty.");
            alert.showAndWait();
        } else {
            stack.pop();
            stackLabels[stack.size()].setText(null);
            topLabels[stack.size()].setText(null);
            if (!stack.isEmpty()) {
                topLabels[stack.size() - 1].setText("Top of Stack");
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
        stack.clear();
        initialize();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stack Reset");
        alert.setHeaderText(null);
        alert.setContentText("The stack has been reset.");
        alert.showAndWait();
    }
}