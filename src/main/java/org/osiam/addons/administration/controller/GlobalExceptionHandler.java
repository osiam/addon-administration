package org.osiam.addons.administration.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.client.exception.UnauthorizedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;

/**
 * This class is responsible for handling all exceptions thrown by any controller-handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler implements AccessDeniedHandler {
    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

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

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUncaught(Exception e) {
        LOG.error("Uncaught exception was thrown.", e);

        ModelAndView mav = new ModelAndView("adminError");

        mav.addObject("exception", e);
        mav.addObject("stacktrace", getStackTrace(e));

        return mav;
    }

    private String getStackTrace(Throwable t) {
        return Throwables.getStackTraceAsString(t);
    }
}
