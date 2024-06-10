package com.biscuittaiger.budgettrackerx.View;

import com.biscuittaiger.budgettrackerx.App.AnalysisApp;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

public class AnalysisView {
    private BorderPane root;
    private AnalysisApp analysisApp;
    private ComboBox<String> monthSelector;

    private static final String[] MONTHS ={
            "January", "February", "March", "April","May", "June", "July", "August",
            "September", "October", "November", "December"
    };



    public BorderPane ExpenseAnalysisApp(String userId){

        root = new BorderPane();
        analysisApp = new AnalysisApp(userId);

        monthSelector = new ComboBox<>();
        monthSelector.getItems().addAll(MONTHS);
        monthSelector.setValue(MONTHS[0]);
        monthSelector.setOnAction(e -> {
            int selectedMonthIndex = monthSelector.getSelectionModel().getSelectedIndex() + 1;
            analysisApp.updateChart(selectedMonthIndex);
        });

        root.setRight(monthSelector);
        root.setCenter(analysisApp);
        return root;
    }

}
