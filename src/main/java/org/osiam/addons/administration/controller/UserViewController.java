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
    
    public static final String REQUEST_PARAMETER_QUERY = "query";
    public static final String REQUEST_PARAMETER_LIMIT = "limit";
    public static final String REQUEST_PARAMETER_OFFSET = "offset";
    public static final String REQUEST_PARAMETER_ORDER_BY = "orderBy";
    public static final String REQUEST_PARAMETER_ASCENDING = "asc";
    
    public static final String MODEL_USER_LIST = "userlist";
    
    @Inject
    private UserService userService;
    
    @RequestMapping
    public ModelAndView handleList(
            @RequestParam(value = REQUEST_PARAMETER_QUERY, required = false) 
            String query,
            @RequestParam(value = REQUEST_PARAMETER_LIMIT, required = false) 
            Integer limit,
            @RequestParam(value = REQUEST_PARAMETER_OFFSET, required = false) 
            Long offset,
            @RequestParam(value = REQUEST_PARAMETER_ORDER_BY, required = false) 
            String orderBy,
            @RequestParam(value = REQUEST_PARAMETER_ASCENDING, required = false) 
            Boolean ascending) {
        
        ModelAndView modelAndView = new ModelAndView("user/list");

        SCIMSearchResult<User> userList = userService.searchUser(query, limit, offset, orderBy, ascending);
        modelAndView.addObject(MODEL_USER_LIST, userList);
        
        return modelAndView;
    }
}
