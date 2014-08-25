package org.osiam.addons.administration.controller.group;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.command.CreateGroupCommand;
import org.osiam.addons.administration.model.command.UpdateGroupCommand;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.resources.exception.SCIMDataValidationException;
import org.osiam.resources.scim.Group;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the create group view.
 */
@Controller
@RequestMapping(CreateGroupController.CONTROLLER_PATH)
public class CreateGroupController extends GenericController {
    private static final Logger LOG = Logger.getLogger(CreateGroupController.class);

    public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/group/create";

    public static final String REQUEST_PARAMETER_ERROR = "error";

    private static final String SESSION_KEY_COMMAND = "command";

    public static final String MODEL = "model";

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

    @RequestMapping(method = RequestMethod.GET, params = REQUEST_PARAMETER_ERROR + "=validation")
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

        final RedirectBuilder redirect = new RedirectBuilder()
                                            .setPath(CONTROLLER_PATH);

        try {
            if(!bindingResult.hasErrors()){
                groupService.createGroup(command.getAsGroup());

                redirect.addParameter("saveSuccess", true);
                redirect.setPath(GroupViewController.CONTROLLER_PATH);
                return redirect.build();
            }
        } catch(SCIMDataValidationException e) {
            LOG.warn("Could not add group.", e);
        }

        storeInSession(SESSION_KEY_COMMAND, command);
        storeBindingResultIntoSession(bindingResult, MODEL);
        redirect.addParameter(REQUEST_PARAMETER_ERROR, "validation");

        return redirect.build();
    }
}
