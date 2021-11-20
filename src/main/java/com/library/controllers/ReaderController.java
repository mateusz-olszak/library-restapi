package com.library.controllers;

import com.library.config.CustomAuthenticationSuccessHandler;
import com.library.domain.Reader;
import com.library.dto.ReaderDto;
import com.library.mappers.ReaderMapper;
import com.library.service.ReaderService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

@Controller
@AllArgsConstructor
public class ReaderController {

    private ReaderService readerService;
    private ReaderMapper readerMapper;

    @GetMapping("/readers/login")
    String viewLoginForm(Model model, Principal principal, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        request.getSession().setAttribute(CustomAuthenticationSuccessHandler.REDIRECT_URL_SESSION_ATTRIBUTE_NAME, referer);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken || principal == null) {
            return "login";
        }
        return "redirect:/books";
    }

    @GetMapping("/login")
    String reachLoginPage(Model model, Principal principal, HttpServletRequest request) {
        return viewLoginForm(model,principal,request);
    }

    @GetMapping("/readers/register")
    String viewRegisterForm(Model model){
        ReaderDto readerDto = new ReaderDto();
        model.addAttribute("reader", readerDto);
        return "register";
    }

    @RequestMapping("/readers/register/save")
    String registerReader(@RequestParam("email") String email, @RequestParam("password") String password) {
        ReaderDto readerDto = new ReaderDto(email,password,new Date());
        Reader reader = readerMapper.mapToReader(readerDto);
        readerService.saveReader(reader);
        return "redirect:/books";
    }

    @DeleteMapping("/readers/delete/{id}")
    void deleteReader(@PathVariable int id){
        readerService.deleteReader(id);
    }

}
