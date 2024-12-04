package com.percyku.learning_web_app.dao.imp;

import com.percyku.learning_web_app.dao.CourseActivityDao;
import com.percyku.learning_web_app.entity.Course;
import com.percyku.learning_web_app.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseActivityDaoImp implements CourseActivityDao {


    private EntityManager entityManager;

    @Autowired
    public CourseActivityDaoImp(EntityManager entityManager){
        this.entityManager=entityManager;
    }


    @Override
    public Course addCourse(Course course) {
        entityManager.merge(course);
        return course;
    }

    @Override
    public Course findCourseAndInstructorByCourseId(int id) {

        TypedQuery<Course> query =entityManager.createQuery("from Course c "
                +" JOIN FETCH c.user "
                +" where c.id = :data",Course.class);
        query.setParameter("data",id);


        Course course =null;
        //execute query
        try{
            course = query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }

        return course;

    }

    @Override
    public Course findCourseByCourseId(int theId) {
        return entityManager.find(Course.class,theId);
    }

    @Override
    public List<Course> findCoursesByInstructorId(Long theId) {
        //create query
        TypedQuery<Course> query =entityManager.createQuery("from Course where user.id = :data",Course.class);
        query.setParameter("data",theId);

        //execute query
        List<Course> courses = query.getResultList();

        return courses;
    }

    @Override
    public User findCoursesByStudentId(Long theId) {
        //create query
        TypedQuery<User> query = entityManager.createQuery(
                "select c from User c "
                        +"JOIN FETCH c.courses_student "
                        +"where c.id = :data1" ,User.class);
        query.setParameter("data1",theId);
        //execute query
        User student= null;

        try{
            student = query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }

        return student;
    }

    @Override
    public  User findCourseByStudentEmail(String email,int courseId) {
        TypedQuery<User> query = entityManager.createQuery(
                "select c from User c "
                        +"JOIN FETCH c.courses_student s "
                        +"where c.email = :data1 and s.id = :data2 ",User.class);
        query.setParameter("data1",email);
        query.setParameter("data2",courseId);

        //execute query
        User student= null;

        try{
            student = query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }

        return student;
    }


    @Override
    public List<Course> findCoursesByCourseName(String name) {
        //create query
        TypedQuery<Course> query =entityManager.createQuery("from Course c "
                +" JOIN FETCH c.user "
                +" where c.title like '%"+name+"%'",Course.class);
//        query.setParameter("data",theId);

        //execute query
        List<Course> courses = query.getResultList();

        return courses;
    }

}
