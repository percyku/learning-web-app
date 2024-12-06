package com.percyku.learning_web_app.conf;

import com.percyku.learning_web_app.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private  CustomUserDetailService customUserDetailService;
    private  CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public WebSecurityConfig(CustomUserDetailService customUserDetailService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customUserDetailService = customUserDetailService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    private final String[] publicUrl = {"/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error"};


    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth->{
            auth.requestMatchers(publicUrl).permitAll();
            auth.requestMatchers("/register","/login","/logout").permitAll();
            auth.requestMatchers("/logout","/dashboard/","/dashboard/course/{id}").authenticated();
            auth.requestMatchers("/dashboard/add","/dashboard/addNew").hasRole("INSTRUCTOR");
            auth.requestMatchers("/instructor_profile/","/instructor_profile/addNew").hasRole("INSTRUCTOR");
            auth.requestMatchers("/dashboard/enroll").hasRole("STUDENT");
            auth.requestMatchers("/student_profile/","/student_profile/addNew").hasRole("STUDENT");
            auth.anyRequest().denyAll();

//            auth.anyRequest().authenticated();
        });

        http.formLogin(form->form.loginPage("/login").successHandler(customAuthenticationSuccessHandler))
                .logout(logout ->{
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");
                })
                .cors(Customizer.withDefaults())
                .csrf(csrf->csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customUserDetailService);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
