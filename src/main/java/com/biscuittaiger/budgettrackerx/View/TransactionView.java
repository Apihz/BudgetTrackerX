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
import java.time.LocalDate;
import java.util.*;

public class TransactionView {

    private final TableView<TransactionApp> transactionTable;
    private Text incomeText;
    private Text expenseText;
    private Text balanceText;
    private Text savingsText;
    private ComboBox<String> monthComboBox;
    private final String userId;
    private final ArrayList<TransactionApp> transactionApps;
    private final static String TRANS_FILE = "src/main/java/com/biscuittaiger/budgettrackerx/Model/TransactionData.txt";

    public TransactionView(String userId) {
        this.userId = userId;
        this.transactionApps = new ArrayList<>();
        transactionTable = new TableView<>();
        setupTransactionView();
    }

    public VBox getTransactionView() {
        String css = this.getClass().getResource("/com/biscuittaiger/budgettrackerx/Transaction.css").toExternalForm();

        VBox transactionView = new VBox(10);
        transactionView.setPadding(new Insets(10));
        transactionView.getStylesheets().add(css);

        monthComboBox = new ComboBox<>();
        monthComboBox.setId("monthComboBox");
        List<String> months = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        monthComboBox.getItems().addAll(months);
        monthComboBox.setValue("1");

        monthComboBox.setOnAction(event -> {
            fetchAndDisplayTransactions(userId, monthComboBox.getValue());
            calculateAndUpdateBudgetInfo(userId,Integer.parseInt(monthComboBox.getValue()));
        });

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
        addButton.setId("addButton");
        addButton.setOnAction(e -> showAddTransactionDialog());

        transactionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editSelectedTransaction();
            }
        });

        Label transactionLabel = new Label("Transaction");
        transactionLabel.setId("transactionLabel");
        Label transactionHeader = new Label("Add,Edit,Delete your transaction");
        transactionHeader.setId("transactionHeader");

        Button editButton = new Button("Edit");
        editButton.setId("editButton");
        editButton.setOnAction(e -> editSelectedTransaction());

        Button deleteButton = new Button("Delete");
        deleteButton.setId("deleteButton");
        deleteButton.setOnAction(event -> deleteSelectedTransaction());

        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setId("buttonBox");
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        transactionView.getChildren().addAll(transactionLabel,transactionHeader,monthComboBox, infoBox, buttonBox, transactionTable);
        fetchAndDisplayTransactions(userId, monthComboBox.getValue());
        return transactionView;
    }

    private void setupTransactionView() {

        TableColumn<TransactionApp, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<TransactionApp, String> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));

        TableColumn<TransactionApp, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<TransactionApp, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<TransactionApp, String> tranIdCol = new TableColumn<>("Transaction ID");
        tranIdCol.setCellValueFactory(new PropertyValueFactory<>("tranId"));

        transactionTable.getColumns().addAll( categoryCol, detailsCol, amountCol, dateCol, tranIdCol);

        transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Enable row selection

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
        infoBox.setId("informationBox");
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

    private void fetchAndDisplayTransactions(String userId, String month) {
        try {
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


    private void showAddTransactionDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add TransactionApp");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        TextField amountField = new TextField();
        ComboBox<String> typeComboBox = new ComboBox<>(FXCollections.observableArrayList("income", "expense", "savings"));
        ComboBox<String> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList("Shopping", "Education", "Electronics", "Entertainment", "Food and Beverages", "Health and Beauty", "Medical", "Transportation", "Other Expenses"));
        TextField categoryDisplayField = new TextField();
        categoryDisplayField.setEditable(false);
        categoryDisplayField.setVisible(false); // Initially hidden
        TextField detailsField = new TextField();
        DatePicker datePicker = new DatePicker(LocalDate.now());

        grid.add(new Label("Amount:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeComboBox, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryComboBox, 1, 2);
        grid.add(categoryDisplayField, 1, 2); // Add category display field to the same position
        grid.add(new Label("Details:"), 0, 3);
        grid.add(detailsField, 1, 3);
        grid.add(new Label("Date:"), 0, 4);
        grid.add(datePicker, 1, 4);

        typeComboBox.getSelectionModel().selectFirst(); // Set the default selection to the first item
        String selectedType = typeComboBox.getValue();
        if (selectedType != null && selectedType.equals("expense")) {
            categoryComboBox.setVisible(true);
            categoryDisplayField.setVisible(false);
        } else {
            categoryComboBox.setVisible(false);
            categoryDisplayField.setVisible(true);
            categoryDisplayField.setText(selectedType);
        }

        typeComboBox.setOnAction(e -> {
            String selectedType2 = typeComboBox.getValue();
            if (selectedType2 != null && selectedType2.equals("expense")) {
                categoryComboBox.setVisible(true);
                categoryDisplayField.setVisible(false);
            } else {
                categoryComboBox.setVisible(false);
                categoryDisplayField.setVisible(true);
                categoryDisplayField.setText(selectedType2);
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                String amount = amountField.getText();
                String type = typeComboBox.getValue();
                String category = type.equals("expense") ? categoryComboBox.getValue() : type;
                String details = detailsField.getText();
                LocalDate date = datePicker.getValue();

                if (amount.isEmpty() || type == null || (type.equals("expense") && (category == null || category.isEmpty())) || details.isEmpty() || date == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields");
                    return;
                }

                String month = monthComboBox.getValue();

                writeTransactionToFile(userId, month, amount, type, category, details, date.toString());
                fetchAndDisplayTransactions(userId, month);
                calculateAndUpdateBudgetInfo(userId, Integer.parseInt(month));
                dialog.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBox, 1, 5);

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.showAndWait();
    }




    private void editSelectedTransaction() {
        TransactionApp selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No transaction selected");
            return;
        }

        showEditTransactionDialog(selectedTransaction);
    }

    private void showEditTransactionDialog(TransactionApp transactionApp) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit TransactionApp");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        TextField amountField = new TextField(String.valueOf(transactionApp.getAmount()));
        ComboBox<String> typeComboBox = new ComboBox<>(FXCollections.observableArrayList("income", "expense", "savings"));
        typeComboBox.setValue(transactionApp.getType());
        ComboBox<String> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList("Shopping", "Education", "Electronics", "Entertainment", "Food and Beverages", "Health and Beauty", "Medical", "Shopping", "Transportation", "Other Expenses"));
        categoryComboBox.setValue(transactionApp.getCategory());
        TextField detailsField = new TextField(transactionApp.getDetails());
        DatePicker datePicker = new DatePicker(LocalDate.parse(transactionApp.getDate()));

        grid.add(new Label("Amount:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeComboBox, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryComboBox, 1, 2);
        grid.add(new Label("Details:"), 0, 3);
        grid.add(detailsField, 1, 3);
        grid.add(new Label("Date:"), 0, 4);
        grid.add(datePicker, 1, 4);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String type = typeComboBox.getValue();
                String category = categoryComboBox.getValue();
                String details = detailsField.getText();
                LocalDate date = datePicker.getValue();

                transactionApp.setAmount(amount);
                transactionApp.setType(type);
                transactionApp.setCategory(category);
                transactionApp.setDetails(details);
                transactionApp.setDate(date.toString());

                updateTransactionFile(transactionApp);
                fetchAndDisplayTransactions(userId, monthComboBox.getValue());

                dialog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> dialog.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBox, 1, 5);

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    private void restrictDatePickerToMonth(DatePicker datePicker, int month) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getMonthValue() != month);
            }
        });
    }

    private void deleteSelectedTransaction() {
        TransactionApp selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No transaction selected");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete this transaction?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                deleteTransactionFromFile(selectedTransaction);
                fetchAndDisplayTransactions(userId, monthComboBox.getValue());
                calculateAndUpdateBudgetInfo(userId,Integer.parseInt(monthComboBox.getValue()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeTransactionToFile(String userId, String month, String amount, String type, String category, String details, String date) throws IOException {
        List<String> existingIds = readExistingTransactionIds();

        String transactionId;
        do {
            int randnum = (int) (Math.random() * 900) + 100;
            transactionId = Integer.toString(randnum);
        } while (existingIds.contains(transactionId));

        String transaction = userId + "," + month + "," + amount + "," + type + "," + category + "," + details + "," + date + "," + transactionId + "\n";
        Files.write(Paths.get(TRANS_FILE), transaction.getBytes(), StandardOpenOption.APPEND);
    }

    private List<String> readExistingTransactionIds() throws IOException {
        List<String> existingIds = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(TRANS_FILE));

        for (String line : lines) {
            String[] delimiter = line.split(",");
            if (delimiter.length > 7) {
                existingIds.add(delimiter[7]);
            }
        }

        return existingIds;
    }

    private void updateTransactionFile(TransactionApp transactionApp) throws IOException {
        Path path = Paths.get(TRANS_FILE);
        List<String> lines = Files.readAllLines(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String line : lines) {
                String[] delimiter = line.split(",");
                if (delimiter[7].equals(transactionApp.getTranId())) {
                    String updatedTransaction = transactionApp.getUserId() + "," + transactionApp.getMonth() + "," + transactionApp.getAmount() + "," + transactionApp.getType() + "," + transactionApp.getCategory() + "," + transactionApp.getDetails() + "," + transactionApp.getDate() + "," + transactionApp.getTranId();
                    writer.write(updatedTransaction);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        }
    }

    private void deleteTransactionFromFile(TransactionApp transactionApp) throws IOException {
        Path path = Paths.get(TRANS_FILE);
        List<String> lines = Files.readAllLines(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String line : lines) {
                String[] delimiter = line.split(",");
                if (!delimiter[7].equals(transactionApp.getTranId())) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    public static void updateDashboardBudgetInfo(String userId, int month, double updatedIncome, double updatedExpense, double updatedBalance, double updatedSavings) {
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
                        parts[2] = String.valueOf(updatedBalance);
                        parts[3] = String.valueOf(updatedIncome);
                        parts[4] = String.valueOf(updatedExpense);
                        parts[6] = String.valueOf(updatedSavings);
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

                stringBuilder.append(newLine.toString()).append("\n");
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

    public static void calculateAndUpdateBudgetInfo(String userId, int month) {
        try {
            ArrayList<TransactionApp> transactions = readTransactionInfoFile(userId, String.valueOf(month));

            double totalIncome = 0;
            double totalExpense = 0;
            double totalBalance = 0;
            double totalSavings = 0;

            for (TransactionApp transaction : transactions) {
                double amount = transaction.getAmount();
                String type = transaction.getType();

                if (type.equals("income")) {
                    totalIncome += amount;
                } else if (type.equals("expense")) {
                    totalExpense += amount;
                } else if (type.equals("savings")) {
                    totalSavings += amount;
                }

                totalBalance = totalIncome - totalExpense - totalSavings;
            }

            updateDashboardBudgetInfo(userId, month, totalIncome, totalExpense, totalBalance, totalSavings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
