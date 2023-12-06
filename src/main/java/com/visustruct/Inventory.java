package com.visustruct;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;

public class Inventory {
    private Stage stage;

    @FXML
    private TableView<Product> inventoryTable;

    private ObservableList<Product> inventory;

    public void setStage(Stage stage) {
        this.stage = stage;

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void backHandler() {
        try {
            exportToCSV("src/main/java/assets/inventory.csv");
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
        LinkedList<Product> linkedList = new LinkedList<>();
        inventory = FXCollections.observableList(linkedList);
        inventoryTable.setItems(inventory);
        importFromCSV("src/main/java/assets/inventory.csv");

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setMinWidth(140);

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        priceColumn.setMinWidth(160);

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantityColumn.setMinWidth(140);

        TableColumn<Product, String> productIDColumn = new TableColumn<>("Product ID");
        productIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        productIDColumn.setMinWidth(140);

        inventoryTable.getColumns().setAll(productIDColumn, nameColumn, priceColumn, quantityColumn);
    }

    @FXML
    private void addProduct() {
        showAddProductDialog();
    }

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        TextField productIDField = new TextField();
        productIDField.setPromptText("Product ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        GridPane grid = new GridPane();
        grid.add(new Label("Product ID:"), 0, 0);
        grid.add(productIDField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String productID = productIDField.getText().trim();
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());

                Product newProduct = new Product(name, price, quantity, productID);
                inventory.add(newProduct);
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void removeProduct() {
        Product selectedProduct = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            inventory.remove(selectedProduct);
        } else {
            showError("Please select a product to remove.");
        }
    }

    @FXML
    private void searchProduct() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Product");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter product name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            for (Product product : inventory) {
                if (product.nameProperty().get().equalsIgnoreCase(name)) {
                    inventoryTable.getSelectionModel().select(product);
                    break;
                }
            }
        });
    }

    @FXML
    private void updateProduct() {
        Product selectedProduct = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Dialog<Product> dialog = new Dialog<>();
            dialog.setTitle("Update Product");

            ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

            TextField nameField = new TextField(selectedProduct.nameProperty().get());
            TextField priceField = new TextField(String.valueOf(selectedProduct.priceProperty().get()));
            TextField quantityField = new TextField(String.valueOf(selectedProduct.quantityProperty().get()));

            GridPane grid = new GridPane();
            grid.add(new Label("Name:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Price:"), 0, 1);
            grid.add(priceField, 1, 1);
            grid.add(new Label("Quantity:"), 0, 2);
            grid.add(quantityField, 1, 2);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButton) {
                    String name = nameField.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    int quantity = Integer.parseInt(quantityField.getText().trim());

                    selectedProduct.nameProperty().set(name);
                    selectedProduct.priceProperty().set(price);
                    selectedProduct.quantityProperty().set(quantity);
                    inventoryTable.refresh();
                }
                return null;
            });

            dialog.showAndWait();
        } else {
            showError("Please select a product to update.");
        }
    }

    @FXML
    private void inventoryInfo() {
        int totalStockNumber = 0;
        double totalInventoryValue = 0.0;

        for (Product product : inventory) {
            totalStockNumber += product.quantityProperty().get();
            totalInventoryValue += product.priceProperty().get() * product.quantityProperty().get();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inventory Information");
        alert.setHeaderText(null);
        alert.setContentText("Total Stock Number: " + totalStockNumber + "\nTotal Inventory Value: $" + totalInventoryValue);

        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void importFromCSV(String filePath) {
        try (Scanner scanner = new Scanner(Files.newBufferedReader(Paths.get(filePath)))) {
            while (scanner.hasNextLine()) {
                String[] productData = scanner.nextLine().split(",");
                String name = productData[0];
                double price = Double.parseDouble(productData[1]);
                int quantity = Integer.parseInt(productData[2]);
                String productID = productData[3];

                Product newProduct = new Product(name, price, quantity, productID);
                inventory.add(newProduct);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        exportToCSV("src/main/java/assets/inventory.csv");
    }

    private void exportToCSV(String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (Product product : inventory) {
                writer.println(product.nameProperty().get() + "," + product.priceProperty().get() + "," + product.quantityProperty().get() + "," + product.productIDProperty().get());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Product {
    private StringProperty name;
    private DoubleProperty price;
    private IntegerProperty quantity;
    private StringProperty productID;

    public Product(String name, double price, int quantity, String productID) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.productID = new SimpleStringProperty(productID);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public StringProperty productIDProperty() {
        return productID;
    }

    @Override
    public String toString() {
        return String.format("%s - Price: $%.2f, Quantity: %d, Product ID: %s", name.get(), price.get(), quantity.get(), productID.get());
    }
}