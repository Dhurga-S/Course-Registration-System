package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.MainApp;
import com.example.courseregistrationsystem.entity.Student;
import com.example.courseregistrationsystem.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;


public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final StudentService studentService = new StudentService();

    @FXML
    protected void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password are required.");
            return;
        }

        Student student = studentService.login(username, password);

        if (student != null) {
            statusLabel.setText("Login Successful!");
            try {
                // Pass the logged-in student to the dashboard
                MainApp.showDashboard(student);
            } catch (IOException e) {
                e.printStackTrace();
                statusLabel.setText("Failed to load dashboard.");
            }
        } else {
            statusLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    protected void handleSignUpButtonAction() {
        try {
            MainApp.showSignUp();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load sign-up page.");
        }
    }
}