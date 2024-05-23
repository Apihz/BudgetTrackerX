package com.biscuittaiger.budgettrackerx.View;

import com.biscuittaiger.budgettrackerx.App.DashboardApp;
import com.biscuittaiger.budgettrackerx.Model.IconPack;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Node;
import java.util.function.Function;
import java.util.Arrays;

public class DashboardView extends MainAppView {
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

        // Create a MenuButton with options for each month
        MenuButton monthMenuButton = new MenuButton("Select Month");
        monthMenuButton.setId("monthMenuButton");
        for (int i = 1; i <= 12; i++) {
            MenuItem monthItem = new MenuItem(getMonthName(i));
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
        topHeader.getChildren().addAll(greetings, menuPane, notificationButton);
        HBox.setMargin(menuPane, new Insets(10));

        month = 1; // Initialize month to January
        dashboard = new DashboardApp("1", month);
        dashboard.readInformationFromFile();

        HBox moneyOverview = new HBox(10);
        moneyOverview.setId("moneyOverview");

        VBox balanceBox = new VBox();
        balanceBox.setId("balanceBox");
        Text text1 = new Text("Balance");
        text1.setId("text1");
        balanceText = new Text("RM " + dashboard.getBalance(month));
        balanceText.setId("balance");
        LineChart<Number, Number> balanceChart = createLineChart("Balance", month -> dashboard.getBalance(month));
        balanceChart.setId("balanceChart");
        balanceBox.getChildren().addAll(text1, balanceText, balanceChart);

        VBox incomeBox = new VBox();
        incomeBox.setId("incomeBox");
        Text text2 = new Text("Income");
        text2.setId("text2");
        incomeText = new Text("RM " + dashboard.getIncome(month));
        incomeText.setId("income");
        LineChart<Number, Number> incomeChart = createLineChart("Income", month -> dashboard.getIncome(month));
        incomeChart.setId("incomeChart");
        incomeBox.getChildren().addAll(text2, incomeText, incomeChart);

        VBox expenseBox = new VBox();
        expenseBox.setId("expenseBox");
        Text text3 = new Text("Expense");
        text3.setId("text3");
        expenseText = new Text("RM " + dashboard.getExpense(month));
        expenseText.setId("expense");
        LineChart<Number, Number> expenseChart = createLineChart("Expense", month -> dashboard.getExpense(month));
        expenseChart.setId("expenseChart");
        expenseBox.getChildren().addAll(text3, expenseText, expenseChart);

        VBox budgetBox = new VBox();
        budgetBox.setId("budgetBox");
        Text text4 = new Text("Budget");
        text4.setId("text4");
        budgetText = new Text("RM " + dashboard.getBudget(month));
        budgetText.setId("budget");
        LineChart<Number, Number> budgetChart = createLineChart("Budget", month -> dashboard.getBudget(month));
        budgetChart.setId("budgetChart");
        budgetBox.getChildren().addAll(text4, budgetText, budgetChart);

        VBox savingsBox = new VBox();
        savingsBox.setId("savingsBox");
        Text text5 = new Text("Savings");
        text5.setId("text5");
        savingsText = new Text("RM " + dashboard.getSavings(month));
        savingsText.setId("savings");
        LineChart<Number, Number> savingsChart = createLineChart("Savings", month -> dashboard.getSavings(month));
        savingsChart.setId("savingsChart");
        savingsBox.getChildren().addAll(text5, savingsText, savingsChart);

        for (Node box : Arrays.asList(balanceBox, incomeBox, expenseBox, budgetBox, savingsBox,savingsBox)) {
            VBox.setVgrow(box, Priority.ALWAYS);
            HBox.setHgrow(box, Priority.ALWAYS);
        }

        moneyOverview.setPadding(new Insets(10, 10, 10, 10));
        moneyOverview.getChildren().addAll(balanceBox, incomeBox, expenseBox, budgetBox, savingsBox);

        root.getChildren().addAll(topHeader, moneyOverview);

        return root;
    }

    private void updateDisplayedInformation() {
        balanceText.setText("RM " + dashboard.getBalance(month));
        incomeText.setText("RM " + dashboard.getIncome(month));
        expenseText.setText("RM " + dashboard.getExpense(month));
        budgetText.setText("RM " + dashboard.getBudget(month));
        savingsText.setText("RM " + dashboard.getSavings(month));
    }

    private LineChart<Number, Number> createLineChart(String title, Function<Integer, Number> dataFunction) {
        NumberAxis xAxis = new NumberAxis(1, 12, 1);
        NumberAxis yAxis = new NumberAxis();

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setId("customLineChart");
        lineChart.setTitle(title);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i = 1; i <= 12; i++) {
            series.getData().add(new XYChart.Data<>(i, dataFunction.apply(i)));
        }

        lineChart.getData().add(series);

        for (XYChart.Data<Number, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-background-color: transparent, transparent;");
        }

        return lineChart;
    }

    private String getMonthName(int month) {
        switch(month) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "";
        }
    }
}
