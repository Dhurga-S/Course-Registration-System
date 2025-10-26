package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.entity.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class CourseService {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public List<Course> getAllCourses() {
        try (Session session = sessionFactory.openSession()) {
            // Fetch courses and their prerequisites
            return session.createQuery("FROM Course c LEFT JOIN FETCH c.prerequisites", Course.class).list();
        }
    }
}