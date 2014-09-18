package org.osiam.addons.administration.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.command.UpdateUserCommand;
import org.osiam.addons.administration.service.ExtensionsService;
import org.osiam.addons.administration.service.UserService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.resources.exception.SCIMDataValidationException;
import org.osiam.resources.scim.Extension;
import org.osiam.resources.scim.Extension.Field;
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
    public static final String MODEL_ALL_TYPES = "allFieldTypes";

    @Inject
    private UserService userService;

    @Inject
    private ExtensionsService extensionService;

    @Inject
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleUserEdit(@RequestParam(value = REQUEST_PARAMETER_ID) final String id) {
        ModelAndView modelAndView = new ModelAndView("user/editUser");

        clearSession();

        List<Extension> extensions = extensionService.getExtensions();

        User user = userService.getUser(id);
        modelAndView.addObject(MODEL, new UpdateUserCommand(user, extensions));
        modelAndView.addObject(MODEL_ALL_TYPES, extractFieldTypes(extensions));

        return modelAndView;
    }

    private Map<String, Map<String, String>> extractFieldTypes(List<Extension> extensions) {
        Map<String, Map<String, String>> result = new HashMap<String, Map<String,String>>();

        for(Extension extension : extensions){
            result.put(extension.getUrn(), new HashMap<String, String>());

            for(Entry<String, Field> entry : extension.getFields().entrySet()){
                result.get(extension.getUrn()).put(entry.getKey(), entry.getValue().getType().getName());
            }
        }

        return result;
    }

    private void clearSession() {
        removeFromSession(SESSION_KEY_COMMAND);
        removeBindingResultFromSession(MODEL);
    }

    @RequestMapping(method = RequestMethod.GET, params = REQUEST_PARAMETER_ERROR + "=validation")
    public ModelAndView handleUserEditFailure(
            @RequestParam(value = REQUEST_PARAMETER_ID) final String id) {

        ModelAndView modelAndView = new ModelAndView("user/editUser");

        UpdateUserCommand cmd = (UpdateUserCommand)restoreFromSession(SESSION_KEY_COMMAND);
        cmd.enrichExtensions(extensionService.getExtensions());

        modelAndView.addObject(MODEL, cmd);
        modelAndView.addObject(MODEL_ALL_TYPES, extractFieldTypes(extensionService.getExtensions()));

        enrichBindingResultFromSession(MODEL, modelAndView);

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String handleUserUpdate(
            @ModelAttribute(MODEL) UpdateUserCommand command,
            BindingResult bindingResult) {

        User user = userService.getUser(command.getId());
        command.setUser(user);

        validateCommand(command, extensionService.getExtensionsMap(), bindingResult);

        final RedirectBuilder redirect = new RedirectBuilder()
                                            .setPath(CONTROLLER_PATH)
                                            .addParameter(REQUEST_PARAMETER_ID, command.getId());

        try {
            if(!bindingResult.hasErrors()){
                userService.replaceUser(command.getId(), command.getAsUser());

                redirect.addParameter("saveSuccess", true);
                redirect.setPath(UserViewController.CONTROLLER_PATH);
                return redirect.build();
            }
        } catch(SCIMDataValidationException e) {
            // just log the exception and fall through to error handling
            LOG.warn("Validation failed. Unable to update user.", e);
        }
        
        // validation failed - store error information in session and return to edit view
        storeInSession(SESSION_KEY_COMMAND, command);
        storeBindingResultIntoSession(bindingResult, MODEL);
        redirect.addParameter(REQUEST_PARAMETER_ERROR, "validation");

        return redirect.build();
    }

    /**
     * We must validate for our own, because we need to purge the command
     * before we can validate it.
     *
     * @param command the command object
     * @param bindingResult the binding result for that command
     */
    private void validateCommand(UpdateUserCommand command, Map<String, Extension> allExtensions, BindingResult bindingResult) {
        command.purge();
        command.validate(allExtensions, bindingResult);
        validator.validate(command, bindingResult);
    }
}
