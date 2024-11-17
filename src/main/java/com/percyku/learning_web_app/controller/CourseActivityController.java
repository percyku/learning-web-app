package com.percyku.learning_web_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class CourseActivityController {

    @GetMapping("/dashboard/")
    public String searchJobs(Model model){
        return "dashboard";
    }

}
