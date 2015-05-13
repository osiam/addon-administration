package org.osiam.addons.administration.controller.group;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.command.CreateGroupCommand;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.client.exception.ConflictException;
import org.osiam.resources.exception.SCIMDataValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the create group view.
 */
@Controller
@RequestMapping(CreateGroupController.CONTROLLER_PATH)
public class CreateGroupController extends GenericController {

    public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/group/create";
    public static final String REQUEST_PARAMETER_ERROR = "error";
    public static final String REQUEST_PARAMETER_CREATE_SUCCESS = "createSuccess";
    public static final String REQUEST_PARAMETER_ERROR_RESET_VALUES = "resetValues";
    public static final String MODEL = "model";
    private static final Logger LOG = Logger.getLogger(CreateGroupController.class);
    private static final String SESSION_KEY_COMMAND = "command";
    @Inject
    private GroupService groupService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleCreateGroup() {
        ModelAndView modelAndView = new ModelAndView("group/createGroup");

        clearSession();

        modelAndView.addObject(MODEL, new CreateGroupCommand());

        return modelAndView;
    }

    private void clearSession() {
        removeFromSession(SESSION_KEY_COMMAND);
        removeBindingResultFromSession(MODEL);
    }

    @RequestMapping(method = RequestMethod.GET, params = REQUEST_PARAMETER_ERROR_RESET_VALUES + "=true")
    public ModelAndView handleCreateGroupFailure() {
        ModelAndView modelAndView = new ModelAndView("group/createGroup");

        modelAndView.addObject(MODEL, restoreFromSession(SESSION_KEY_COMMAND));
        enrichBindingResultFromSession(MODEL, modelAndView);

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String handleCreateGroup(
            @Valid @ModelAttribute(MODEL) CreateGroupCommand command,
            BindingResult bindingResult) {

        boolean isDuplicated = false;
        final RedirectBuilder redirect = new RedirectBuilder()
                .setPath(CONTROLLER_PATH);

        try {
            if (!bindingResult.hasErrors()) {
                groupService.createGroup(command.getAsGroup());

                redirect.addParameter(REQUEST_PARAMETER_CREATE_SUCCESS, "true");
                redirect.setPath(GroupViewController.CONTROLLER_PATH);
                return redirect.build();
            }
        } catch (SCIMDataValidationException e) {
            LOG.warn("Could not add group.", e);
        } catch (ConflictException e) {
            LOG.warn("Could not create group. Displayname already taken", e);
            // duplicate parameter instead validation parameter
            isDuplicated = true;
        }

        // Set error parameter
        if (isDuplicated) {
            redirect.addParameter(REQUEST_PARAMETER_ERROR, "duplicated");
        } else {
            redirect.addParameter(REQUEST_PARAMETER_ERROR, "validation");
        }

        redirect.addParameter(REQUEST_PARAMETER_ERROR_RESET_VALUES, "true");

        storeInSession(SESSION_KEY_COMMAND, command);
        storeBindingResultIntoSession(bindingResult, MODEL);

        return redirect.build();
    }
}
