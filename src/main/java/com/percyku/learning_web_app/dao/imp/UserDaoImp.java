package com.percyku.learning_web_app.dao.imp;

import com.percyku.learning_web_app.dao.UserDao;
import com.percyku.learning_web_app.entity.Role;
import com.percyku.learning_web_app.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    private EntityManager entityManager;

    @Autowired
    public UserDaoImp(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public List<Role>  findAllRole() {
        TypedQuery<Role> query =entityManager.createQuery("from Role ",Role.class);
        List<Role> Roles = query.getResultList();
        return Roles;
    }

    @Override
    public Long findRoleById(String roleName) {
        TypedQuery<Role> theQuery = entityManager.createQuery("from Role where name=:uName", Role.class);
        theQuery.setParameter("uName", roleName);

        Role theRole = null;
        try {
            theRole = theQuery.getSingleResult();
        } catch (Exception e) {
            theRole = null;
        }

        return theRole.getId();
    }

    @Override
    public User findUserById(Long id) {

        return entityManager.find(User.class,id);
    }

    @Override
    public User findUserByEmail(String email) {
        TypedQuery<User> theQuery = entityManager.createQuery("from User where email=:uEmail", User.class);
        theQuery.setParameter("uEmail", email);

        User theUser = null;
        try {
            theUser = theQuery.getSingleResult();
        } catch (Exception e) {
            theUser = null;
        }

        return theUser;
    }

    @Override
    public User createUser(User user) {

        entityManager.merge(user);
        return user;
    }


}
