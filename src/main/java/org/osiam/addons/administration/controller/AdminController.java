package org.osiam.addons.administration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller contains all handler for the page after the login ( the path /admin ).
 */
@Controller
@RequestMapping(AdminController.CONTROLLER_PATH)
public class AdminController {

    public final static String CONTROLLER_PATH = "/admin";

    @RequestMapping
    public String handleOverview() {
        return "redirect:" + UserViewController.CONTROLLER_PATH;
    }
}
