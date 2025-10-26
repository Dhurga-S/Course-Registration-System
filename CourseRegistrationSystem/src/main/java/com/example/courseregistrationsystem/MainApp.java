package com.example.courseregistrationsystem;

import com.example.courseregistrationsystem.controller.DashboardController;
import com.example.courseregistrationsystem.entity.Student;
import com.example.courseregistrationsystem.service.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Load sample data first
        DataLoader.loadData();


        showLogin();
    }

    // This method shows the login screen
    public static void showLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/Login.fxml"));
        // We make the scene a bit taller to fit the new button
        Scene scene = new Scene(fxmlLoader.load(), 300, 275);
        primaryStage.setTitle("Course Registration Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // This method shows the new sign-up screen
    public static void showSignUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/SignUp.fxml"));
        Scene scene = new Scene(loader.load(), 320, 300);
        primaryStage.setTitle("Create Account");
        primaryStage.setScene(scene);
    }

    // Scene switching method
    public static void showDashboard(Student student) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Dashboard.fxml"));
        Parent root = loader.load();

        // Pass the student object to the dashboard controller
        DashboardController controller = loader.getController();
        controller.setStudent(student);

        Scene scene = new Scene(root, 700, 450);
        primaryStage.setTitle("Student Dashboard");
        primaryStage.setScene(scene);
    }

    @Override
    public void stop() {
        // Shut down the Hibernate SessionFactory when the app closes
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch();
    }
}