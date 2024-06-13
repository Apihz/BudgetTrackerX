package com.biscuittaiger.budgettrackerx.App;

import com.biscuittaiger.budgettrackerx.View.AnalysisView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AnalysisApp {
    private BorderPane root;
    private AnalysisView analysisView;
    private ComboBox<String> monthSelector;

    private static final String[] MONTHS = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    public BorderPane ExpenseAnalysisApp(String userId) {
        String css = this.getClass().getResource("/com/biscuittaiger/budgettrackerx/analysis.css").toExternalForm();
        root = new BorderPane();
        root.getStylesheets().add(css);
        analysisView = new AnalysisView(userId);

        monthSelector = new ComboBox<>();
        monthSelector.setId("monthSelector");
        monthSelector.getItems().addAll(MONTHS);
        monthSelector.setValue(MONTHS[0]);
        monthSelector.setOnAction(e -> {
            int selectedMonthIndex = monthSelector.getSelectionModel().getSelectedIndex() + 1;
            analysisView.updateChart(userId,selectedMonthIndex);
        });

        root.setLeft(monthSelector);
        root.setCenter(analysisView);

        return root;
    }
}
