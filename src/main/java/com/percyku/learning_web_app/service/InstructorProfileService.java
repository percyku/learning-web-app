package com.percyku.learning_web_app.service;

import com.percyku.learning_web_app.dao.UserDao;
import com.percyku.learning_web_app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstructorProfileService {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public InstructorProfileService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder=passwordEncoder;
    }

    @Transactional
    public User addNew(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.createUser(user);
        return user;
    }

}
