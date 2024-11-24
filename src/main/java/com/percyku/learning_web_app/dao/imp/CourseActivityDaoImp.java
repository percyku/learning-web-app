package com.percyku.learning_web_app.dao.imp;

import com.percyku.learning_web_app.dao.CourseActivityDao;
import com.percyku.learning_web_app.entity.Course;
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
    public List<Course> findCoursesByInstructorId(Long theId) {
        //create query
        TypedQuery<Course> query =entityManager.createQuery("from Course where user.id = :data",Course.class);
        query.setParameter("data",theId);

        //execute query
        List<Course> courses = query.getResultList();

        return courses;
    }

}
