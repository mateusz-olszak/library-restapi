package com.library.controllers;

import com.library.service.facade.ReaderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderFacade readerFacade;
    private final ModelFillerService modelFillerService;

    @GetMapping("/readers/login")
    String viewLoginForm(Principal principal, HttpServletRequest request) {
        return readerFacade.printLoginPage(principal, request);
    }

    @GetMapping("/login")
    String reachLoginPage(Principal principal, HttpServletRequest request) {
        return viewLoginForm(principal,request);
    }

    @GetMapping("/readers/register")
    String viewRegisterForm(Model model){
        Map<String, Object> modelMap = readerFacade.printRegisterForm();
        modelFillerService.fullFillModel(model,modelMap);
        return "register";
    }

    @RequestMapping("/readers/register/save")
    String registerReader(@RequestParam("email") String email, @RequestParam("password") String password) {
        readerFacade.registerReader(email, password);
        return "redirect:/books";
    }
}
