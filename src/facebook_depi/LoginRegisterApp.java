package facebook_depi;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginRegisterApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);
    }

    // Show Login Screen
    private void showLoginScreen(Stage primaryStage) {
        // Create UI Elements
        Label loginTitle = new Label("Login");
        loginTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields!");
            } else {
                // Add login logic here
                showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome, " + username + "!");
            }
        });

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> showRegisterWindow());

        VBox loginLayout = new VBox(10, loginTitle, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, registerButton);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);

        Scene loginScene = new Scene(loginLayout, 300, 300);
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    // Show Register Window
    private void showRegisterWindow() {
        // Create a new stage for registration
        Stage registerStage = new Stage();

        // Create UI Elements
        Label registerTitle = new Label("Register");
        registerTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields!");
            } else {
                // Add registration logic here
                showAlert(Alert.AlertType.INFORMATION, "Registration Success", "User registered successfully!");
                registerStage.close(); // Close the registration window
            }
        });

        VBox registerLayout = new VBox(10, registerTitle, usernameLabel, usernameField, emailLabel, emailField, passwordLabel, passwordField, registerButton);
        registerLayout.setPadding(new Insets(20));
        registerLayout.setAlignment(Pos.CENTER);

        Scene registerScene = new Scene(registerLayout, 300, 350);
        registerStage.setTitle("Register");
        registerStage.setScene(registerScene);
        registerStage.show();
    }

    // Show an alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}