package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.MainApp;
import com.example.courseregistrationsystem.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class SignUpController {

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;

    private final StudentService studentService = new StudentService();

    @FXML
    protected void handleSignUpButtonAction() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 1. Client-side validation: Check if passwords match
        if (!password.equals(confirmPassword)) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Passwords do not match.");
            return;
        }

        // 2. Call the service to create the student
        String result = studentService.createStudent(username, password, name);

        // 3. Display the result from the service
        if (result.startsWith("Sign-up successful")) {
            statusLabel.setStyle("-fx-text-fill: green;"); // Show success in green
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
        }
        statusLabel.setText(result);
    }

    @FXML
    protected void handleBackButtonAction() {
        try {
            // We will create this MainApp.showLogin() method in the next step
            MainApp.showLogin();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load login page.");
        }
    }
}