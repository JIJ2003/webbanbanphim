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
	    
	    if (requestURI.startsWith("/api/dashboard/") || 
	        (requestURI.startsWith("/api/products") && 
	         ("POST".equals(httpRequest.getMethod()) || 
	          "PUT".equals(httpRequest.getMethod()) || 
	          "DELETE".equals(httpRequest.getMethod())))) {
	        
	        HttpSession session = httpRequest.getSession(false);
	        User currentUser = null;
	        
	        System.out.println("Request URI: " + requestURI);
	        System.out.println("HTTP Method: " + httpRequest.getMethod());
	        System.out.println("Session: " + session);
	        
	        if (session != null) {
	            currentUser = (User) session.getAttribute("currentUser");
	        }
	        
	        System.out.println("CurrentUser: " + currentUser);
	        
	        if (currentUser == null) {
	            System.out.println("No user logged in.");
	            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            httpResponse.setContentType("application/json");
	            httpResponse.getWriter().write("{\"message\":\"Admin access required: no user\"}");
	            return;
	        }
	        
	        System.out.println("User Role: " + currentUser.getRole());
	        if (!User.UserRole.ADMIN.equals(currentUser.getRole())) {
	            System.out.println("User is not admin.");
	            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            httpResponse.setContentType("application/json");
	            httpResponse.getWriter().write("{\"message\":\"Admin access required: not admin\"}");
	            return;
	        }
	    }
	    
	    chain.doFilter(request, response);
	}

}