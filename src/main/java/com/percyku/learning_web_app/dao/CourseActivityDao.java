package com.percyku.learning_web_app.dao;

import com.percyku.learning_web_app.entity.Course;
import com.percyku.learning_web_app.entity.User;

import java.util.List;

public interface CourseActivityDao {

    Course addCourse(Course course);

    Course findCourseAndInstructorByCourseId(int id);

    User findCoursesByStudentId(Long theId);

    List<Course> findCoursesByInstructorId(Long theId);

    List<Course> findCoursesByCourseName(String name);
}
