package com.visustruct;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutProgram {
    private Stage stage;

    // Set the stage instance during initialization
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void backHandler(ActionEvent actionEvent) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-menu.fxml"));
            Parent root = loader.load();

            // Set the controller and stage instance
            Controller controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("VisuaStruct - Main Menu");

            // Update the root node of the current scene with the new content
            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

            // Optionally, release resources associated with the old root node
            // For example, if the old root is an instance of a custom controller, you may want to call a cleanup method on it.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}