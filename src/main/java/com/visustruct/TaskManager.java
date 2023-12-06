package com.visustruct;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TaskManager {
    private Stage stage;
    private final Map<Integer, Stack<Task>> taskStacks;

    @FXML
    private TextArea outputTextArea;

    public void setStage(Stage stage) {
        this.stage = stage;

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void backHandler() {
        try {
            exportTasksToFile("src/main/java/assets/tasks.txt");
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

    public void initialize() {
        outputTextArea.setText("No Tasks added.");
        outputTextArea.setEditable(false);
        importTasksFromFile("src/main/java/assets/tasks.txt");
        updateOutput();
    }

    public TaskManager() {
        taskStacks = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            taskStacks.put(i, new Stack<>());
        }
    }

    public void addTaskToStack(Task task) {
        Stack<Task> stack = taskStacks.get(task.getPriority());
        if (stack != null) {
            stack.push(task);
        }
    }

    public Task removeTaskFromStack(int priority) {
        Stack<Task> stack = taskStacks.get(priority);
        if (stack != null && !stack.isEmpty()) {
            return stack.pop();
        }
        return null;
    }

    public Stack<Task> getTaskStack(int priority) {
        return taskStacks.get(priority);
    }

    @FXML
    private void addFromStack() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add Task");

        ButtonType loginButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        ChoiceBox<String> statusField = new ChoiceBox<>();
        statusField.getItems().addAll("Ongoing", "Done", "Not Started");
        DatePicker dueDateField = new DatePicker();
        ChoiceBox<Integer> priorityField = new ChoiceBox<>();
        priorityField.getItems().addAll(1, 2, 3, 4, 5);
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        TextField notesField = new TextField();
        notesField.setPromptText("Notes");

        GridPane grid = new GridPane();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Status:"), 0, 1);
        grid.add(statusField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDateField, 1, 2);
        grid.add(new Label("Priority Level:"), 0, 3);
        grid.add(priorityField, 1, 3);
        grid.add(new Label("Category:"), 0, 4);
        grid.add(categoryField, 1, 4);
        grid.add(new Label("Notes:"), 0, 5);
        grid.add(notesField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Task(nameField.getText(), statusField.getValue(), dueDateField.getValue(), priorityField.getValue(), categoryField.getText(), notesField.getText());
            }
            return null;
        });

        Optional<Task> result = dialog.showAndWait();

        result.ifPresent(task -> {
            addTaskToStack(task);
            updateOutput();
        });
    }

    @FXML
    private void removeFromStack() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Remove Task");

        ButtonType removeButtonType = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(removeButtonType, ButtonType.CANCEL);

        ChoiceBox<Integer> priorityField = new ChoiceBox<>();
        priorityField.getItems().addAll(1, 2, 3, 4, 5);

        GridPane grid = new GridPane();
        grid.add(new Label("Priority Level:"), 0, 0);
        grid.add(priorityField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == removeButtonType) {
                return priorityField.getValue();
            }
            return null;
        });

        Optional<Integer> result = dialog.showAndWait();

        result.ifPresent(priority -> {
            Stack<Task> stack = getTaskStack(priority);
            if (stack != null && !stack.isEmpty()) {
                Task removedTask = removeTaskFromStack(priority);
                updateOutput();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Priority " + priority + " stack is empty.");

                alert.showAndWait();
            }
        });
    }

    private void updateOutput() {
        outputTextArea.clear();
        outputTextArea.appendText("Tasks To Do:\n");
        for (int i = 1; i <= 5; i++) {
            Stack<Task> tasks = getTaskStack(i);
            for (Task task : tasks) {
                outputTextArea.appendText("- " + task.getName() + "\n");
                outputTextArea.appendText("  Status: " + task.getStatus() + "\n");
                outputTextArea.appendText("  Due Date: " + task.getDueDate() + "\n");
                outputTextArea.appendText("  Priority Level: " + task.getPriority() + "\n");
                outputTextArea.appendText("  Category: " + task.getCategory() + "\n");
                outputTextArea.appendText("  Notes: " + task.getNotes() + "\n");
                outputTextArea.appendText("\n");
            }
        }
    }

    public void stop() {
        exportTasksToFile("src/main/java/assets/tasks.txt");
    }

    public void importTasksFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                Task task = new Task(parts[0], parts[1], LocalDate.parse(parts[2]), Integer.parseInt(parts[3]), parts[4], parts[5]);
                addTaskToStack(task);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + filePath);
            e.printStackTrace();
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date from file: " + filePath);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from file: " + filePath);
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error reading data from file: " + filePath);
            e.printStackTrace();
        }
    }

    public void exportTasksToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (int i = 1; i <= 5; i++) {
                Stack<Task> tasks = getTaskStack(i);
                for (Task task : tasks) {
                    writer.println(task.getName() + "," + task.getStatus() + "," + task.getDueDate() + "," + task.getPriority() + "," + task.getCategory() + "," + task.getNotes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Task {
    private String name;
    private String status;
    private LocalDate dueDate;
    private int priority;
    private String category;
    private String notes;

    public Task(String name, String status, LocalDate dueDate, int priority, String category, String notes) {
        this.name = name;
        this.status = status;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
        this.notes = notes;
    }

    // getters and setters for all fields

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}