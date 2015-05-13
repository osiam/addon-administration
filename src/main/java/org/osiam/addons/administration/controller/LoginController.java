package org.osiam.addons.administration.controller;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This controller contains all handler for the login page ( the root path / ).
 */
@Controller
@RequestMapping(LoginController.CONTROLLER_PATH)
public class LoginController {

    public static final String CONTROLLER_PATH = "/";

    @Inject
    private OsiamConnector connector;

    @Inject
    private GeneralSessionData session;

    @RequestMapping
    public String handleRedirectToLogin() {
        return new RedirectBuilder()
                .setDestination(connector.getAuthorizationUri(Scope.ALL).toString())
                .build();
    }

    @RequestMapping(params = "code")
    public String handleLoggedIn(
            @RequestParam(value = "code", required = true) String code) {

        session.setAccessToken(connector.retrieveAccessToken(code));
        return new RedirectBuilder()
                .setPath(AdminController.CONTROLLER_PATH)
                .build();
    }
}
