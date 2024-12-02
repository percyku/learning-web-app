package com.percyku.learning_web_app.controller;

import com.percyku.learning_web_app.entity.InstructorProfile;
import com.percyku.learning_web_app.entity.StudentProfile;
import com.percyku.learning_web_app.entity.User;
import com.percyku.learning_web_app.service.InstructorProfileService;
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
@RequestMapping("/instructor_profile")
public class InstructorProfileController {

    private UserService userService;
    private InstructorProfileService instructorProfileService;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public InstructorProfileController(UserService userService, InstructorProfileService instructorProfileService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.instructorProfileService =instructorProfileService;
        this.passwordEncoder=passwordEncoder;
    }


    @GetMapping("/")
    public String instructorProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userService.getUserEmail(authentication.getName());
            if (user == null) {
                return "redirect:/error/";
            }

            StudentProfile studentProfile =new StudentProfile(user.getId(),user.getUserName(),user.getFirstName(),user.getLastName(),user.getEmail());
            model.addAttribute("profileByInstructor", studentProfile);
        }
        return "instructor-profile";
    }


    @PostMapping("/addNew")
    public String addNew(InstructorProfile instructorProfile, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if((!instructorProfile.getPassword().equals("") || !instructorProfile.getPasswordAgain().equals(""))
                && !instructorProfile.getPassword().equals(instructorProfile.getPasswordAgain())){
            model.addAttribute("profileByInstructor", instructorProfile);
            model.addAttribute("error", "If you want to change password,please fill Password &  Check Password Again content in same");
            return "instructor-profile";
        }

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userService.getUserEmail(authentication.getName());
            if (user == null) {
                return "redirect:/error/";
            }


            user.setUserName(instructorProfile.getUserName());
            user.setFirstName(instructorProfile.getFirstName());
            user.setLastName(instructorProfile.getLastName());

            boolean logoutFlag=false;
            if(!instructorProfile.getPassword().equals("")){
                user.setPassword(passwordEncoder.encode(instructorProfile.getPassword()));
                logoutFlag=true;
            }
            instructorProfileService.addNew(user);

            if(logoutFlag){
               return "redirect:/logout";
            }


        }

        return "redirect:/dashboard/";
    }

}
