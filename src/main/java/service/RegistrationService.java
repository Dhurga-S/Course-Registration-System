package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.entity.Course;
import com.example.courseregistrationsystem.entity.Registration;
import com.example.courseregistrationsystem.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class RegistrationService {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public String registerStudentForCourse(Student student, Course course) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                // Reload entities in the current session
                Student sessionStudent = session.get(Student.class, student.getId());
                Course sessionCourse = session.get(Course.class, course.getId());

                // 1. Check Capacity
                if (sessionCourse.getCurrentEnrollment() >= sessionCourse.getCapacity()) {
                    return "Registration failed: Course is full.";
                }

                // 2. Check if already registered
                boolean isAlreadyRegistered = sessionStudent.getRegistrations().stream()
                        .anyMatch(reg -> reg.getCourse().getId() == sessionCourse.getId());
                if (isAlreadyRegistered) {
                    return "Registration failed: Already registered for this course.";
                }

                // 3. Check Prerequisites
                Set<Course> prerequisites = sessionCourse.getPrerequisites();
                if (!prerequisites.isEmpty()) {
                    Set<Integer> registeredCourseIds = sessionStudent.getRegistrations().stream()
                            .map(reg -> reg.getCourse().getId())
                            .collect(Collectors.toSet());

                    for (Course prereq : prerequisites) {
                        if (!registeredCourseIds.contains(prereq.getId())) {
                            return "Registration failed: Missing prerequisite: " + prereq.getCourseCode();
                        }
                    }
                }

                // 4. All checks passed: Create registration
                Registration newRegistration = new Registration();
                newRegistration.setStudent(sessionStudent);
                newRegistration.setCourse(sessionCourse);
                newRegistration.setRegistrationTime(LocalDateTime.now());

                sessionCourse.setCurrentEnrollment(sessionCourse.getCurrentEnrollment() + 1);

                session.persist(newRegistration);
                session.merge(sessionCourse); // Update the course enrollment

                tx.commit();

                // Manually add to the student object we are using
                student.getRegistrations().add(newRegistration);

                return "Successfully registered for " + sessionCourse.getCourseCode() + "!";

            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
                return "Registration failed due to a system error.";
            }
        }
    }
}