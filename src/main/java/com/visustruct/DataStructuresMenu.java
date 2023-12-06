package com.visustruct;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DataStructuresMenu {
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

    public void queueHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("queue.fxml"));
            Parent root = loader.load();

            Queue queueVisualization = loader.getController();
            queueVisualization.setStage(stage);
            stage.setTitle("VisuaStruct - Queue");

            Scene queueScene = new Scene(root);
            stage.setScene(queueScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void arrayHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("array.fxml"));
            Parent root = loader.load();

            Array arrayVisualization = loader.getController();
            arrayVisualization.setStage(stage);
            stage.setTitle("VisuaStruct - Array");

            Scene arrayScene = new Scene(root);
            stage.setScene(arrayScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void treeHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tree.fxml"));
            Parent root = loader.load();

            Tree treeVisualization = loader.getController();
            treeVisualization.setStage(stage);
            stage.setTitle("VisuaStruct - Tree");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stackHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("stack.fxml"));
            Parent root = loader.load();

            StackDS stackVisualization = loader.getController();
            stackVisualization.setStage(stage);
            stage.setTitle("VisuaStruct - Stack");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
