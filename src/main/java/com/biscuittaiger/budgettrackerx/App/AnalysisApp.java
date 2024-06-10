package com.biscuittaiger.budgettrackerx.App;

import javafx.scene.chart.*;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalysisApp extends VBox {
    private PieChart pieChart;
    private BarChart<String, Number> barChart;

    private List<double[]> userData;  // List to store user data arrays

    public AnalysisApp(String userId) {
        pieChart = new PieChart();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("Category");
        yAxis.setLabel("Amount (RM)");

        getChildren().addAll(pieChart, barChart);
        loadData(userId);
        updateChart(1);
    }

    private void loadData(String userId) {
        userData = new ArrayList<>();
        String filename = "src/main/java/com/biscuittaiger/budgettrackerx/App/budget_info.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 7 && userId.equals(parts[0])) {
                    int month = Integer.parseInt(parts[1]);
                    double balance = Double.parseDouble(parts[2]);
                    double income = Double.parseDouble(parts[3]);
                    double expense = Double.parseDouble(parts[4]);
                    double budget = Double.parseDouble(parts[5]);
                    double savings = Double.parseDouble(parts[6]);
                    userData.add(new double[] {month, balance, income, expense, budget, savings});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateChart(int month) {
        pieChart.getData().clear();
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        double total = 0;
        for (double[] data : userData) {
            if (data[0] == month) {
                total += data[1] + data[2] + data[3] + data[4] + data[5];
            }
        }

        for (double[] data : userData) {
            if (data[0] == month) {

                PieChart.Data balanceSlice = new PieChart.Data("Balance", data[1]);
                PieChart.Data incomeSlice = new PieChart.Data("Income", data[2]);
                PieChart.Data expenseSlice = new PieChart.Data("Expense", data[3]);
                PieChart.Data budgetSlice = new PieChart.Data("Budget", data[4]);
                PieChart.Data savingsSlice = new PieChart.Data("Savings", data[5]);

                pieChart.getData().add(balanceSlice);
                pieChart.getData().add(incomeSlice);
                pieChart.getData().add(expenseSlice);
                pieChart.getData().add(budgetSlice);
                pieChart.getData().add(savingsSlice);

                balanceSlice.setName(String.format("Balance %.1f%%", (data[1] / total) * 100));
                incomeSlice.setName(String.format("Income %.1f%%", (data[2] / total) * 100));
                expenseSlice.setName(String.format("Expense %.1f%%", (data[3] / total) * 100));
                budgetSlice.setName(String.format("Budget %.1f%%", (data[4] / total) * 100));
                savingsSlice.setName(String.format("Savings %.1f%%", (data[5] / total) * 100));

                series.getData().add(new XYChart.Data<>("Balance", data[1]));
                series.getData().add(new XYChart.Data<>("Income", data[2]));
                series.getData().add(new XYChart.Data<>("Expense", data[3]));
                series.getData().add(new XYChart.Data<>("Budget", data[4]));
                series.getData().add(new XYChart.Data<>("Savings", data[5]));
            }
        }
        barChart.getData().add(series);
    }
}
