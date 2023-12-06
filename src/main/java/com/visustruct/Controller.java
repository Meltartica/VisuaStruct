package com.visustruct;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Controller {
    DisplayFacts displayFacts = new DisplayFacts();
    private Stage stage;

    private final List<String> factsList = displayFacts.getFactsList();

    @FXML
    private TextArea didyouknow;

    /**
     * Initializes the Controller by loading data from files and displaying initial game statistics.
     */
    @FXML
    protected void initialize() {
        displayFacts.loadFactsFromFile();
        displayRandomFact();
    }

    /**
     * Displays a random fact from the factsList in the didyouknow label.
     * If the factsList is empty, "No facts found." will be displayed.
     */
    private void displayRandomFact() {
        if (!factsList.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(factsList.size());
            String randomFact = factsList.get(randomIndex);
            didyouknow.setText(randomFact);
        } else {
            didyouknow.setText("No facts found.");
        }
    }

    // Set the stage instance during initialization
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void aboutProgramHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("about-program.fxml"));
            Parent root = loader.load();

            AboutProgram aboutProgramController = loader.getController();
            aboutProgramController.setStage(stage);
            stage.setTitle("VisuaStruct - About Program");

            Scene aboutScene = new Scene(root);

            stage.setScene(aboutScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void dataStructuresHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("datastructures.fxml"));
            Parent root = loader.load();

            DataStructuresMenu DSProgramController = loader.getController();
            DSProgramController.setStage(stage);
            stage.setTitle("VisuaStruct - Data Structures");

            Scene DSScene = new Scene(root);

            stage.setScene(DSScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void applicationsMenuHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("applications.fxml"));
            Parent root = loader.load();

            ApplicationsMenu applicationsMenu = loader.getController();
            applicationsMenu.setStage(stage);
            stage.setTitle("VisuaStruct - Data Structures");

            Scene AScene = new Scene(root);

            stage.setScene(AScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void encryptHandler(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("encryption.fxml"));
            Parent root = loader.load();

            Encryptonado encryptHandler = loader.getController();
            encryptHandler.setStage(stage);
            stage.setTitle("VisuaStruct - Encryptonado");

            Scene AScene = new Scene(root);

            stage.setScene(AScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}