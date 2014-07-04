package org.osiam.addons.administration.controller;

import org.osiam.addons.administration.controller.user.UserViewController;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller contains all handler for the page after the login ( the path /admin ).
 */
@Controller
@RequestMapping(AdminController.CONTROLLER_PATH)
public class AdminController {

    public static final String CONTROLLER_PATH = "/admin";

    @RequestMapping
    public String handleOverview() {
        return new RedirectBuilder()
                .setPath(UserViewController.CONTROLLER_PATH)
                .build();
    }
}
