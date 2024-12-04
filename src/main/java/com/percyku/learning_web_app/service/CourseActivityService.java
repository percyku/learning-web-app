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


    public List<PageCourse> getCourseByStudent(User user) {

        User tmpUser = courseActivityDao.findCoursesByStudentId(user.getId());


        if(tmpUser == null){
            return new ArrayList<PageCourse>();
        }

        List<PageCourse> res=new ArrayList<>();
        for(Course tmpCourse:tmpUser.getCourses_student()){

            List<Long>students= new ArrayList<>();
            for(User tmpStudent:tmpCourse.getUsers()){
                students.add(tmpStudent.getId());
            }

            PageCourse tmpPageCourse = new PageCourse(
                    tmpCourse.getId(),
                    tmpCourse.getTitle(),
                    tmpCourse.getDescription(),
                    tmpCourse.getPrice(),
                    tmpUser,
                    students,
                    true
            );

            res.add(tmpPageCourse);
        }
        return res;
    }

    public User getCoursesByStudentEmail(String email,int courseId){
        return courseActivityDao.findCourseByStudentEmail(email,courseId);
    }




    public List<PageCourse> getCourseByCourseName(String userName,String courseName) {
        List<Course> tmpCourses = courseActivityDao.findCoursesByCourseName(courseName);
        List<PageCourse> res=new ArrayList<>();
        log.debug(tmpCourses.toString());
        if(tmpCourses.size() >0){
            User tmpInstructor=tmpCourses.get(0).getUser();


            for(Course tmpCourse : tmpCourses){
                List<Long>students= new ArrayList<>();
                boolean registered=false;
                for(User tmpStudent:tmpCourse.getUsers()){
                    if(userName.equals(tmpStudent.getEmail())){
                        registered=true;
                    }
                    students.add(tmpStudent.getId());
                }

                PageCourse tmpPageCourse = new PageCourse(
                        tmpCourse.getId(),
                        tmpCourse.getTitle(),
                        tmpCourse.getDescription(),
                        tmpCourse.getPrice(),
                        tmpInstructor,
                        students,
                        registered
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


    @Transactional
    public Boolean enrollCourse(int courseId) {

        //reference findCourseAndStudentsByCourseId

        User student = (User)getCurrentUser();
        Course course =courseActivityDao.findCourseByCourseId(courseId);

        log.debug("This student id:"+student.getId()+",email:"+student.getEmail());

        if(course == null){
            return false;
        }
        log.debug("course id:"+course.getId());
        boolean checkExist=false;
        List<Long>students= new ArrayList<>();
        for(User tmpUser: course.getUsers()){
            if(tmpUser.getId() == student.getId()){
                checkExist =true;
            }
            students.add(tmpUser.getId());
            log.debug("Each student information:"+tmpUser.toString());
        }


        log.debug("check had been register or not :" +checkExist);
        if(!checkExist){
            course.addUsers(student);
            students.add(student.getId());
            List<User> tempUsers = course.getUsers();
            if(tempUsers != null){
                for(User user : tempUsers){
                    log.debug("Each enroll student :" +user.toString());
                }
            }
        }

        return true;
    }


}
