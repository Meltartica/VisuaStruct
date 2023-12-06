package com.visustruct;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationsMenu {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void backHandler(ActionEvent actionEvent) {
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

    public void stockHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("stockanalysis.fxml"));
            Parent root = loader.load();

            StockAnalysis stockAnalysis = loader.getController();
            stockAnalysis.setStage(stage);
            stage.setTitle("VisuaStruct - Stock Analysis");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inventoryHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inventory.fxml"));
            Parent root = loader.load();

            Inventory inventorySystem = loader.getController();
            inventorySystem.setStage(stage);
            stage.setTitle("VisuaStruct - Inventory Management System");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void taskHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("taskmanager.fxml"));
            Parent root = loader.load();

            TaskManager taskHandler = loader.getController();
            taskHandler.setStage(stage);
            stage.setTitle("VisuaStruct - Task Manager");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dnaHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dnadecoder.fxml"));
            Parent root = loader.load();

            DNASequenceDecoder dnaHandler = loader.getController();
            dnaHandler.setStage(stage);
            stage.setTitle("VisuaStruct - DNA Sequence Decoder");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
