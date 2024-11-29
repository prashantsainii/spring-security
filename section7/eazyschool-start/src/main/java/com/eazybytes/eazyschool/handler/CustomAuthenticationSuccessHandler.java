package com.eazybytes.eazyschool.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// in this class, I can write the custom logic when the authentication gets successful
// gives more control to developer - what to happen

//defaultSuccessUrl("/dashboard") - will only redirect to particular url when the authentication gets successful
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Login successful for the user: {}", authentication.getName());
        response.sendRedirect("/dashboard");

        // apart from these i can have many other things as well, like triggering the email, creating a database entry
    }
}
