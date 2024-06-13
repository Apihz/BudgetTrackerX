package com.biscuittaiger.budgettrackerx.View;

import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalysisView extends VBox {
    private static final List<String> CATEGORIES = Arrays.asList(
            "Shopping", "Education", "Electronics", "Entertainment", "Food and Beverages",
            "Health and Beauty", "Medical", "Transportation", "Other Expenses"
    );

    private PieChart pieChart;
    private BarChart<String, Number> barChart;
    private List<String[]> expenseDataList;
    private Label totalExpenseLabel;

    public AnalysisView(String userId) {
        pieChart = new PieChart();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("Category");
        yAxis.setLabel("Amount (RM)");
        totalExpenseLabel = new Label();

        // Rotate the labels for bar graph to avoid overlap
        xAxis.setTickLabelRotation(45);
        xAxis.setTickLabelGap(10);

        getChildren().addAll(totalExpenseLabel, pieChart, barChart);
        loadData();
        updateChart(userId,1); // Default month
    }

    private void loadData( ) {
        expenseDataList = new ArrayList<>();
        String fileName = "src/main/java/com/biscuittaiger/budgettrackerx/Model/TransactionData.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header line
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 8) {  // Adjusted to the new format with more fields
                    expenseDataList.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateChart(String userId,int month) {
        pieChart.getData().clear();
        barChart.getData().clear();

        List<PieChart.Data> pieChartData = new ArrayList<>();
        XYChart.Series<String, Number> barChartSeries = new XYChart.Series<>();
        double totalExpense = 0;

        // Calculate total expenses for the month
        for (String[] data : expenseDataList) {
            int dataMonth = Integer.parseInt(data[1]);
            double amount = Double.parseDouble(data[2]);
            String type = data[3];

            if (userId.equals(data[0]) && dataMonth == month && type.equals("expense")) {
                totalExpense += amount;
            }
        }
        totalExpenseLabel.setText("Total Expense: RM" + totalExpense);
        totalExpenseLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        totalExpenseLabel.setPadding(new Insets(0, 10, 10, 20));

        // Create pie chart data
        for (String category : CATEGORIES) {
            double categoryTotal = 0;
            for (String[] data : expenseDataList) {
                int dataMonth = Integer.parseInt(data[1]);
                double amount = Double.parseDouble(data[2]);
                String type = data[3];
                String dataCategory = data[4];

                if (userId.equals(data[0]) && dataMonth == month && type.equals("expense") && dataCategory.equals(category)) {
                    categoryTotal += amount;
                }
            }
            if (categoryTotal > 0) {
                pieChartData.add(new PieChart.Data(category, categoryTotal));
            }
        }

        // Add all categories to the bar chart
        for (String category : CATEGORIES) {
            double categoryAmount = 0;
            for (String[] data : expenseDataList) {
                int dataMonth = Integer.parseInt(data[1]);
                double amount = Double.parseDouble(data[2]);
                String type = data[3];
                String dataCategory = data[4];

                if (userId.equals(data[0]) && dataMonth == month && type.equals("expense") && dataCategory.equals(category)) {
                    categoryAmount += amount;
                }
            }
            barChartSeries.getData().add(new XYChart.Data<>(category, categoryAmount));
        }

        pieChart.getData().addAll(pieChartData);
        barChart.getData().add(barChartSeries);

        for (PieChart.Data slice : pieChartData) {
            double percentage = (slice.getPieValue() / totalExpense) * 100;
            slice.setName(slice.getName() + " " + String.format("%.2f%%", percentage) + " (RM" + slice.getPieValue() + ")");
        }
    }
}
