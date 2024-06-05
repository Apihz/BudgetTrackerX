package com.biscuittaiger.budgettrackerx.View;


import com.biscuittaiger.budgettrackerx.App.TransactionApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class TransactionView {

    private final TableView<TransactionApp> transactionTable;
    private Text incomeText;
    private Text expenseText;
    private Text balanceText;
    private Text savingsText;
    private ComboBox<String> monthComboBox;
    private final String username;
    private final ArrayList<TransactionApp> transactionApps;
    private final static String TRANS_FILE = "src/main/java/com/biscuittaiger/budgettrackerx/View/TransactioninfoTEST.txt";

    public TransactionView(String username) {
        this.username = username;
        this.transactionApps = new ArrayList<>();
        transactionTable = new TableView<>();
        setupTransactionView();
    }

    public VBox getTransactionView() {
        VBox transactionView = new VBox(10);
        transactionView.setPadding(new Insets(10));

        monthComboBox = new ComboBox<>();
        List<String> months = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
        monthComboBox.getItems().addAll(months);
        monthComboBox.setValue("JAN");

        monthComboBox.setOnAction(event -> fetchAndDisplayTransactions(username, monthComboBox.getValue()));

        HBox infoBox = new HBox(10);
        infoBox.setPadding(new Insets(10));

        // Setting up the income box
        VBox incomeBox = createInfoBox("Income", Color.LIGHTGREEN);
        incomeText = (Text) incomeBox.getChildren().get(1);

        // Setting up the expense box
        VBox expenseBox = createInfoBox("Expense", Color.LIGHTCORAL);
        expenseText = (Text) expenseBox.getChildren().get(1);

        // Setting up the balance box
        VBox balanceBox = createInfoBox("Balance", Color.LIGHTBLUE);
        balanceText = (Text) balanceBox.getChildren().get(1);

        // Setting up the savings box
        VBox savingsBox = createInfoBox("Savings", Color.LIGHTYELLOW);
        savingsText = (Text) savingsBox.getChildren().get(1);

        infoBox.getChildren().addAll(incomeBox, expenseBox, balanceBox, savingsBox);

        // Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> showAddTransactionDialog());

        transactionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editSelectedTransaction();
            }
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> editSelectedTransaction());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteSelectedTransaction());

        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        transactionView.getChildren().addAll(monthComboBox, infoBox, buttonBox, transactionTable);
        fetchAndDisplayTransactions(username, monthComboBox.getValue());
        return transactionView;
    }

    private void setupTransactionView() {
        TableColumn<TransactionApp, Integer> numberCol = new TableColumn<>("No");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<TransactionApp, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<TransactionApp, String> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));

        TableColumn<TransactionApp, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<TransactionApp, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<TransactionApp, String> tranIdCol = new TableColumn<>("TransactionApp ID");
        tranIdCol.setCellValueFactory(new PropertyValueFactory<>("tranId"));

        transactionTable.getColumns().addAll(numberCol, categoryCol, detailsCol, amountCol, dateCol, tranIdCol);

        transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //Enable row selection

        transactionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // A row is selected, you can perform any necessary actions here
            }
        });
    }

    private VBox createInfoBox(String labelText, Color backgroundColor) {
        VBox infoBox = new VBox();
        infoBox.setPrefSize(100, 100);
        infoBox.setMinSize(10, 80);
        infoBox.setMaxSize(200, 120);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: " + toHexString(backgroundColor) + "; -fx-border-color: black;");

        Label label = new Label(labelText);
        Text text = new Text("RM 0.00");
        infoBox.getChildren().addAll(label, text);

        return infoBox;
    }

    private String toHexString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    private void fetchAndDisplayTransactions(String username, String month) {
        try {
            String userId = readUserAuthFile(username);
            if (userId.endsWith("not found")) {
                transactionTable.getItems().clear();
                transactionTable.setPlaceholder(new Label(userId));
                return;
            }
            ArrayList<TransactionApp> transactionApps = readTransactionInfoFile(userId, month);
            displayTransactionInfo(transactionApps);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<TransactionApp> readTransactionInfoFile(String userId, String month) throws FileNotFoundException {
        Scanner readFile = new Scanner(new File(TRANS_FILE));
        ArrayList<TransactionApp> transactionApps = new ArrayList<>();

        int number = 1; // Start the transaction number from 1
        try {
            while (readFile.hasNext()) {
                String line = readFile.nextLine();
                String[] delimiter = line.split(",");
                if (userId.equals(delimiter[0]) && month.equals(delimiter[1])) {
                    double amount = Double.parseDouble(delimiter[2]);
                    String type = delimiter[3];
                    String category = delimiter[4];
                    String details = delimiter[5];
                    String date = delimiter[6];
                    String tranId = delimiter[7];

                    TransactionApp transactionApp = new TransactionApp(number++, userId, month, amount, type, category, details, date, tranId);
                    transactionApps.add(transactionApp);
                }
            }
        } finally {
            readFile.close();
        }

        return transactionApps;
    }

    public void displayTransactionInfo(ArrayList<TransactionApp> transactionApps) {
        double income = 0;
        double expense = 0;
        double savings = 0;
        double balance = 0;

        for (TransactionApp transactionApp : transactionApps) {
            double amount = transactionApp.getAmount();
            String type = transactionApp.getType();

            if (type.equals("income")) {
                income += amount;
            } else if (type.equals("expense")) {
                expense += amount;
            } else if (type.equals("savings")) {
                savings += amount;
            }

            balance = income - expense - savings;
        }

        incomeText.setText("RM " + String.format("%.2f", income));
        expenseText.setText("RM " + String.format("%.2f", expense));
        balanceText.setText("RM " + String.format("%.2f", balance));
        savingsText.setText("RM " + String.format("%.2f", savings));

        ObservableList<TransactionApp> transactionAppData = FXCollections.observableArrayList(transactionApps);
        transactionTable.setItems(transactionAppData);

        if (transactionApps.isEmpty()) {
            transactionTable.setPlaceholder(new Label("No data found"));
        }
    }

    public static String readUserAuthFile(String inputUsrnm) throws FileNotFoundException {
        Scanner readFile = new Scanner(new File(TRANS_FILE));
        try {
            while (readFile.hasNext()) {
                String line = readFile.nextLine();
                String[] delimiter = line.split(",");
                if (inputUsrnm.equals(delimiter[0])) {
                    return delimiter[1];
                }
            }
        } finally {
            readFile.close();
        }
        return inputUsrnm + " not found";
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //--------------------------------------- FOR THE ADD BUTTON  FUNCTION-----------------------------------//
    private void showAddTransactionDialog() {
        String userId;
        try {
            userId = readUserAuthFile(username);
        } catch (FileNotFoundException e) {
            showAlert("Error", "User authentication file not found");
            return;
        }

        // Create a new stage for the pop-up dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add TransactionApp");

        // Create the form fields
        TextField amountField = new TextField();
        TextField monthField = new TextField();
        TextField dayField = new TextField();
        TextField categoryField = new TextField();
        TextField detailsField = new TextField();

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("income", "expense", "savings");
        typeComboBox.setValue("expense"); // Set default value

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");

        // Layout the form
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Amount:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Month (JAN-DEC):"), 0, 1);
        grid.add(monthField, 1, 1);
        grid.add(new Label("Day:"), 0, 2);
        grid.add(dayField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Details:"), 0, 4);
        grid.add(detailsField, 1, 4);
        grid.add(new Label("Type:"), 0, 5);
        grid.add(typeComboBox, 1, 5);
        grid.add(addButton, 0, 6);
        grid.add(cancelButton, 1, 6);

        Scene dialogScene = new Scene(grid, 400, 300);
        dialogStage.setScene(dialogScene);

        addButton.setOnAction(e -> {
            try {

                double amount = Double.parseDouble(amountField.getText());
                String month = monthField.getText().toUpperCase();
                int day = Integer.parseInt(dayField.getText());
                String category = categoryField.getText();
                String details = detailsField.getText();
                String type = typeComboBox.getValue();

                if (!isValidDate(month, day)) {
                    showAlert("Invalid Date", "Please enter a valid date.");
                    return;
                }

                String transactionId = generateTransactionId(month, day);
                TransactionApp newTransactionApp = new TransactionApp(transactionApps.size() + 1, userId, month, amount, type, category, details, "2024-" + getMonthNumber(month) + "-" + String.format("%02d", day), transactionId);
                transactionApps.add(newTransactionApp);
                appendTransactionToFile(newTransactionApp);

                // Refresh the table view
                transactionTable.getItems().setAll(transactionApps);
                dialogStage.close();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for amount and day.");
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        dialogStage.showAndWait();
    }

    private boolean isValidDate(String month, int day) {
        int maxDays = getDaysInMonth(month);
        return day > 0 && day <= maxDays;
    }

    private int getDaysInMonth(String month) {
        switch (month) {
            case "JAN":
            case "MAR":
            case "MAY":
            case "JUL":
            case "AUG":
            case "OCT":
            case "DEC":
                return 31;
            case "APR":
            case "JUN":
            case "SEP":
            case "NOV":
                return 30;
            case "FEB":
                return 29; // 2024 is a leap year
            default:
                return 0;
        }
    }

    private String generateTransactionId(String month, int day) {
        int randomNum = (int) (Math.random() * 100); // Generate a random two-digit number
        String formattedDay = String.format("%02d", day);
        String formattedRandomNum = String.format("%02d", randomNum);
        return "24" + getMonthNumber(month) + formattedDay + formattedRandomNum;
    }

    private String getMonthNumber(String month) {
        switch (month) {
            case "JAN":
                return "01";
            case "FEB":
                return "02";
            case "MAR":
                return "03";
            case "APR":
                return "04";
            case "MAY":
                return "05";
            case "JUN":
                return "06";
            case "JUL":
                return "07";
            case "AUG":
                return "08";
            case "SEP":
                return "09";
            case "OCT":
                return "10";
            case "NOV":
                return "11";
            case "DEC":
                return "12";
            default:
                return "00";
        }
    }

    private void appendTransactionToFile(TransactionApp transactionApp) {
        String filePath = TRANS_FILE;
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String transactionString = String.join(",",
                    transactionApp.getUserId(),
                    transactionApp.getMonth(),
                    String.valueOf(transactionApp.getAmount()),
                    transactionApp.getType(),
                    transactionApp.getCategory(),
                    transactionApp.getDetails(),
                    transactionApp.getDate(),
                    transactionApp.getTranId()
            );
            writer.write(transactionString);
            writer.newLine();
        } catch (IOException e) {
            showAlert("File Error", "An error occurred while writing to the file.");
        }
    }

    //-------------------------------------------------------------------------------------------------------//
    //--------------------------------------- FOR THE DELETE BUTTON  FUNCTION--------------------------------//
    private void deleteSelectedTransaction() {
        TransactionApp selectedTransactionApp = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransactionApp == null) {
            showAlert("Error", "Please select a transaction to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete TransactionApp");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete this transaction?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete from UI
            transactionApps.remove(selectedTransactionApp);
            transactionTable.getItems().setAll(transactionApps);

            // Delete from file
            deleteTransactionFromFile(selectedTransactionApp);
        }
    }

    private void deleteTransactionFromFile(TransactionApp transactionAppToDelete) {
        String filePath = TRANS_FILE;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                if (!parts[7].equals(transactionAppToDelete.getTranId())) {
                    updatedLines.add(line);
                }
            }
            Files.write(Paths.get(filePath), updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            showAlert("Error", "An error occurred while deleting the transaction from the file.");
        }
    }

    //-------------------------------------------------------------------------------------------------------//
    //--------------------------------------- FOR THE EDIT BUTTON  FUNCTION----------------------------------//
    private void editSelectedTransaction() {
        TransactionApp selectedTransactionApp = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransactionApp == null) {
            showAlert("Error", "Please select a transaction to edit.");
            return;
        }

        // Read userId and transactionId
//        String userId = selectedTransactionApp.getUserId();
//        String transactionId = selectedTransactionApp.getTranId();
        // Show edit dialog and get the updated transaction
        TransactionApp updatedTransactionApp = showEditTransactionDialog(selectedTransactionApp);
        if (updatedTransactionApp == null) {
            // Edit was cancelled
            return;
        }

        // Update transaction in file
        updateTransactionInFile(selectedTransactionApp.getUserId(), selectedTransactionApp.getTranId(), updatedTransactionApp);

    }

    private TransactionApp showEditTransactionDialog(TransactionApp transactionApp) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit TransactionApp");

        TextField amountField = new TextField(String.valueOf(transactionApp.getAmount()));
        TextField monthField = new TextField(transactionApp.getMonth());
        TextField dayField = new TextField(transactionApp.getDate().split("-")[2]);
        TextField categoryField = new TextField(transactionApp.getCategory());
        TextField detailsField = new TextField(transactionApp.getDetails());

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("income", "expense", "savings");
        typeComboBox.setValue(transactionApp.getType());

        Button editButton = new Button("Edit");
        Button cancelButton = new Button("Cancel");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Amount:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Month (JAN-DEC):"), 0, 1);
        grid.add(monthField, 1, 1);
        grid.add(new Label("Day:"), 0, 2);
        grid.add(dayField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Details:"), 0, 4);
        grid.add(detailsField, 1, 4);
        grid.add(new Label("Type:"), 0, 5);
        grid.add(typeComboBox, 1, 5);
        grid.add(editButton, 0, 6);
        grid.add(cancelButton, 1, 6);

        Scene dialogScene = new Scene(grid, 400, 300);
        dialogStage.setScene(dialogScene);

        editButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String month = monthField.getText().toUpperCase();
                int day = Integer.parseInt(dayField.getText());
                String category = categoryField.getText();
                String details = detailsField.getText();
                String type = typeComboBox.getValue();

                if (!isValidDate(month, day)) {
                    showAlert("Invalid Date", "Please enter a valid date.");
                    return;
                }

                TransactionApp updatedTransactionApp = new TransactionApp(
                        transactionApp.getNumber(),
                        transactionApp.getUserId(),
                        month,
                        amount,
                        type,
                        category,
                        details,
                        "2024-" + getMonthNumber(month) + "-" + String.format("%02d", day),
                        transactionApp.getTranId()
                );

                dialogStage.close();
                handleEditTransaction(updatedTransactionApp);
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for amount and day.");
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        dialogStage.showAndWait();
        return null;
    }

    private void updateTransactionInFile(String userId, String transactionId, TransactionApp updatedTransactionApp) {
        String filePath = TRANS_FILE;
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();

        try ( Scanner scanner = new Scanner(file)) {
            // Read all lines from the file
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to read from file.");
            return;
        }

        // Iterate over the lines and update the matching transaction
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            if (parts.length == 8 && parts[0].equals(userId) && parts[7].equals(transactionId)) {
                // Found the matching transaction, update the line
                String updatedLine = String.join(",",
                        updatedTransactionApp.getUserId(),
                        updatedTransactionApp.getMonth(),
                        String.valueOf(updatedTransactionApp.getAmount()),
                        updatedTransactionApp.getType(),
                        updatedTransactionApp.getCategory(),
                        updatedTransactionApp.getDetails(),
                        updatedTransactionApp.getDate(),
                        updatedTransactionApp.getTranId());
                lines.set(i, updatedLine);
                break; // Stop searching
            }
        }
        // Write the updated lines back to the file
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to write to file.");
        }
    }

    private void handleEditTransaction(TransactionApp updatedTransactionApp) {
        // Update transaction in file
        updateTransactionInFile(updatedTransactionApp.getUserId(), updatedTransactionApp.getTranId(), updatedTransactionApp);

        // Update transaction in the UI
        for (int i = 0; i < transactionApps.size(); i++) {
            if (transactionApps.get(i).getTranId().equals(updatedTransactionApp.getTranId())) {
                transactionApps.set(i, updatedTransactionApp);
                break;
            }
        }
        // Refresh the table view
        transactionTable.getItems().setAll(transactionApps);
    }
    //-------------------------------------------------------------------------------------------------------//
}
