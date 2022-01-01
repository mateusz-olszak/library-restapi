package com.library.service.facade;

import com.library.config.CustomAuthenticationSuccessHandler;
import com.library.domain.Reader;
import com.library.dto.books.ReaderDto;
import com.library.mappers.ReaderMapper;
import com.library.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReaderFacade {

    private final ReaderService readerService;
    private final ReaderMapper readerMapper;

    public String printLoginPage(Principal principal, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        request.getSession().setAttribute(CustomAuthenticationSuccessHandler.REDIRECT_URL_SESSION_ATTRIBUTE_NAME, referer);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken || principal == null) {
            return "login";
        }
        return "redirect:/books";
    }

    public Map<String, Object> printRegisterForm() {
        Map<String, Object> modelMap = new HashMap<>();
        ReaderDto readerDto = new ReaderDto();
        modelMap.put("reader", readerDto);
        return modelMap;
    }

    public void registerReader(String email, String password) {
        ReaderDto readerDto = new ReaderDto(email,password,new Date());
        Reader reader = readerMapper.mapToReader(readerDto);
        readerService.saveReader(reader);
    }

    public void deleteReader(int readerId) {
        readerService.deleteReader(readerId);
    }

    public String checkDuplicateEmail(String email) {
        return readerService.isEmailUnique(email) ? "OK" : "Duplicated";
    }
}
