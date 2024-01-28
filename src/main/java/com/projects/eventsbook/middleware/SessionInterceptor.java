package com.projects.eventsbook.middleware;

import com.projects.eventsbook.DTO.userDomain.UserProfileDTO;
import com.projects.eventsbook.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

@Component
public class SessionInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler)
            throws Exception {
//
//        if (request.getRequestURI().equals("/groups")) {
//            return true;
//        }
//        WebUtils.setSessionAttribute(request, "currentUser", new UserProfileDTO(1L));
//        return true;
        boolean isAuthenticated = WebUtils.getSessionAttribute(request, "currentUser") != null;
        if (!isAuthenticated) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
        }

        return isAuthenticated;
    }

}
