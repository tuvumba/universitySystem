package dev.tuvumba.universityClient.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        if(request.getRequestURI().startsWith("/login") && request.getSession().getAttribute("jwt") != null) {
            response.sendRedirect("/dashboard");
            System.out.println("Redirect to dashboard");
            return false;
        }

        if (requestURI.startsWith("/login") || requestURI.startsWith("/css") || requestURI.startsWith("/js")) {
            return true;
        }

        if (request.getSession().getAttribute("jwt") == null) {
            response.sendRedirect("/login");
            System.out.println("Redirect to login");
            return false;
        }


        return true;
    }
}
