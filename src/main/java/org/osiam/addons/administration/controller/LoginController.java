package org.osiam.addons.administration.controller;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.client.OsiamConnector;
import org.osiam.client.exception.ConflictException;
import org.osiam.client.oauth.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;

/**
 * This controller contains all handler for the login page ( the root path / ).
 */
@Controller
@RequestMapping(LoginController.CONTROLLER_PATH)
public class LoginController {

    public static final String CONTROLLER_PATH = "/";

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Inject
    private OsiamConnector connector;

    @Inject
    private GeneralSessionData generalSessionData;

    @RequestMapping
    public String showStartPage() {
        return "start";
    }

    @RequestMapping(params = "code")
    public String handleLoggedIn(@RequestParam(value = "code", required = true) String code) {
        try {
            generalSessionData.setAccessToken(connector.retrieveAccessToken(code));
        } catch (ConflictException e) {
            logger.error(e.getMessage());
            return "/";
        }

        return "redirect:/";
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn() {
        return generalSessionData.getAccessToken() != null;
    }

    @ModelAttribute("redirectUri")
    public String getRedirectUri() {
        return connector.getAuthorizationUri(Scope.ADMIN).toString();
    }
}
