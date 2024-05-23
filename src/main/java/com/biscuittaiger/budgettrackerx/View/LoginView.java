package com.biscuittaiger.budgettrackerx.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    private String username;
    private String password;
    private TextField usernameField;
    private TextField passwordField;
    private Stage stage;
    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {

        VBox vbox = new VBox();
        // Username label and text field
        Label usernameLabel = new Label("Username:");
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter your username");

        // Password label and password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter your password");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e->{
            username = usernameInput.getText();
            password = passwordInput.getText();

            if(username.equals("admin") && password.equals("admin")) {
                stage.setTitle("Budget Tracker");
                stage.setScene(getScene());
                stage.show();
            }
        });
        loginButton.getStyleClass().add("login-button");

        // Add components to the grid pane
        vbox.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton);

        Scene scene = new Scene(vbox,500,500);
        return scene;
    }
}
