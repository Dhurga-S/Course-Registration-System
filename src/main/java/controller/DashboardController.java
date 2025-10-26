package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.entity.Course;
import com.example.courseregistrationsystem.entity.Registration;
import com.example.courseregistrationsystem.entity.Student;
import com.example.courseregistrationsystem.service.CourseService;
import com.example.courseregistrationsystem.service.RegistrationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.List;

public class DashboardController {

    private Student currentStudent;
    private final CourseService courseService = new CourseService();
    private final RegistrationService registrationService = new RegistrationService();

    @FXML private Text welcomeText;
    @FXML private TableView<Course> availableCoursesTable;
    @FXML private TableColumn<Course, String> colCourseCode;
    @FXML private TableColumn<Course, String> colCourseTitle;
    @FXML private TableColumn<Course, Integer> colCapacity;
    @FXML private TableColumn<Course, Integer> colEnrolled;
    @FXML private ListView<String> myCoursesList;
    @FXML private Label registrationStatusLabel;

    // This method is called by MainApp to pass the logged-in student
    public void setStudent(Student student) {
        this.currentStudent = student;
        welcomeText.setText("Welcome, " + student.getName() + "!");

        // Initialize the view
        loadAvailableCourses();
        loadMyCourses();
    }

    @FXML
    private void initialize() {
        // Set up the table columns to bind to Course properties
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colCourseTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colEnrolled.setCellValueFactory(new PropertyValueFactory<>("currentEnrollment"));
    }

    private void loadAvailableCourses() {
        List<Course> courses = courseService.getAllCourses();
        availableCoursesTable.setItems(FXCollections.observableArrayList(courses));
    }

    private void loadMyCourses() {
        myCoursesList.getItems().clear();
        for (Registration reg : currentStudent.getRegistrations()) {
            myCoursesList.getItems().add(
                    reg.getCourse().getCourseCode() + ": " + reg.getCourse().getTitle()
            );
        }
    }

    @FXML
    protected void handleRegisterButtonAction() {
        Course selectedCourse = availableCoursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            registrationStatusLabel.setText("Please select a course to register.");
            return;
        }

        String result = registrationService.registerStudentForCourse(currentStudent, selectedCourse);
        registrationStatusLabel.setText(result);

        // Refresh both lists to show updated data
        loadAvailableCourses();
        loadMyCourses();
    }
}