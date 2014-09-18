package org.osiam.addons.administration.controller.group;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.command.UpdateGroupCommand;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.addons.administration.service.UserService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.resources.exception.SCIMDataValidationException;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the group edit view.
 */
@Controller
@RequestMapping(EditGroupController.CONTROLLER_PATH)
public class EditGroupController extends GenericController {
    private static final Logger LOG = Logger.getLogger(EditGroupController.class);

    public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/group/edit";

    public static final String REQUEST_PARAMETER_ID = "id";
    public static final String REQUEST_PARAMETER_ERROR = "error";

    private static final String SESSION_KEY_COMMAND = "command";

    public static final String MODEL = "model";
    public static final String MODEL_USER_LIST = "userList";

    @Inject
    private GroupService groupService;

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleGroupEdit(@RequestParam(value = REQUEST_PARAMETER_ID) final String id) {
        ModelAndView modelAndView = new ModelAndView("group/editGroup");

        clearSession();

        Group group = groupService.getGroup(id);
        Map<String, User> allUsers = getAllUsers();

        modelAndView.addObject(MODEL, new UpdateGroupCommand(group));
        modelAndView.addObject(MODEL_USER_LIST, allUsers);

        return modelAndView;
    }

    private void clearSession() {
        removeFromSession(SESSION_KEY_COMMAND);
        removeBindingResultFromSession(MODEL);
    }

    private Map<String, User> getAllUsers() {
        SCIMSearchResult<User> result = userService.searchUser("", 0, 0L, "userName", true, "id, userName");

        Map<String, User> idToUserMapping = new HashMap<String, User>();

        for(User user : result.getResources()){
            idToUserMapping.put(user.getId(), user);
        }

        return idToUserMapping;
    }

    @RequestMapping(method = RequestMethod.GET, params = REQUEST_PARAMETER_ERROR + "=validation")
    public ModelAndView handleGroupEditFailure(
            @RequestParam(value = REQUEST_PARAMETER_ID) final String id) {

        ModelAndView modelAndView = new ModelAndView("group/editGroup");

        modelAndView.addObject(MODEL, restoreFromSession(SESSION_KEY_COMMAND));
        enrichBindingResultFromSession(MODEL, modelAndView);

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String handleGroupUpdate(
            @Valid @ModelAttribute(MODEL) UpdateGroupCommand command,
            BindingResult bindingResult) {

        final RedirectBuilder redirect = new RedirectBuilder()
                                            .setPath(CONTROLLER_PATH)
                                            .addParameter(REQUEST_PARAMETER_ID, command.getId());

        try {
            if(!bindingResult.hasErrors()){
                groupService.updateGroup(command.getId(), command.getAsUpdateGroup());

                redirect.addParameter("saveSuccess", true);
                redirect.setPath(GroupViewController.CONTROLLER_PATH);
                return redirect.build();
            }
        } catch(SCIMDataValidationException e) {
            LOG.warn("Could not update group.", e);
        }

        storeInSession(SESSION_KEY_COMMAND, command);
        storeBindingResultIntoSession(bindingResult, MODEL);
        redirect.addParameter(REQUEST_PARAMETER_ERROR, "validation");

        return redirect.build();
    }
}
