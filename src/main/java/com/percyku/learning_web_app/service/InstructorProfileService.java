package com.percyku.learning_web_app.service;

import com.percyku.learning_web_app.dao.UserDao;
import com.percyku.learning_web_app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstructorProfileService {

    private UserDao userDao;


    @Autowired
    public InstructorProfileService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public User addNew(User user){
        userDao.createUser(user);
        return user;
    }

}
