package org.osiam.addons.administration.controller.user;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.exception.NoSuchUserException;
import org.osiam.addons.administration.model.command.UpdateUserCommand;
import org.osiam.addons.administration.service.UserService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.resources.scim.UpdateUser;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleUserEdit(@RequestParam(value = REQUEST_PARAMETER_ID) final String id) throws NoSuchUserException {
        ModelAndView modelAndView = new ModelAndView("user/editUser");

        User user = userService.getUser(id);
        modelAndView.addObject(MODEL, new UpdateUserCommand(user));

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String handleUserUpdate(@ModelAttribute(MODEL) UpdateUserCommand command) throws NoSuchUserException {
        User user = userService.getUser(command.getId());
        command.setUser(user);

        UpdateUser updateUser = command.asUpdateUser();
        userService.updateUser(command.getId(), updateUser);

        return new RedirectBuilder().setPath(CONTROLLER_PATH)
                .addParameter(REQUEST_PARAMETER_ID, command.getId()).build();
    }
}
