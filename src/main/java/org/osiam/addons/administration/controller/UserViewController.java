package org.osiam.addons.administration.controller;

import javax.inject.Inject;

import org.osiam.addons.administration.service.UserService;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is responsible for handling user list. 
 * This is the main page of the "userview".
 */
@Controller
@RequestMapping(UserViewController.CONTROLLER_PATH)
public class UserViewController {

    public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/user/list";
    
    /*
     * List of request parameters.
     */
    
    public static final String RP_QUERY = "query";
    public static final String RP_LIMIT = "limit";
    public static final String RP_OFFSET = "offset";
    public static final String RP_ORDER_BY = "orderBy";
    public static final String RP_ASCENDING = "asc";
    
    
    /*
     * List of model names.
     */
    public static final String MN_USER_LIST = "userlist";
    
    
    @Inject
    private UserService service;
    
    @RequestMapping
    public ModelAndView handleList(
            @RequestParam(value = RP_QUERY, required = false) 
            String query,
            @RequestParam(value = RP_LIMIT, required = false) 
            Integer limit,
            @RequestParam(value = RP_OFFSET, required = false) 
            Long offset,
            @RequestParam(value = RP_ORDER_BY, required = false) 
            String orderBy,
            @RequestParam(value = RP_ASCENDING, required = false) 
            Boolean ascending) {
        
        ModelAndView mav = new ModelAndView("user/list");

        SCIMSearchResult<User> userList = service.searchUser(query, limit, offset, orderBy, ascending);
        mav.addObject(MN_USER_LIST, userList);
        
        return mav;
    }
}
