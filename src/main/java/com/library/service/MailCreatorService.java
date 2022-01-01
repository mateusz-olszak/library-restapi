package com.library.service;

import com.library.config.AdminConfig;
import com.library.config.CompanyConfig;
import com.library.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailCreatorService {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private CopyService copyService;

    @Autowired
    private CompanyConfig companyConfig;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildEmailCard(String message) {
        Context context = new Context();
        long available = copyService.findAllAvailableBooks(Status.AVAILABLE).stream().count();
        long rented = copyService.findAllAvailableBooks(Status.RENTED).stream().count();
        long destroyed = copyService.findAllAvailableBooks(Status.DESTROYED).stream().count();
        long lost = copyService.findAllAvailableBooks(Status.LOST).stream().count();
        context.setVariable("message", message);
        context.setVariable("available", available);
        context.setVariable("rented", rented);
        context.setVariable("destroyed", destroyed);
        context.setVariable("lost", lost);
        context.setVariable("admin", adminConfig);
        context.setVariable("company", companyConfig);
        context.setVariable("button", "Visit Website");
        context.setVariable("buttonUrl", "http://localhost:8080/books");
        return templateEngine.process("emailCard",context);
    }

}
