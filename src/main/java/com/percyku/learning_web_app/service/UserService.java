package com.percyku.learning_web_app.service;

import com.percyku.learning_web_app.dao.UserDao;
import com.percyku.learning_web_app.entity.Role;
import com.percyku.learning_web_app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder=passwordEncoder;
    }

    public List<Role> getAllRoles(){
        return userDao.findAllRole();
    }


    public User getUserEmail(String email) {
        User user =userDao.findUserByEmail(email);
        if(user ==null){
            return null;
        }
        return user;
    }

    @Transactional
    public User createUser(User user){

        if(user.getRoles()!= null){
           for(Role role : user.getRoles()){
               Long id =userDao.findRoleById(role.getName());
               role.setId(id);
           }
           user.setEnabled(true);
        }

        System.out.println(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.createUser(user);

        return user;
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
