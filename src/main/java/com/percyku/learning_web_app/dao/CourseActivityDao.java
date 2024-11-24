package com.percyku.learning_web_app.dao;

import com.percyku.learning_web_app.entity.Course;

import java.util.List;

public interface CourseActivityDao {

    Course addCourse(Course course);

    Course findCourseAndInstructorByCourseId(int id);

    List<Course> findCoursesByInstructorId(Long theId);
}
