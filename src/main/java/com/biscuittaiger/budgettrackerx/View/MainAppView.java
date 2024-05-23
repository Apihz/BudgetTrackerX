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

    @Override
    public void start(Stage primaryStage) {
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

        IconPack icon = new IconPack();

        Button button1 = new Button("Dashboard");
        button1.setId("button1");
        button1.setGraphic(icon.getDashboardIcon());
        Button button2 = new Button("Transaction");
        button2.setId("button2");
        button2.setGraphic(icon.getTransactionIcon());
        Button button3 = new Button("Analytics");
        button3.setId("button3");
        button3.setGraphic(icon.getAnalyticsIcon());
        Button button4 = new Button("Saving goals");
        button4.setId("button4");
        button4.setGraphic(icon.getSavingsIcon());
        leftToolBar.getChildren().addAll(leftHeader, button1, button2, button3, button4);
        leftBar.getChildren().addAll(leftHeader, leftToolBar);

        VBox rightBar = new VBox();
        rightBar.setId("rightBar");
        VBox.setVgrow(rightBar, Priority.ALWAYS);
        rightBar.minWidthProperty().bind(root.widthProperty().multiply(0.8)); // 80% width

        DashboardView dashboardView = new DashboardView();
        rightBar.getChildren().add(dashboardView.DashboardOverview());

        root.getChildren().addAll(leftBar, rightBar);
        HBox.setHgrow(rightBar, Priority.ALWAYS);

        Scene scene = new Scene(root, 1250, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Budget Tracker");
        primaryStage.setMinWidth(1200*0.8);
        primaryStage.setMinHeight(800*0.8);
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> adjustFontSize(root, newVal.doubleValue(), primaryStage.getHeight()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> adjustFontSize(root, primaryStage.getWidth(), newVal.doubleValue()));

        primaryStage.show();
    }

    private void adjustFontSize(HBox root, double newWidth, double newHeight) {
        double fontSize = Math.min(newWidth, newHeight) / 70; // Scale based on the smaller dimension
        root.setStyle("-fx-font-size: " + fontSize + "px;");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
