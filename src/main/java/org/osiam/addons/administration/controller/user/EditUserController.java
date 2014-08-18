package org.osiam.addons.administration.controller.user;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.command.UpdateUserCommand;
import org.osiam.addons.administration.service.UserService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.resources.exception.SCIMDataValidationException;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the user edit view.
 */
@Controller
@RequestMapping(EditUserController.CONTROLLER_PATH)
public class EditUserController extends GenericController {
    private static final Logger LOG = Logger.getLogger(EditUserController.class);

    public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/user/edit";

    public static final String REQUEST_PARAMETER_ID = "id";
    public static final String REQUEST_PARAMETER_ERROR = "error";

    private static final String SESSION_KEY_COMMAND = "command";

    public static final String MODEL = "model";

    @Inject
    private UserService userService;

    @Inject
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleUserEdit(@RequestParam(value = REQUEST_PARAMETER_ID) final String id) {
        ModelAndView modelAndView = new ModelAndView("user/editUser");

        clearSession();

        User user = userService.getUser(id);
        modelAndView.addObject(MODEL, new UpdateUserCommand(user));

        return modelAndView;
    }

    private void clearSession() {
        removeFromSession(SESSION_KEY_COMMAND);
        removeBindingResultFromSession(MODEL);
    }

    @RequestMapping(method = RequestMethod.GET, params = REQUEST_PARAMETER_ERROR + "=validation")
    public ModelAndView handleUserEditFailure(
            @RequestParam(value = REQUEST_PARAMETER_ID) final String id) {

        ModelAndView modelAndView = new ModelAndView("user/editUser");

        modelAndView.addObject(MODEL, restoreFromSession(SESSION_KEY_COMMAND));
        enrichBindingResultFromSession(MODEL, modelAndView);

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String handleUserUpdate(
            @Valid @ModelAttribute(MODEL) UpdateUserCommand command,
            BindingResult bindingResult) {

        final RedirectBuilder redirect = new RedirectBuilder()
                                            .setPath(CONTROLLER_PATH)
                                            .addParameter(REQUEST_PARAMETER_ID, command.getId());

        try {
            if(!bindingResult.hasErrors()){
                User user = userService.getUser(command.getId());
                command.setUser(user);

                userService.replaceUser(command.getId(), command.getAsUser());

                redirect.addParameter("saveSuccess", true);
                redirect.setPath(UserViewController.CONTROLLER_PATH);
                return redirect.build();
            }
        } catch(SCIMDataValidationException e) {
            LOG.warn("Could not update user.", e);
        }

        storeInSession(SESSION_KEY_COMMAND, command);
        storeBindingResultIntoSession(bindingResult, MODEL);
        redirect.addParameter(REQUEST_PARAMETER_ERROR, "validation");

        return redirect.build();
    }

    private void validateCommand(UpdateUserCommand command, BindingResult bindingResult) {
        //we must validate for our own, because we need to purge the command
        //before we can validate it

        command.purge();
        validator.validate(command, bindingResult);
    }
}
