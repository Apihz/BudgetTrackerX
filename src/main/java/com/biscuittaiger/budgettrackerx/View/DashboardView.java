package com.biscuittaiger.budgettrackerx.View;

import com.biscuittaiger.budgettrackerx.App.BudgetApp;
import com.biscuittaiger.budgettrackerx.App.DashboardApp;
import com.biscuittaiger.budgettrackerx.App.TransactionApp;
import com.biscuittaiger.budgettrackerx.Model.IconPack;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class DashboardView {
    private Text balanceText;
    private Text incomeText;
    private Text expenseText;
    private Text budgetText;
    private Text savingsText;
    private int month;
    private DashboardApp dashboard;
    private Label dateTimeLabel;
    private String userId;
    private TableView<TransactionApp> transactionTable;
    private VBox notifications;
    private VBox balanceBox;
    private VBox incomeBox;
    private VBox expenseBox;
    private VBox budgetBox;
    private VBox savingsBox;

    public VBox DashboardOverview(String userId, String username) {
        DashboardView dashboardView = this;
        dashboardView.userId = userId;
        String css = this.getClass().getResource("/com/biscuittaiger/budgettrackerx/dashboardX.css").toExternalForm();
        IconPack icon = new IconPack();

        month = 1; // Initialize month to January
        dashboard = new DashboardApp(userId, month);
        dashboard.readInformationFromFile();

        VBox root = new VBox();
        root.getStylesheets().add(css);

        HBox topHeader = new HBox();
        topHeader.setId("topHeader");
        VBox greetings = new VBox();
        Label headerLabel = new Label("Welcome back " + username + " to Budget Tracker");
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

        dateTimeLabel = new Label();
        dateTimeLabel.setId("dateTimeLabel");
        updateDateTime();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        topHeader.getChildren().addAll(greetings, dateTimeLabel);

        HBox moneyOverview = new HBox(10);
        moneyOverview.setId("moneyOverview");

        balanceBox = new VBox();
        balanceBox.setId("balanceBox");
        Text text1 = new Text("Balance");
        text1.setId("text1");
        balanceText = new Text("RM " + dashboard.getBalance(month));
        balanceText.setId("balance");
        LineChart<Number, Number> balanceChart = dashboard.createLineChart("Balance", month -> dashboard.getBalance(month));
        balanceChart.setId("balanceChart");
        balanceBox.getChildren().addAll(text1, balanceText, balanceChart);

        incomeBox = new VBox();
        incomeBox.setId("incomeBox");
        Text text2 = new Text("Income");
        text2.setId("text2");
        incomeText = new Text("RM " + dashboard.getIncome(month));
        incomeText.setId("income");
        LineChart<Number, Number> incomeChart = dashboard.createLineChart("Income", month -> dashboard.getIncome(month));
        incomeChart.setId("incomeChart");
        incomeBox.getChildren().addAll(text2, incomeText, incomeChart);

        expenseBox = new VBox();
        expenseBox.setId("expenseBox");
        Text text3 = new Text("Expense");
        text3.setId("text3");
        expenseText = new Text("RM " + dashboard.getExpense(month));
        expenseText.setId("expense");
        LineChart<Number, Number> expenseChart = dashboard.createLineChart("Expense", month -> dashboard.getExpense(month));
        expenseChart.setId("expenseChart");
        expenseBox.getChildren().addAll(text3, expenseText, expenseChart);

        budgetBox = new VBox();
        budgetBox.setId("budgetBox");
        Text text4 = new Text("Budget");
        text4.setId("text4");
        budgetText = new Text("RM " + dashboard.getBudget(month));
        budgetText.setId("budget");
        LineChart<Number, Number> budgetChart = dashboard.createLineChart("Budget", month -> dashboard.getBudget(month));
        budgetChart.setId("budgetChart");
        budgetBox.getChildren().addAll(text4, budgetText, budgetChart);

        savingsBox = new VBox();
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

        Label recentTransactionText = new Label("Recent Transactions");
        recentTransactionText.setId("recentTransactionText");

        transactionTable = new TableView<>();
        transactionTable.setId("transactionTable");

        TableColumn<TransactionApp, String> transactionId = new TableColumn<>("ID");
        transactionId.setCellValueFactory(new PropertyValueFactory<>("tranId"));

        TableColumn<TransactionApp, String> transactionCol = new TableColumn<>("Transaction");
        transactionCol.setCellValueFactory(new PropertyValueFactory<>("details"));

        TableColumn<TransactionApp, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<TransactionApp, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<TransactionApp, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        transactionId.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.05));
        transactionCol.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.37));
        amountCol.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.15));
        dateCol.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.2));
        categoryCol.prefWidthProperty().bind(transactionTable.widthProperty().multiply(0.2));

        transactionId.setResizable(false);
        transactionCol.setResizable(false);
        amountCol.setResizable(false);
        dateCol.setResizable(false);
        categoryCol.setResizable(false);

        transactionTable.setPrefWidth(350);
        transactionTable.setMaxHeight(375);

        transactionTable.getColumns().addAll(transactionId, transactionCol, amountCol, dateCol, categoryCol);

        ObservableList<TransactionApp> data = getTransactionsFromFile(userId, String.valueOf(month));
        transactionTable.setItems(data);


        recentTransaction.getChildren().addAll(recentTransactionText, transactionTable);

        updateTransactions(userId, transactionTable);

        VBox notificationBox = new VBox();
        notificationBox.setId("notificationBox");
        Label notificationText = new Label("Notifications");
        notificationText.setId("notificationText");
        VBox notificationHeader = new VBox();
        notificationHeader.setId("notificationHeader");
        notificationHeader.getChildren().addAll(notificationText);
        notifications = new VBox();
        notifications.setId("notifications");
        for(int i=0;i<12;i++) {
            checkAndDisplayNotification(i);
        }
        if (notifications.getChildren().isEmpty()) {
            Label noNotifications = new Label("No notifications");
            noNotifications.setId("No Notifications");
            notifications.getChildren().add(noNotifications);
        }
        ScrollPane scrollPane = new ScrollPane(notifications);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(380);

        scrollPane.setId("notificationScrollPane");

        notificationBox.getChildren().addAll(notificationHeader,  scrollPane);

        thirdRow.getChildren().addAll(recentTransaction, notificationBox);


        moneyOverview.setPadding(new Insets(10, 10, 10, 10));
        moneyOverview.getChildren().addAll(balanceBox, incomeBox, expenseBox, budgetBox, savingsBox);

        VBox.setMargin(menuPane, new Insets(20, 0, -50, 15));

        animateCharts(balanceBox, incomeBox, expenseBox, budgetBox, savingsBox);

        root.getChildren().addAll(topHeader, menuPane, moneyOverview, thirdRow);

        return root;
    }

    private void applyFadeTransition(Node node, double durationSeconds) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(durationSeconds), node);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    private void applyTranslateTransition(Node node, double durationSeconds, double fromX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(durationSeconds), node);
        translateTransition.setFromX(fromX);
        translateTransition.setToX(0);
        translateTransition.play();
    }

    private void animateCharts(VBox... boxes) {
        double durationSeconds = 0.5;
        double initialOffsetX = 30.0;
        for (VBox box : boxes) {
            applyFadeTransition(box, durationSeconds);
            applyTranslateTransition(box, durationSeconds, initialOffsetX);
        }
    }

    private void updateDisplayedInformation() {
        balanceText.setText("RM " + dashboard.getBalance(month));
        incomeText.setText("RM " + dashboard.getIncome(month));
        expenseText.setText("RM " + dashboard.getExpense(month));
        budgetText.setText("RM " + dashboard.getBudget(month));
        savingsText.setText("RM " + dashboard.getSavings(month));

        updateTransactions(userId, transactionTable);
        animateCharts(balanceBox, incomeBox, expenseBox, budgetBox, savingsBox); // Apply animations

    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE hh:mm:ss a");
        String formattedDateTime = now.format(formatter);
        formattedDateTime = formattedDateTime.toUpperCase();
        dateTimeLabel.setText(formattedDateTime);
    }
    private void updateTransactions(String userId, TableView<TransactionApp> transactionTable) {
        ObservableList<TransactionApp> data = getTransactionsFromFile(userId, String.valueOf(month));
        transactionTable.setItems(data);
    }

    private ObservableList<TransactionApp> getTransactionsFromFile(String userId, String month) {
        ObservableList<TransactionApp> transactions = FXCollections.observableArrayList();
        String fileName = "src/main/java/com/biscuittaiger/budgettrackerx/Model/TransactionTEST.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(userId) && fields[1].equals(month) && fields[3].equals("expense")) {
                    String id = fields[0];
                    double amount = Double.parseDouble(fields[2]);
                    String type = fields[3];
                    String category = fields[4];
                    String details = fields[5];
                    String date = fields[6];
                    String tranId = fields[7];

                    TransactionApp transaction = new TransactionApp(Integer.parseInt(id), userId, month, amount, type, category, details, date, tranId);
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private void checkAndDisplayNotification(int month) {
        String[] categories = {"Shopping", "Education", "Electronics", "Entertainment", "Food and Beverages", "Health and Beauty", "Medical", "Transportation", "Other Expenses"};
        BudgetView budget = new BudgetView();
        double[] categoryBudget = new double[categories.length];
        double[] categoryExpense;
        try {
            categoryExpense = BudgetApp.readAndCalculateExpenses(userId, month);
        } catch (FileNotFoundException e) {
            return;
        }

        for (int i = 0; i < categories.length; i++) {
            categoryBudget[i] = budget.readBudget(userId, month, categories[i]);
            System.out.println("Budget for " + categories[i] + ": " + categoryBudget[i]); // Debugging statement
        }

        for (int i = 0; i < categories.length; i++) {
            if (categoryExpense[i] > categoryBudget[i]) {
                String message = "Your expenses for the category '" + categories[i] + "' in "+dashboard.getMonthName(month)+" have exceeded the budget!";
                VBox notificationBox = createNotificationBox(message);
                VBox.setMargin(notificationBox, new Insets(5, 0, 0, 0)); // Adds space around each notification box
                notifications.getChildren().add(notificationBox);
                ScrollPane scrollPane = new ScrollPane(notifications);
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);
            }
        }
    }
    private VBox createNotificationBox(String message) {
        VBox notificationList = new VBox();
        ScrollPane scrollPane = new ScrollPane(notificationList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setId("notificationScrollPane");
        notificationList.setPadding(new Insets(5)); // Adds padding inside each notification box
        notificationList.setStyle(
                "-fx-border-color: #343434; " +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-background-color: #343434"


        ); // Adds border to each notification box
        notificationList.setMaxWidth(275);
        Label notificationLabel = new Label(message);
        notificationLabel.setId("notificationLabel");
        notificationLabel.setWrapText(true);
        notificationLabel.getStyleClass().add("notification-message");

        notificationList.getChildren().add(notificationLabel);

        System.out.println("Notification added: " + message); // Debugging statement

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), notificationList);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        return notificationList;
    }



}
