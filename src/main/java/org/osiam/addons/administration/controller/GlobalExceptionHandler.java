package org.osiam.addons.administration.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.client.exception.UnauthorizedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class is responsible for handling all exceptions thrown by any controller-handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler implements AccessDeniedHandler {

    @Inject
    private GeneralSessionData session;
    
    
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized() {
        session.setAccesstoken(null);
        
        return new RedirectBuilder().setDestination(LoginController.CONTROLLER_PATH).build();
    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        response.sendRedirect(LoginController.CONTROLLER_PATH);
    }
}
