package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.entity.Student;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;


public class StudentService {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Student login(String username, String password) {
        try (Session session = sessionFactory.openSession()) {
            // We reload the student and fetch their registrations
            Query<Student> query = session.createQuery(
                    "FROM Student s LEFT JOIN FETCH s.registrations WHERE s.username = :username AND s.password = :password",
                    Student.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.uniqueResultOptional().orElse(null);
        }
    }

    public String createStudent(String username, String password, String name) {
        // 1. Basic validation
        if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
            return "All fields are required.";
        }

        try (Session session = sessionFactory.openSession()) {
            // 2. Check if username already exists
            Query<Student> query = session.createQuery("FROM Student WHERE username = :username", Student.class);
            query.setParameter("username", username);
            if (query.uniqueResultOptional().isPresent()) {
                return "Username already exists. Please choose another.";
            }

            // 3. All checks passed, create the new student
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Student newStudent = new Student();
                newStudent.setUsername(username);
                newStudent.setPassword(password); // In a real app, you would hash this!
                newStudent.setName(name);

                session.persist(newStudent);
                tx.commit();

                return "Sign-up successful! You can now log in.";

            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
                return "Sign-up failed due to a system error.";
            }
        }
    }
}