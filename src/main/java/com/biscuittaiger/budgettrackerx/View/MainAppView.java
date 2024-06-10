package com.biscuittaiger.budgettrackerx.View;

import com.biscuittaiger.budgettrackerx.Model.FontPack;
import com.biscuittaiger.budgettrackerx.Model.IconPack;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainAppView extends Application {

    public void start(Stage primaryStage, String userId, String username) {
        String css = this.getClass().getResource("/com/biscuittaiger/budgettrackerx/mainApp.css").toExternalForm();

        FontPack fontPack = new FontPack();
        fontPack.getFontJetBrain();

        HBox root = new HBox();
        root.getStylesheets().add(css);

        VBox leftBar = new VBox();
        leftBar.setId("leftBar");
        VBox.setVgrow(leftBar, Priority.ALWAYS);
        leftBar.maxWidthProperty().bind(root.widthProperty().multiply(0.2)); // 20% width
        Label leftHeader = new Label("BT Budget Tracker");
        leftHeader.setId("leftHeader");
        VBox leftToolBar = new VBox(10);
        leftToolBar.setId("leftToolBar");
        leftToolBar.setPadding(new Insets(100, 10, 10, 10));

        VBox rightBar = new VBox();
        rightBar.setId("rightBar");

        IconPack icon = new IconPack();
        DashboardView dashboardView = new DashboardView();
        TransactionView transactionView = new TransactionView(userId);
        BudgetView budgetView = new BudgetView();
        AnalysisView analysisView = new AnalysisView();

        rightBar.getChildren().add(dashboardView.DashboardOverview(userId,username));//initialized dashboardView upon successful login
        Button button1 = new Button("Dashboard");
        button1.setId("button1");
        button1.setGraphic(icon.getDashboardIcon());
        button1.setOnAction(e ->  {
            rightBar.getChildren().clear();
            rightBar.getChildren().add(dashboardView.DashboardOverview(userId,username));
        });
        Button button2 = new Button("Transaction");
        button2.setId("button2");
        button2.setGraphic(icon.getTransactionIcon());
        button2.setOnAction(e -> {
            rightBar.getChildren().clear();
            rightBar.getChildren().add(transactionView.getTransactionView());
        });
        Button button3 = new Button("Analytics");
        button3.setId("button3");
        button3.setGraphic(icon.getAnalyticsIcon());
        button3.setOnAction(e -> {
            rightBar.getChildren().clear();
            rightBar.getChildren().add(analysisView.ExpenseAnalysisApp(userId));
        });
        Button button4 = new Button("Budget Planning");
        button4.setId("button4");
        button4.setGraphic(icon.getPlanningIcon());
        button4.setOnAction(e -> {
            rightBar.getChildren().clear();
            rightBar.getChildren().add(budgetView.BudgetView(userId));
        });
        Button button5 = new Button("Notification");
        button5.setId("button5");
        button5.setGraphic(icon.getNotificationIcon());
        leftToolBar.getChildren().addAll(leftHeader, button1, button2, button3, button4, button5);
        leftBar.getChildren().addAll(leftHeader, leftToolBar);


        VBox.setVgrow(rightBar, Priority.ALWAYS);
        rightBar.minWidthProperty().bind(root.widthProperty().multiply(0.8)); // 80% width

        root.getChildren().addAll(leftBar, rightBar);
        HBox.setHgrow(rightBar, Priority.ALWAYS);

        Scene scene = new Scene(root, 1300, 760);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Budget Tracker");
        primaryStage.setMinWidth(1200*0.8);
        primaryStage.setMinHeight(800*0.8);
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> adjustFontSize(root, newVal.doubleValue(), primaryStage.getHeight()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> adjustFontSize(root, primaryStage.getWidth(), newVal.doubleValue()));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void adjustFontSize(HBox root, double newWidth, double newHeight) {
        double fontSize = Math.min(newWidth, newHeight) / 70; // Scale based on the smaller dimension
        root.setStyle("-fx-font-size: " + fontSize + "px;");
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
