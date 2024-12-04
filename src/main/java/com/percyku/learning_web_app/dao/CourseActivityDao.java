package com.percyku.learning_web_app.dao;

import com.percyku.learning_web_app.entity.Course;
import com.percyku.learning_web_app.entity.User;

import java.util.List;

public interface CourseActivityDao {

    Course addCourse(Course course);

    Course findCourseAndInstructorByCourseId(int id);

    Course findCourseByCourseId(int theId);


    User findCoursesByStudentId(Long theId);

    User findCourseByStudentEmail(String email,int courseId);

    List<Course> findCoursesByInstructorId(Long theId);

    List<Course> findCoursesByCourseName(String name);
}
