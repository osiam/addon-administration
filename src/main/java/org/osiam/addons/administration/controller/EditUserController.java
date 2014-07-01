package org.osiam.addons.administration.controller;

import javax.inject.Inject;

import org.osiam.addons.administration.model.command.UpdateUserCommand;
import org.osiam.addons.administration.service.UserService;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the user edit view.
 * 
 * @author Timo Kanera, tarent solutions GmbH
 */
@Controller
@RequestMapping(EditUserController.CONTROLLER_PATH)
public class EditUserController {

    public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/user/edit";
    public static final String REQUEST_PARAMETER_ID = "id";
    public static final String MODEL = "model";
    
    @Inject
    private UserService userService;

    @RequestMapping
    public ModelAndView handle(
            @RequestParam(value = REQUEST_PARAMETER_ID) final String id) {
        ModelAndView modelAndView = new ModelAndView("user/editUser");
        
        User user = userService.getUser(id);
        if (user != null) {
            modelAndView.addObject(MODEL, new UpdateUserCommand(user));
        }
        
        return modelAndView;
    }
}
