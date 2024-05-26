package com.biscuittaiger.budgettrackerx.View;

import com.biscuittaiger.budgettrackerx.App.DashboardApp;
import com.biscuittaiger.budgettrackerx.Model.IconPack;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.Node;

import java.util.Arrays;

public class DashboardView {
    private Text balanceText;
    private Text incomeText;
    private Text expenseText;
    private Text budgetText;
    private Text savingsText;
    private int month;
    private DashboardApp dashboard;

    public VBox DashboardOverview() {
        String css = this.getClass().getResource("/com/biscuittaiger/budgettrackerx/dashboardX.css").toExternalForm();
        IconPack icon = new IconPack();

        month = 1; // Initialize month to January
        dashboard = new DashboardApp("1", month);
        dashboard.readInformationFromFile();

        VBox root = new VBox();
        root.getStylesheets().add(css);

        HBox topHeader = new HBox();
        topHeader.setId("topHeader");
        VBox greetings = new VBox();
        Label headerLabel = new Label("Welcome to Budget Tracker");
        headerLabel.setId("headerLabel");
        Text headerText = new Text("Track, manage and forecast your budgets and expenditure");
        headerText.setId("headerText");
        greetings.getChildren().addAll(headerLabel, headerText);

        MenuButton monthMenuButton = new MenuButton("Select Month");
        monthMenuButton.setId("monthMenuButton");
        for (int i = 1; i <= 12; i++) {
            MenuItem monthItem = new MenuItem(dashboard.getMonthName(i));
            monthItem.setId("monthItem" + i);
            final int monthIndex = i;
            monthItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    month = monthIndex;
                    updateDisplayedInformation();
                }
            });
            monthMenuButton.getItems().add(monthItem);
        }
        Pane menuPane = new Pane();
        menuPane.getChildren().add(monthMenuButton);

        Button notificationButton = new Button();
        notificationButton.setId("notificationButton");
        notificationButton.setGraphic(icon.getNotificationIcon());
        topHeader.getChildren().addAll(greetings, notificationButton);

        HBox moneyOverview = new HBox(10);
        moneyOverview.setId("moneyOverview");

        VBox balanceBox = new VBox();
        balanceBox.setId("balanceBox");
        Text text1 = new Text("Balance");
        text1.setId("text1");
        balanceText = new Text("RM " + dashboard.getBalance(month));
        balanceText.setId("balance");
        LineChart<Number, Number> balanceChart = dashboard.createLineChart("Balance", month -> dashboard.getBalance(month));
        balanceChart.setId("balanceChart");
        balanceBox.getChildren().addAll(text1, balanceText, balanceChart);

        VBox incomeBox = new VBox();
        incomeBox.setId("incomeBox");
        Text text2 = new Text("Income");
        text2.setId("text2");
        incomeText = new Text("RM " + dashboard.getIncome(month));
        incomeText.setId("income");
        LineChart<Number, Number> incomeChart = dashboard.createLineChart("Income", month -> dashboard.getIncome(month));
        incomeChart.setId("incomeChart");
        incomeBox.getChildren().addAll(text2, incomeText, incomeChart);

        VBox expenseBox = new VBox();
        expenseBox.setId("expenseBox");
        Text text3 = new Text("Expense");
        text3.setId("text3");
        expenseText = new Text("RM " + dashboard.getExpense(month));
        expenseText.setId("expense");
        LineChart<Number, Number> expenseChart = dashboard.createLineChart("Expense", month -> dashboard.getExpense(month));
        expenseChart.setId("expenseChart");
        expenseBox.getChildren().addAll(text3, expenseText, expenseChart);

        VBox budgetBox = new VBox();
        budgetBox.setId("budgetBox");
        Text text4 = new Text("Budget");
        text4.setId("text4");
        budgetText = new Text("RM " + dashboard.getBudget(month));
        budgetText.setId("budget");
        LineChart<Number, Number> budgetChart = dashboard.createLineChart("Budget", month -> dashboard.getBudget(month));
        budgetChart.setId("budgetChart");
        budgetBox.getChildren().addAll(text4, budgetText, budgetChart);

        VBox savingsBox = new VBox();
        savingsBox.setId("savingsBox");
        Text text5 = new Text("Savings");
        text5.setId("text5");
        savingsText = new Text("RM " + dashboard.getSavings(month));
        savingsText.setId("savings");
        LineChart<Number, Number> savingsChart = dashboard.createLineChart("Savings", month -> dashboard.getSavings(month));
        savingsChart.setId("savingsChart");
        savingsBox.getChildren().addAll(text5, savingsText, savingsChart);

        HBox thirdRow = new HBox(10);
        thirdRow.setId("thirdRow");

        VBox recentTransaction = new VBox(20);
        recentTransaction.setId("recentTransaction");
        Label recentTransactionText = new Label("Recent Transaction");recentTransactionText.setId("recentTransactionText");


        GridPane transactionGrid = new GridPane();
        transactionGrid.setId("transactionGrid");
        transactionGrid.setHgap(150);
        transactionGrid.setVgap(25);
        GridPane headerGrid = new GridPane();
        headerGrid.setId("headerGrid");
        headerGrid.setHgap(150);
        headerGrid.setVgap(0);

        String[] transactionHeaders = {"Transaction","Amount","Date","Category"};
        String[] testTransItem = {"Beli shopii","RM70","13/2/17","Shopping"};

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(transactionGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(380);

        for(int col = 0; col < transactionHeaders.length; col++) {
            Label transHeaderLabel = new Label(transactionHeaders[col]);
            transHeaderLabel.setId("transHeaderLabel");
            headerGrid.add(transHeaderLabel, col, 0);
        }
        for(int row = 0; row < 100; row++) {
            for(int col = 0; col < transactionHeaders.length; col++) {
                Label detailLabel = new Label(testTransItem[col]);
                detailLabel.setId("detailLabel");
                transactionGrid.add(detailLabel, col, row);
            }
        }

        recentTransaction.getChildren().addAll(recentTransactionText,headerGrid, transactionGrid,scrollPane);

        VBox savingGoals = new VBox();
        savingGoals.setId("savingGoals");
        VBox goalsBox = new VBox();
        goalsBox.setId("goalsBox");

        if (goalsBox.getChildren().isEmpty()) {
            Label savingGoalsLabel = new Label("No saving goal yet");savingGoalsLabel.setId("savingGoalsLabel");
            savingGoals.getChildren().add(savingGoalsLabel);
        }

        savingGoals.getChildren().add(goalsBox);

        thirdRow.getChildren().addAll(recentTransaction,savingGoals);

        moneyOverview.setPadding(new Insets(10, 10, 10, 10));
        moneyOverview.getChildren().addAll(balanceBox, incomeBox, expenseBox, budgetBox, savingsBox);
        VBox.setMargin(menuPane, new Insets(20, 0, -50, 15));
        for (Node box : Arrays.asList(balanceBox, incomeBox, expenseBox, budgetBox, savingsBox,savingsBox)) {
            VBox.setVgrow(box, Priority.ALWAYS);
            HBox.setHgrow(box, Priority.ALWAYS);
        }

        root.getChildren().addAll(topHeader,menuPane, moneyOverview, thirdRow);

        return root;
    }

    private void updateDisplayedInformation() {
        balanceText.setText("RM " + dashboard.getBalance(month));
        incomeText.setText("RM " + dashboard.getIncome(month));
        expenseText.setText("RM " + dashboard.getExpense(month));
        budgetText.setText("RM " + dashboard.getBudget(month));
        savingsText.setText("RM " + dashboard.getSavings(month));
    }


}
