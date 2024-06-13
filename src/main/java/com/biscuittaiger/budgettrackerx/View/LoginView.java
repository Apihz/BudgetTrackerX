package com.biscuittaiger.budgettrackerx.View;

import com.biscuittaiger.budgettrackerx.App.LoginApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginView {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginBtn;
    private Button clearBtn;
    private Button registerBtn;
    private Button regBackBtn;
    private TextField regUsernameField;
    private PasswordField regPasswordField;
    private Button regSubmitBtn;
    private Stage primaryStage;
    private LoginApp loginApp;
    private HBox hbox;

    public LoginView(Stage primaryStage, LoginApp loginApp) {
        this.primaryStage = primaryStage;
        this.loginApp = loginApp;
    }

    public void showLoginPage() {
        Label usernamelbl = new Label("USERNAME");
        Label passwordlbl = new Label("PASSWORD");
        Label registerlbl = new Label("Register Account for first time user.");

        usernamelbl.setStyle("-fx-font: normal bold 15px 'arial'; -fx-text-fill: turquoise;");
        passwordlbl.setStyle("-fx-font: normal bold 15px 'arial'; -fx-text-fill: turquoise;");
        registerlbl.setStyle("-fx-font: normal bold 13px 'arial'; -fx-text-fill: turquoise;");

        usernameField = new TextField();
        passwordField = new PasswordField();

        loginBtn = new Button("Login");
        clearBtn = new Button("Clear");
        registerBtn = new Button("Register");

        loginBtn.setStyle("-fx-background-color: turquoise; -fx-text-fill: black;");
        clearBtn.setStyle("-fx-background-color: turquoise; -fx-text-fill: black;");
        registerBtn.setStyle("-fx-background-color: turquoise; -fx-text-fill: black;");

        hbox = new HBox(20);
        hbox.getChildren().addAll(clearBtn, loginBtn);

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(400, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(usernamelbl, 0, 0);
        gridpane.add(usernameField, 1, 0);
        gridpane.add(passwordlbl, 0, 1);
        gridpane.add(passwordField, 1, 1);
        gridpane.add(hbox, 1, 2);
        gridpane.add(registerlbl, 0, 3);
        gridpane.add(registerBtn, 0, 4);

        gridpane.setStyle("-fx-background-color: darkslategrey;");

        Scene scene = new Scene(gridpane, 500, 400);

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();

        loginBtn.setOnAction(e -> {
            if (validateInput(usernameField, passwordField)) {
                loginApp.loginUser(usernameField.getText(), passwordField.getText());
            }
        });
        clearBtn.setOnAction(e -> {
            usernameField.clear();
            passwordField.clear();
        });

        registerBtn.setOnAction(e -> showRegistrationPage());
    }

    public void showRegistrationPage() {
        Label usernamelbl = new Label("USERNAME");
        Label passwordlbl = new Label("PASSWORD");

        usernamelbl.setStyle("-fx-font: normal bold 15px 'arial'; -fx-text-fill: turquoise;");
        passwordlbl.setStyle("-fx-font: normal bold 15px 'arial'; -fx-text-fill: turquoise;");

        regUsernameField = new TextField();
        regPasswordField = new PasswordField();

        regSubmitBtn = new Button("Submit");
        regSubmitBtn.setStyle("-fx-background-color: turquoise; -fx-text-fill: black;");
        regBackBtn = new Button("Back");
        regBackBtn.setStyle("-fx-background-color: turquoise; -fx-text-fill: black;");

        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(regBackBtn, regSubmitBtn);

        GridPane gridpane = new GridPane();
        gridpane.setMinSize(400, 400);
        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        gridpane.setAlignment(Pos.CENTER);

        gridpane.add(usernamelbl, 0, 0);
        gridpane.add(regUsernameField, 1, 0);
        gridpane.add(passwordlbl, 0, 1);
        gridpane.add(regPasswordField, 1, 1);
        gridpane.add(hbox, 1, 2);
        gridpane.setStyle("-fx-background-color: darkslategrey;");

        regSubmitBtn.setOnAction(e -> {
            if (validateInput(regUsernameField, regPasswordField)) {
                loginApp.registerUser(regUsernameField.getText(), regPasswordField.getText());
            }
        });
        regBackBtn.setOnAction(e->{
            showLoginPage();
        });

        Scene scene = new Scene(gridpane, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Registration Page");
        primaryStage.show();
    }

    private boolean validateInput(TextField usernameField, PasswordField passwordField) {
        boolean isValid = true;
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

    public void clearRegistrationFields() {
        regUsernameField.clear();
        regPasswordField.clear();
    }
}
