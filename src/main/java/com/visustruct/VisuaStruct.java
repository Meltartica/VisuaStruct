package com.visustruct;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class VisuaStruct extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VisuaStruct.class.getResource("main-menu.fxml"));
        Parent root = fxmlLoader.load();

        // Set the controller and stage instance
        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("VisuaStruct - Main Menu");
        stage.setScene(scene);

        // Disable resizing
        stage.setResizable(false);

        // Set the icon
        Image image = new Image("file:src/main/resources/assets/icon.png");
        stage.getIcons().add(image);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
