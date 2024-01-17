package com.projects.eventsbook.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Configuration
public class CommonAttributesConfig {
    @ModelAttribute("authenticated")
    public boolean addAuthenticatedAttribute(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
