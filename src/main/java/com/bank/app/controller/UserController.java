package com.bank.app.controller;

import com.bank.app.security.UserCreatorFormValidator;
import com.bank.app.domain.model.UserCreateForm;
import com.bank.app.sevice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCreatorFormValidator userCreatorFormValidator;

    @InitBinder("registerForm")
    public void registerFormInitBinder(WebDataBinder binder) {
        binder.addValidators(userCreatorFormValidator);

    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }


    @PostMapping("/login")
    public String processLogin(){
        return "redirect:/";
    }


   @GetMapping("/register")
    public String register(@ModelAttribute("registerForm") UserCreateForm userCreateForm) {
        return "register";

    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registrationForm") UserCreateForm userCreateForm,
                                      BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "register";
        }

        userService.registerUser(userCreateForm);
        return "redirect:/";
    }


}
