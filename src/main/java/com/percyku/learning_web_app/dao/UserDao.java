package com.percyku.learning_web_app.dao;

import com.percyku.learning_web_app.entity.Role;
import com.percyku.learning_web_app.entity.User;

import java.util.List;

public interface UserDao {

    List<Role> findAllRole();

    Long findRoleById(String roleName);

    User findUserById(Long id);
    User findUserByEmail(String email);

    User createUser(User user);

}
