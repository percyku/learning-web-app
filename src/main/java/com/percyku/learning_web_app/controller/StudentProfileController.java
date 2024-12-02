package com.percyku.learning_web_app.controller;

import com.percyku.learning_web_app.entity.StudentProfile;
import com.percyku.learning_web_app.entity.User;
import com.percyku.learning_web_app.service.StudentProfileService;
import com.percyku.learning_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student_profile")
public class StudentProfileController {

    private UserService userService;
    private StudentProfileService studentProfileService;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public StudentProfileController(UserService userService, StudentProfileService studentProfileService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.studentProfileService =studentProfileService;
        this.passwordEncoder=passwordEncoder;
    }


    @GetMapping("/")
    public String studentProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userService.getUserEmail(authentication.getName());
            if (user == null) {
                return "redirect:/error/";
            }

            StudentProfile studentProfile =new StudentProfile(user.getId(),user.getUserName(),user.getFirstName(),user.getLastName(),user.getEmail());
            model.addAttribute("profileByStudent", studentProfile);
        }
        return "student-profile";
    }


    @PostMapping("/addNew")
    public String addNew(StudentProfile studentProfile,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(studentProfile);

        if((!studentProfile.getPassword().equals("") || !studentProfile.getPasswordAgain().equals(""))
                && !studentProfile.getPassword().equals(studentProfile.getPasswordAgain())){
            model.addAttribute("profileByStudent", studentProfile);
            model.addAttribute("error", "If you want to change password,please fill Password &  Check Password Again content in same");
            return "student-profile";
        }

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userService.getUserEmail(authentication.getName());
            if (user == null) {
                return "redirect:/error/";
            }


            user.setUserName(studentProfile.getUserName());
            user.setFirstName(studentProfile.getFirstName());
            user.setLastName(studentProfile.getLastName());

            boolean logoutFlag=false;
            if(!studentProfile.getPassword().equals("")){
                user.setPassword(passwordEncoder.encode(studentProfile.getPassword()));
                logoutFlag=true;
            }
            studentProfileService.addNew(user);

            if(logoutFlag){
               return "redirect:/logout";
            }

        }

        return "redirect:/dashboard/";
    }

}
