package com.percyku.learning_web_app.service;


import com.percyku.learning_web_app.dao.UserDao;
import com.percyku.learning_web_app.entity.User;
import com.percyku.learning_web_app.until.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService  implements UserDetailsService {

    private UserDao userDao;

    public CustomUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("------username:"+username);
        User user = userDao.findUserByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException("User not found for:" + username);
        }else{
            return new CustomUserDetails(user);
        }

    }
}
