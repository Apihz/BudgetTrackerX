package com.biscuittaiger.budgettrackerx.App;

import com.biscuittaiger.budgettrackerx.View.MainAppView;
import com.biscuittaiger.budgettrackerx.View.LoginView;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class LoginApp extends Application {

    private final Path USERS_FILE = Paths.get("src/main/java/com/biscuittaiger/budgettrackerx/Model/LoginData.txt");
    private LoginView loginView;
    private Stage primaryStage;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loginView = new LoginView(primaryStage, this);
        loginView.showLoginPage();
    }

    public void loginUser(String username, String password) {
        try {
            List<String> users = Files.readAllLines(USERS_FILE);
            for (String user : users) {
                String[] parts = user.split(",");
                if (parts.length == 3 && parts[1].equals(username) && parts[2].equals(password)) {
                    showMainApp(primaryStage, parts[0], username); // parts[0] is the user ID
                    return;
                }
            }
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect Username or Password.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not read users file.");
        }
    }

    public void registerUser(String username, String password) {
        try {
            List<String> users = Files.readAllLines(USERS_FILE);

            Optional<Integer> maxId = users.stream()
                    .map(user -> {
                        try {
                            return Integer.parseInt(user.split(",")[0]);
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    })
                    .max(Integer::compareTo);

            int userid = maxId.orElse(0) + 1;

            for (String user : users) {
                String[] parts = user.split(",");
                if (parts.length == 3 && parts[1].equals(username)) {
                    showAlert(Alert.AlertType.ERROR, "Username Exists", "Username already exists, please enter another key.");
                    loginView.clearRegistrationFields();
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE.toFile(), true))) {
                writer.write(userid + "," + username + "," + password);
                writer.newLine();
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
                DashboardApp dashboard = new DashboardApp(String.valueOf(userid),1);
                dashboard.initializeNewAccount();
                loginView.showLoginPage();
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not write to users file: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void showMainApp(Stage stage, String userid, String username) {
        MainAppView mainAppView = new MainAppView();
        try {
         //   mainAppView.start(stage, userid, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
