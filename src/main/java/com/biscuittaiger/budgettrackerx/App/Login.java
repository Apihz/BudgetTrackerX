package com.biscuittaiger.budgettrackerx.App;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modern Login");

        // Create a grid pane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Username label and text field
        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter your username");
        GridPane.setConstraints(usernameInput, 1, 0);

        // Password label and password field
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter your password");
        GridPane.setConstraints(passwordInput, 1, 1);

        // Login button
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        GridPane.setConstraints(loginButton, 1, 2);

        // Add components to the grid pane
        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton);

        // Set up scene and add CSS stylesheet
        Scene scene = new Scene(grid, 300, 200);
        String cssFile = getClass().getResource("styles.css") != null ? getClass().getResource("styles.css").toExternalForm() : null;
        if (cssFile != null) {
            scene.getStylesheets().add(cssFile);
        }
        primaryStage.setScene(scene);
        primaryStage.show();

        // Event handler for the login button
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            // Hardcoded credentials (replace with your actual authentication logic)
            if (username.equals("admin") && password.equals("password")) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username);
                // Add code to navigate to the dashboard or next screen
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


