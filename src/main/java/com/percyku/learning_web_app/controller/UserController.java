package com.percyku.learning_web_app.controller;

import com.percyku.learning_web_app.entity.Role;
import com.percyku.learning_web_app.entity.User;
import com.percyku.learning_web_app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";

    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/";

    }


    @GetMapping("/register")
    public String register(Model model){

        List<Role>roles =userService.getAllRoles();
        model.addAttribute("roles",roles);
        model.addAttribute("user",new User());

        return "register";

    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid User user, Model model){
//        System.out.println("User::" +user);


        User checkUser= userService.getUserEmail(user.getEmail());


        if(checkUser!= null){
            model.addAttribute("error", "Email already registered,try to login or register with other email.");
            List<Role>roles =userService.getAllRoles();
            model.addAttribute("roles",roles);
            model.addAttribute("user",user);
            return "register";
        }

        userService.createUser(user);

        return "redirect:/dashboard/";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
}
