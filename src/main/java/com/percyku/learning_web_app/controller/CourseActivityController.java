package com.percyku.learning_web_app.controller;

import com.percyku.learning_web_app.entity.Course;
import com.percyku.learning_web_app.entity.PageCourse;
import com.percyku.learning_web_app.entity.User;
import com.percyku.learning_web_app.service.CourseActivityService;
import com.percyku.learning_web_app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class CourseActivityController {
    private final static Logger log = LoggerFactory.getLogger(CourseActivityController.class);

    UserService userService;
    CourseActivityService courseActivityService;

    @Autowired
    public CourseActivityController(UserService userService,CourseActivityService courseActivityService) {
        this.userService = userService;
        this.courseActivityService=courseActivityService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(Model model,@RequestParam(value = "searchCourseName", required = false) String searchCourseName){

        User user =(User)courseActivityService.getCurrentUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))) {
                List<PageCourse> pageCourses =courseActivityService.getCourseByInstructor(user);
                log.debug("result:"+pageCourses);
                model.addAttribute("pageCourse", pageCourses);
            }else{
                log.debug("job:"+searchCourseName);
                List<PageCourse> pageCourses =null;
                if(searchCourseName != null) {
                    pageCourses=courseActivityService.getCourseByCourseName(user.getEmail(),searchCourseName);
                }else{
                    pageCourses=courseActivityService.getCourseByStudent(user);
                }
                log.debug("result:"+pageCourses);
                model.addAttribute("pageCourse", pageCourses);
            }

        }



        return "dashboard";
    }




    @GetMapping("/dashboard/add")
    public String addJobs(Model model){
        model.addAttribute("course",new Course());
        //model.addAttribute("user",userService.getCurrentUserProfile());
        return "add-courses";
    }



    @PostMapping("/dashboard/addNew")
    public String addNew(Course course,Model model){

        Course checkCourse = courseActivityService.findCourseById(course.getId());

        if(checkCourse == null){
            User user =(User)userService.getCurrentUser();
            if (user == null) {
                return "redirect:/error/";
            }
            course.setUser(user);
            course.setReviews(null);
            courseActivityService.createCourse(course);
        }else{
            checkCourse.setTitle(course.getTitle());
            checkCourse.setPrice(course.getPrice());
            checkCourse.setDescription(course.getDescription());
            courseActivityService.createCourse(checkCourse);
        }


        return "redirect:/dashboard/";
    }


    @GetMapping("/dashboard/edit/{id}")
    public String editCourse(@PathVariable("id")int id , Model model){

        Course course = courseActivityService.findCourseById(id);
        User user =(User) userService.getCurrentUser();



        if(course == null || user == null) return "redirect:/dashboard/";
        if(course.getUser().getId() != user.getId()) return "redirect:/dashboard/";

        model.addAttribute("course",course);

        return "edit-courses";
    }

}
