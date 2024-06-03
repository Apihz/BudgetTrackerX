package com.biscuittaiger.budgettrackerx.App;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class NewLogin extends Application {
    private final String RESOURCE_USERS_FILE = "/com/biscuittaiger/budgettrackerx/LoginData.txt";
    private final Path USERS_FILE_PATH = Paths.get(System.getProperty("user.home"), "budgettrackerx", "LoginData.txt");
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("User Login and Registration");

        // Ensure the users file exists and is accessible
        createUsersFileIfNotExists();

        showLoginPage();
    }

    private void createUsersFileIfNotExists() {
        try {
            if (Files.notExists(USERS_FILE_PATH)) {
                Files.createDirectories(USERS_FILE_PATH.getParent());
                try (InputStream resourceStream = getClass().getResourceAsStream(RESOURCE_USERS_FILE)) {
                    if (resourceStream == null) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Resource file not found.");
                        return;
                    }
                    Files.copy(resourceStream, USERS_FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Resource file copied to: " + USERS_FILE_PATH.toAbsolutePath());
                }
            } else {
                System.out.println("File already exists at: " + USERS_FILE_PATH.toAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not create or access users file.");
        }
    }

    private void showLoginPage() {
        Label useridlbl = new Label("USER ID");
        Label usernamelbl = new Label("USERNAME");
        Label passwordlbl = new Label("PASSWORD");
        Label registerlbl = new Label("Register Account for first time user.");

        useridlbl.setStyle("-fx-font: normal bold 15px 'arial' ");
        usernamelbl.setStyle("-fx-font: normal bold 15px 'arial' ");
        passwordlbl.setStyle("-fx-font: normal bold 15px 'arial' ");
        registerlbl.setStyle("-fx-font: normal 13px 'arial' ");

        TextField useridField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button loginbtn = new Button("Login");
        Button clearbtn = new Button("Clear");
        Button registerbtn = new Button("Register");

        loginbtn.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        clearbtn.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        registerbtn.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        // Login button function
        loginbtn.setOnAction(e -> {
            if (validateInput(useridField, usernameField, passwordField)) {
                loginUser(useridField.getText(), usernameField.getText(), passwordField.getText());
            }
        });

        // Clear button function
        clearbtn.setOnAction(e -> {
            useridField.clear();
            usernameField.clear();
            passwordField.clear();
        });

        // Register button function
        registerbtn.setOnAction(e -> showRegistrationPage());

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(400, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(useridlbl, 0, 0);
        gridpane.add(useridField, 1, 0);
        gridpane.add(usernamelbl, 0, 1);
        gridpane.add(usernameField, 1, 1);
        gridpane.add(passwordlbl, 0, 2);
        gridpane.add(passwordField, 1, 2);
        gridpane.add(loginbtn, 0, 3);
        gridpane.add(clearbtn, 1, 3);
        gridpane.add(registerlbl, 0, 4);
        gridpane.add(registerbtn, 0, 5);

        gridpane.setStyle("-fx-background-color: lightcyan;");

        Scene scene = new Scene(gridpane, 500, 400);

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showRegistrationPage() {
        Label useridlbl = new Label("USER ID");
        Label usernamelbl = new Label("USERNAME");
        Label passwordlbl = new Label("PASSWORD");

        useridlbl.setStyle("-fx-font: normal bold 15px 'arial' ");
        usernamelbl.setStyle("-fx-font: normal bold 15px 'arial' ");
        passwordlbl.setStyle("-fx-font: normal bold 15px 'arial' ");

        TextField useridField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button submitbtn = new Button("Submit");
        submitbtn.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(400, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(useridlbl, 0, 0);
        gridpane.add(useridField, 1, 0);
        gridpane.add(usernamelbl, 0, 1);
        gridpane.add(usernameField, 1, 1);
        gridpane.add(passwordlbl, 0, 2);
        gridpane.add(passwordField, 1, 2);
        gridpane.add(submitbtn, 1, 3);

        gridpane.setStyle("-fx-background-color: lightcyan;");

        submitbtn.setOnAction(e -> {
            if (validateInput(useridField, usernameField, passwordField)) {
                registerUser(useridField.getText(), usernameField.getText(), passwordField.getText());
                showLoginPage();
            }
        });

        Scene scene = new Scene(gridpane, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Registration Page");
        primaryStage.show();
    }

    private boolean validateInput(TextField useridField, TextField usernameField, PasswordField passwordField) {
        boolean isValid = true;
        if (useridField.getText().isEmpty()) {
            useridField.setPromptText("Do not leave empty!");
            isValid = false;
        }
        if (usernameField.getText().isEmpty()) {
            usernameField.setPromptText("Do not leave empty!");
            isValid = false;
        }
        if (passwordField.getText().isEmpty()) {
            passwordField.setPromptText("Do not leave empty!");
            isValid = false;
        }
        return isValid;
    }

    private void loginUser(String userid, String username, String password) {
        try {
            List<String> users = Files.readAllLines(USERS_FILE_PATH);
            System.out.println("Users read from file: " + USERS_FILE_PATH.toAbsolutePath());
            for (String user : users) {
                String[] parts = user.split(",");
                if (parts.length == 3 && parts[0].equals(userid) && parts[1].equals(username) && parts[2].equals(password)) {
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
                    return;
                }
            }
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect User ID, Username, or Password.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not read users file.");
        }
    }

    private void registerUser(String userid, String username, String password) {
        System.out.println("Writing to file: " + USERS_FILE_PATH.toAbsolutePath());
        try (BufferedWriter writer = Files.newBufferedWriter(USERS_FILE_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(userid + "," + username + "," + password);
            writer.newLine();
            writer.flush();  // Ensure data is flushed to the file
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not write to users file.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
