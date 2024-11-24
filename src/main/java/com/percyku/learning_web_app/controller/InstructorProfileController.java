package com.percyku.learning_web_app.controller;

import com.percyku.learning_web_app.entity.StudentProfile;
import com.percyku.learning_web_app.entity.User;
import com.percyku.learning_web_app.service.StudentProfileService;
import com.percyku.learning_web_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor_profile")
public class InstructorProfileController {

    private UserService userService;
    private StudentProfileService studentProfileService;
    @Autowired
    public InstructorProfileController(UserService userService, StudentProfileService studentProfileService) {
        this.userService = userService;
        this.studentProfileService =studentProfileService;
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
    public String addNew(StudentProfile studentProfile,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if((!studentProfile.getPassword().equals("") || !studentProfile.getPasswordAgain().equals(""))
                && !studentProfile.getPassword().equals(studentProfile.getPasswordAgain())){
            model.addAttribute("profileByInstructor", studentProfile);
            model.addAttribute("error", "If you want to change password,please fill Password &  Check Password Again content in same");
            return "instructor-profile";
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
                user.setPassword(studentProfile.getPassword());
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
