package org.osiam.addons.administration.controller.group;

import javax.inject.Inject;
import javax.validation.Valid;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.command.UpdateGroupCommand;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.client.exception.ConflictException;
import org.osiam.resources.exception.SCIMDataValidationException;
import org.osiam.resources.scim.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(EditGroupController.class);

	public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/group/edit";

	public static final String REQUEST_PARAMETER_ID = "id";
	public static final String REQUEST_PARAMETER_ERROR = "error";
	public static final String REQUEST_PARAMETER_ERROR_RESET_VALUES = "resetValues";

	private static final String SESSION_KEY_COMMAND = "command";

	public static final String MODEL = "model";

	@Inject
	private GroupService groupService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGroupEdit(@RequestParam(value = REQUEST_PARAMETER_ID) final String id) {
		ModelAndView modelAndView = new ModelAndView("group/editGroup");

		clearSession();

		Group group = groupService.getGroup(id);

		modelAndView.addObject(MODEL, new UpdateGroupCommand(group));

		return modelAndView;
	}

	private void clearSession() {
		removeFromSession(SESSION_KEY_COMMAND);
		removeBindingResultFromSession(MODEL);
	}

	@RequestMapping(method = RequestMethod.GET, params = REQUEST_PARAMETER_ERROR_RESET_VALUES + "=true")
	public ModelAndView handleGroupEditFailure(@RequestParam(value = REQUEST_PARAMETER_ID) final String id) {

		ModelAndView modelAndView = new ModelAndView("group/editGroup");

		modelAndView.addObject(MODEL, restoreFromSession(SESSION_KEY_COMMAND));
		enrichBindingResultFromSession(MODEL, modelAndView);

		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String handleGroupUpdate(@Valid @ModelAttribute(MODEL) UpdateGroupCommand command, BindingResult bindingResult) {
		boolean isDuplicated = false;

		final RedirectBuilder redirect = new RedirectBuilder().setPath(CONTROLLER_PATH).addParameter(REQUEST_PARAMETER_ID, command.getId());

		try {
			if (!bindingResult.hasErrors()) {
				groupService.updateGroup(command.getId(), command.getAsUpdateGroup());

				redirect.addParameter("saveSuccess", true);
				redirect.setPath(GroupViewController.CONTROLLER_PATH);
				return redirect.build();
			}
		} catch (SCIMDataValidationException e) {
			LOG.warn("Could not update group.", e);
		} catch (ConflictException e) {
			// log the exception and throw no whitelabel page
			LOG.warn("Unable to update group. Duplicated data.", e);
			// duplicate parameter instead validation parameter
			isDuplicated = true;
		}

		//Set error parameter
		if(isDuplicated) {
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
