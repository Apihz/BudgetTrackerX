package com.biscuittaiger.budgettrackerx.View;

import com.biscuittaiger.budgettrackerx.App.BudgetApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BudgetView {
    private ComboBox<String> monthSelection;
    private ComboBox<String> categorySelection;
    private TextField amountField;
    private Label feedbackLabel;
    private Label budgetLabel;
    private Label expenseLabel;
    private Label withinBudgetLabel;
    private Label totalBudgetLabel;
    private String userId;
    private static final String BudgetFile = "src/main/java/com/biscuittaiger/budgettrackerx/Model/BudgetData.txt";
    private static ArrayList<BudgetApp> budgetList = new ArrayList<>();


    public VBox BudgetView (String userId) {

        String css = this.getClass().getResource("/com/biscuittaiger/budgettrackerx/budgetview.css").toExternalForm();

        this.userId = userId;
        // Load budgets from file
        loadBudgetsFromFile();

        VBox root = new VBox(15);
        HBox center = new HBox(25);
        center.setAlignment(Pos.CENTER);
        VBox leftbox = new VBox(25);leftbox.setId("leftbox");
        VBox header = new VBox(10);
        VBox midBox = new VBox(20);midBox.setId("midbox");
        VBox monthBox = new VBox(10);
        VBox categoryBox = new VBox(10);
        VBox expenseBox = new VBox(10);
        VBox totalBudgetBox = new VBox(10);
        VBox withinBudgetBox = new VBox(10);
        VBox budgetBox = new VBox(10);

        root.getStylesheets().add(css);
        //topBox
        // monthBox
        monthSelection = new ComboBox<>();
        monthSelection.setId("monthSelection");
        monthSelection.setMinWidth(200);
        List<String> months = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
        monthSelection.getItems().addAll(months);
        monthSelection.setValue("JAN");
        monthSelection.setStyle("-fx-font-size: 12 px; -fx-text-fill: white");
        monthBox.getChildren().addAll(monthSelection);
        monthBox.setAlignment(Pos.CENTER);
        VBox.setMargin(monthSelection, new Insets(10, 0, 0, 0));


        //title
        Label titleLabel = new Label("BUDGET PLANNING");
        titleLabel.setId("titleLabel");
        titleLabel.setAlignment(Pos.CENTER);
        Label budgetInformation = new Label("Set your budget for each category and each month");
        budgetInformation.setId("budgetLabel");
         budgetInformation.setAlignment(Pos.CENTER);
        header.getChildren().addAll(titleLabel, budgetInformation);
        header.setAlignment(Pos.CENTER);



        //midBox
        // categoryBox
        categorySelection = new ComboBox<>();
        categorySelection.setId("categorySelection");
        categorySelection.setMinWidth(200);
        List<String> categories = Arrays.asList("Shopping", "Education", "Electronics", "Entertainment", "Food and Beverages", "Health and Beauty", "Medical", "Transportation", "Other Expenses");
        categorySelection.getItems().addAll(categories);
        categorySelection.setValue("Shopping");
        Label categoryLabel = new Label("Choose Category");
        categoryLabel.setId("categoryLabel");
        categoryBox.getChildren().addAll(categoryLabel, categorySelection);

        // amountBox + budget button + feedback label
        amountField = new TextField();
        amountField.setId("amountField");
        amountField.setId("amountField");
        amountField.setPromptText("Enter Budget Amount");
        amountField.setMaxSize(200, 40);

        Button addButton = new Button("Set Budget");
        addButton.setId("addButton");
        addButton.setStyle("-fx-font-size: 16px;");

        feedbackLabel = new Label();
        feedbackLabel.setTextFill(Color.GREEN);

        categoryBox.setAlignment(Pos.CENTER);

        midBox.getChildren().addAll(categoryBox, amountField,addButton, feedbackLabel);
        midBox.setAlignment(Pos.CENTER);

        //bottomBox
        //budgetBox
        budgetLabel = new Label();
        budgetLabel.setId("budgetLabel");
        budgetBox.setId("budgetBox");
        budgetBox.getChildren().addAll(budgetLabel);
        budgetBox.setAlignment(Pos.CENTER);
        budgetLabel.setStyle("-fx-translate-y: -2px");
        budgetBox.setPadding(new Insets(10,0,10,0));
        VBox.setMargin(budgetBox,new Insets(-10,0,0,0));

        //expenseBox
        expenseLabel = new Label();
        expenseLabel.setId("expenseLabel");
        expenseBox.setId("expenseBox");
        expenseBox.getChildren().addAll(expenseLabel);
        expenseBox.setAlignment(Pos.CENTER);
        expenseBox.setPadding(new Insets(10,0,10,0));
        VBox.setMargin(expenseBox, new Insets(-15,0,0,0));

        totalBudgetLabel = new Label();
        totalBudgetLabel.setId("totalBudgetLabel");
        totalBudgetBox.getChildren().addAll(totalBudgetLabel);
        totalBudgetBox.setId("totalBudgetBox");
        totalBudgetBox.setAlignment(Pos.CENTER);
        totalBudgetBox.setPadding(new Insets(10,0,10,0));
        VBox.setMargin(totalBudgetBox, new Insets(-15,0,0,0));

        //comparisonBox
        withinBudgetLabel = new Label();
        withinBudgetLabel.setId("withinBudgetLabel");
        withinBudgetBox.getChildren().addAll(withinBudgetLabel);
        withinBudgetBox.setId("withinBudgetBox");
        withinBudgetBox.setAlignment(Pos.CENTER);
        withinBudgetBox.setPadding(new Insets(10,0,10,0));
        VBox.setMargin(withinBudgetBox, new Insets(-15,0,0,0));


        // Set up event handlers
        addButton.setOnAction(e -> setBudget());
        monthSelection.setOnAction(e -> displayBudgetStatus());
        categorySelection.setOnAction(e -> displayBudgetStatus());

        leftbox.getChildren().addAll(midBox,budgetBox, expenseBox,totalBudgetBox, withinBudgetBox);
        center.getChildren().add(leftbox);

        root.getChildren().addAll(header,monthBox,center);
    // Display initial status
        displayBudgetStatus();
        return root;
    }

    private void setBudget() {
        String month = monthSelection.getValue();
        int monthInt = getMonthAsInt(month);
        String category = categorySelection.getValue();
        double tempAmount;

        try {
            tempAmount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Please enter a valid amount.");
            feedbackLabel.setTextFill(Color.RED);
            return;
        }

        // Save budget to ArrayList and file
        saveBudget(userId, monthInt, category, tempAmount);

        feedbackLabel.setText("Budget added & updated successfully!");
        displayBudgetStatus();
        double totalBudgetMonthly = calculateTotalBudgetForMonth(userId,monthInt);
        // Clear input fields
        amountField.clear();
        updateDashboardBudgetInfo(userId, monthInt, totalBudgetMonthly);
    }

    private void displayBudgetStatus() {
        String month = monthSelection.getValue();
        int monthInt = getMonthAsInt(month);
        String category = categorySelection.getValue();
        double budgetAmount = readBudget(userId, monthInt, category);
        calculateTotalBudgetForMonth(userId,monthInt);
        double[] expenses;
        try {
            expenses = BudgetApp.readAndCalculateExpenses(userId, monthInt);
        } catch (FileNotFoundException e) {
            feedbackLabel.setText("Expenses file not found.");
            return;
        }

        double expenseAmount = 0;

        switch (category) {
            case "Shopping":
                expenseAmount = expenses[0];
                break;
            case "Education":
                expenseAmount = expenses[1];
                break;
            case "Electronics":
                expenseAmount = expenses[2];
                break;
            case "Entertainment":
                expenseAmount = expenses[3];
                break;
            case "Food and Beverages":
                expenseAmount = expenses[4];
                break;
            case "Health and Beauty":
                expenseAmount = expenses[5];
                break;
            case "Medical":
                expenseAmount = expenses[6];
                break;
            case "Transportation":
                expenseAmount = expenses[7];
                break;
            case "Other Expenses":
                expenseAmount = expenses[8];
                break;
        }

        budgetLabel.setText(category+" budget:" + String.format("\nRM%.2f", budgetAmount));
        expenseLabel.setText("Total Expense for "+category+":" + String.format("\nRM%.2f", expenseAmount));

        if (expenseAmount > budgetAmount) {
            withinBudgetLabel.setText("You have exceeded\n the budget!!!");
            withinBudgetLabel.setTextFill(Color.RED);
        } else {
            withinBudgetLabel.setText("You are still within\n the budget!!!");
            withinBudgetLabel.setTextFill(Color.GREEN);
        }

    }

    // Save budget to ArrayList and text file
    private void saveBudget(String userId, int month, String budgetCategory, double budgetAmount) {
        boolean found = false;

        for (BudgetApp budgetItem : budgetList) {
            if (budgetItem.getUserId().equals(userId) && budgetItem.getMonth()==month && budgetItem.getBudgetCategory().equals(budgetCategory)) {
               budgetItem.setBudgetAmount(budgetAmount);
                found = true;
                break;
            }
        }

        if (!found) {
            BudgetApp BudgetItem = new BudgetApp(userId, month, budgetCategory, budgetAmount);
            budgetList.add(BudgetItem);
        }

        // Write to text file
        try (FileWriter writer = new FileWriter(BudgetFile)) {
            for (BudgetApp budgetItem : budgetList) {
                writer.write(budgetItem.getUserId() + "," + budgetItem.getMonth() + "," + budgetItem.getBudgetCategory() + "," + budgetItem.getBudgetAmount() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Read budget from ArrayList
    public double readBudget(String userId, int month, String category) {
        for (BudgetApp budgetItem : budgetList) {
            if (budgetItem.getUserId().equals(userId) && budgetItem.getMonth() == month && budgetItem.getBudgetCategory().equals(category)) {
                return budgetItem.getBudgetAmount();
            }
        }
        return 0.00; // Default value if no budget is found
    }


    // Load budgets from file into ArrayList
    private void loadBudgetsFromFile() {
        budgetList.clear();

        try (Scanner scanner = new Scanner(new File(BudgetFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] delimiter = line.split(",");
                if (delimiter.length == 4) {
                    BudgetApp budgetItem = new BudgetApp(delimiter[0], Integer.parseInt(delimiter[1]), delimiter[2], Double.parseDouble(delimiter[3]));
                    budgetList.add(budgetItem);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    private int getMonthAsInt(String month) {
        switch (month) {
            case "JAN":
                return 1;
            case "FEB":
                return 2;
            case "MAR":
                return 3;
            case "APR":
                return 4;
            case "MAY":
                return 5;
            case "JUN":
                return 6;
            case "JUL":
                return 7;
            case "AUG":
                return 8;
            case "SEP":
                return 9;
            case "OCT":
                return 10;
            case "NOV":
                return 11;
            case "DEC":
                return 12;
            default:
                return -1; // invalid
        }


    }

    private double calculateTotalBudgetForMonth(String userId, int month) {
        double totalBudget = 0;
        for (BudgetApp budgetItem : budgetList) {
            if (budgetItem.getMonth() == month && budgetItem.getUserId().equals(userId) ) {
                totalBudget += budgetItem.getBudgetAmount();
            }
        }
        totalBudgetLabel.setText("Total budget for this month: \nRM"+totalBudget);
       return totalBudget;
    }

    public void updateDashboardBudgetInfo(String userId, int month, double updatedBudget) {
        String filePath = "src/main/java/com/biscuittaiger/budgettrackerx/Model/DashboardData.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            // Read the header line
            String headerLine = reader.readLine();
            if (headerLine != null) {
                stringBuilder.append(headerLine).append("\n");
            }

            // Read each line and update the values if needed
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if the line has the correct number of columns and non-empty userId and month
                if (parts.length == 7 && !parts[0].isEmpty() && !parts[1].isEmpty()) {
                    // Check if this is the line to edit
                    if (userId.equals(parts[0]) && String.valueOf(month).equals(parts[1])) {
                        // Update the values
                        parts[5] = String.valueOf(updatedBudget);// Update the budget
                    }
                }
                // Reconstruct the line
                StringBuilder newLine = new StringBuilder();
                for (int i = 0; i < parts.length; i++) {
                    newLine.append(parts[i]);
                    if (i < parts.length - 1) {
                        newLine.append(",");
                    }
                }

                stringBuilder.append(newLine).append("\n");
            }
            reader.close();

            // Write the modified content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(stringBuilder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
