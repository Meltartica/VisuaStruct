package com.visustruct;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class StockAnalysis {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label symbolLabel;
    private Stage stage;
    private ObservableList<XYChart.Data<Number, Number>> stockData = FXCollections.observableArrayList();
    private LineChart<Number, Number> lineChart;
    private final String apiKey = "49dc48f478ac76d4d86a91e13937531a";

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void backHandler() {
        try {
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

        // Create X and Y axis
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Stock Price");

        lineChart = new LineChart<>(xAxis, yAxis);

        // Create a series for stock data
        XYChart.Series<Number, Number> series = new XYChart.Series<>("Stock Price", stockData);
        lineChart.getData().add(series);

        // Add the lineChart to the anchorPane
        anchorPane.getChildren().add(lineChart);

        // Adjust the layout as needed
        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
    }

    @FXML
    private void fetchData() {
        // Clear the predicted data series from the line chart
        lineChart.getData().removeIf(series -> series.getName().equals("Predicted Stock Price"));

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Fetch Stock Data");
        dialog.setHeaderText("Enter a stock symbol:");
        dialog.setContentText("Symbol:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(symbol -> {
            if (symbol.isEmpty()) {
                System.err.println("Please enter a stock symbol.");
                return;
            }

            symbolLabel.setText("Stock Symbol: " + symbol);

            try {
                URL url = new URL("https://financialmodelingprep.com/api/v3/historical-price-full/" + symbol + "?apikey=" + apiKey);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() == 200) {
                    Scanner scanner = new Scanner(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();

                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }

                    String jsonResponse = response.toString();
                    if (jsonResponse.contains("Error Message")) {
                        System.err.println("Invalid stock symbol: " + symbol);
                        return;
                    }

                    parseStockData(jsonResponse);
                } else {
                    System.err.println("Failed to fetch data. HTTP Error Code: " + connection.getResponseCode());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void parseStockData(String response) {
        JSONObject jsonObject = new JSONObject(response);

        // Check if "historical" exists in the JSON response
        if (!jsonObject.has("historical")) {
            System.err.println("historical not found in the response");
            return;
        }

        // Create a TextInputDialog for user input
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Number of Days");
        dialog.setHeaderText("Enter the number of days from the current time:");
        dialog.setContentText("Days:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(days -> {
            if (days.isEmpty() || !days.matches("\\d+")) {
                System.err.println("Please enter a valid number of days.");
                return;
            }

            int numDays = Integer.parseInt(days);

            JSONArray historicalData = jsonObject.getJSONArray("historical");

            // Clear the previous stock data
            stockData.clear();

            // Iterate over each date in the historical data in reverse order
            for (int i = numDays; i >= 0; i--) {
                JSONObject dayData = historicalData.getJSONObject(i);

                // Get the closing price for this date
                double closePrice = dayData.getDouble("close");

                // Add the date and closing price to the end of the stock data
                stockData.add(new XYChart.Data<>(numDays - i + 1, closePrice));
            }
        });
    }

    @FXML
    private void predictHandler() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Predict Stock Data");
        dialog.setHeaderText("Enter the number of days to predict:");
        dialog.setContentText("Days:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(days -> {
            if (days.isEmpty() || !days.matches("\\d+")) {
                System.err.println("Please enter a valid number of days.");
                return;
            }

            int numDays = Integer.parseInt(days);

            // Create arrays for the day numbers and closing prices
            int size = stockData.size();
            double[] dayNumbers = new double[size];
            double[] closingPrices = new double[size];

            for (int i = 0; i < size; i++) {
                XYChart.Data<Number, Number> data = stockData.get(i);
                dayNumbers[i] = data.getXValue().doubleValue();
                closingPrices[i] = data.getYValue().doubleValue();
            }

            // Calculate the means
            double meanDay = Arrays.stream(dayNumbers).average().orElse(0);
            double meanPrice = Arrays.stream(closingPrices).average().orElse(0);

            // Calculate the slope and intercept of the regression line
            double slope = 0;
            double intercept = 0;
            double sumNumerator = 0;
            double sumDenominator = 0;

            for (int i = 0; i < size; i++) {
                sumNumerator += (dayNumbers[i] - meanDay) * (closingPrices[i] - meanPrice);
                sumDenominator += Math.pow(dayNumbers[i] - meanDay, 2);
            }

            if (sumDenominator != 0) {
                slope = sumNumerator / sumDenominator;
                intercept = meanPrice - slope * meanDay;
            }

            // Create a new series for the predicted data
            ObservableList<XYChart.Data<Number, Number>> predictedData = FXCollections.observableArrayList();
            XYChart.Series<Number, Number> predictedSeries = new XYChart.Series<>("Predicted Stock Price", predictedData);

            // Predict the stock prices for the next n days
            Random random = new Random();
            for (int i = 1; i <= numDays; i++) {
                double predictedPrice = slope * (size + i) + intercept;

                // Add a random factor to the predicted price
                double randomFactor = (random.nextDouble() - 0.5) * 2; // random number between -1 and 1
                predictedPrice += randomFactor;

                predictedData.add(new XYChart.Data<>(size + i, predictedPrice));
            }

            // Add the predicted series to the line chart
            lineChart.getData().add(predictedSeries);
        });
    }
}