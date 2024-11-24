package com.percyku.learning_web_app.service;

import com.percyku.learning_web_app.dao.CourseActivityDao;
import com.percyku.learning_web_app.dao.UserDao;
import com.percyku.learning_web_app.entity.Course;
import com.percyku.learning_web_app.entity.PageCourse;
import com.percyku.learning_web_app.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseActivityService {
    private final static Logger log = LoggerFactory.getLogger(CourseActivityService.class);

    private UserDao userDao;
    CourseActivityDao courseActivityDao;

    @Autowired
    public CourseActivityService(UserDao userDao,CourseActivityDao courseActivityDao) {
        this.courseActivityDao = courseActivityDao;
        this.userDao=userDao;
    }


    @Transactional
    public Course createCourse(Course course){
        return courseActivityDao.addCourse(course);
    }

    public Course findCourseById(int id){

        Course course = courseActivityDao.findCourseAndInstructorByCourseId(id);
        if(course== null ) return null;

        return course;
    }

    public List<PageCourse> getCourseByInstructor(User user) {

        List<Course> tmpCourses = courseActivityDao.findCoursesByInstructorId(user.getId());
        List<PageCourse> res=new ArrayList<>();
        log.debug(tmpCourses.toString());
        if(tmpCourses.size() >0){
            User tmpInstructor=tmpCourses.get(0).getUser();


            for(Course tmpCourse : tmpCourses){
                List<Long>students= new ArrayList<>();
                boolean registered = false;
                for(User tmpStudent:tmpCourse.getUsers()){
                    students.add(tmpStudent.getId());
                    registered=true;
                }

                PageCourse tmpPageCourse = new PageCourse(
                        tmpCourse.getId(),
                        tmpCourse.getTitle(),
                        tmpCourse.getDescription(),
                        tmpCourse.getPrice(),
                        user,
                        students
                );
                res.add(tmpPageCourse);
            }

        }

        return res;
    }


    public Object getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();
            User user= userDao.findUserByEmail(authentication.getName());

            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))){
                return user;
            }else{
                return user;
            }
        }

        return null;
    }

}
