package com.keycraft.config;

import com.keycraft.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        
        // Check if this is an admin-only endpoint
        if (requestURI.startsWith("/api/admin/") || 
            (requestURI.startsWith("/api/products") && 
             ("POST".equals(httpRequest.getMethod()) || 
              "PUT".equals(httpRequest.getMethod()) || 
              "DELETE".equals(httpRequest.getMethod())))) {
            
            HttpSession session = httpRequest.getSession(false);
            User currentUser = null;
            
            if (session != null) {
                currentUser = (User) session.getAttribute("currentUser");
            }
            
            if (currentUser == null || !User.UserRole.ADMIN.equals(currentUser.getRole())) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"message\":\"Admin access required\"}");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
}