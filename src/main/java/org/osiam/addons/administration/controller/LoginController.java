package org.osiam.addons.administration.controller;

import javax.inject.Inject;

import org.osiam.addons.administration.model.SessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This controller contains all handler for the login page ( the root path / ).
 */
@Controller
public class LoginController {

    public static final String CONTROLLER_PATH = "/";

    @Inject
    private OsiamConnector connector;

    @Inject
    private SessionData session;

    @RequestMapping
    public String handleRedirectoToLogin() {
        return "redirect:" + connector.getAuthorizationUri(Scope.ALL).toString();
    }

    @RequestMapping(params = "code")
    public String handleLoggedIn(
            @RequestParam(value = "code", required = true) String code) {

        session.setAccesstoken(connector.retrieveAccessToken(code));
        return "redirect:admin";
    }
}
