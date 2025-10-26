package com.example.courseregistrationsystem;

import com.example.courseregistrationsystem.entity.Course;
import com.example.courseregistrationsystem.entity.Student;
import com.example.courseregistrationsystem.service.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DataLoader {

    public static void loadData() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Create Student
            Student student1 = new Student();
            student1.setUsername("student");
            student1.setPassword("pass");
            student1.setName("Alice");
            session.persist(student1);

            // Create Courses
            Course cs101 = new Course();
            cs101.setCourseCode("CS101");
            cs101.setTitle("Intro to Programming");
            cs101.setCapacity(50);
            cs101.setCurrentEnrollment(0);
            session.persist(cs101);

            Course cs102 = new Course();
            cs102.setCourseCode("CS102");
            cs102.setTitle("Data Structures");
            cs102.setCapacity(40);
            cs102.setCurrentEnrollment(0);
            // CS102 requires CS101
            cs102.getPrerequisites().add(cs101);
            session.persist(cs102);

            Course math101 = new Course();
            math101.setCourseCode("MATH101");
            math101.setTitle("Calculus I");
            math101.setCapacity(2); // Small capacity for testing
            math101.setCurrentEnrollment(0);
            session.persist(math101);

            tx.commit();
            System.out.println("Sample data loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load sample data.");
        }
    }
}