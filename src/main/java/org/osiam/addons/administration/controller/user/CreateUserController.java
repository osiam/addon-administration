package org.osiam.addons.administration.controller.user;

import javax.inject.Inject;
import javax.validation.Valid;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.command.CreateUserCommand;
import org.osiam.addons.administration.service.UserService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.client.exception.ConflictException;
import org.osiam.resources.exception.SCIMDataValidationException;
import org.osiam.resources.scim.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the user edit view.
 */
@Controller
@RequestMapping(CreateUserController.CONTROLLER_PATH)
public class CreateUserController extends GenericController {

	private static final Logger LOG = LoggerFactory.getLogger(CreateUserController.class);
	public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/user/create";

	public static final String REQUEST_PARAMETER_ERROR = "error";
	public static final String REQUEST_PARAMETER_ERROR_RESET_VALUES = "resetValues";

	public static final String MODEL = "model";

	private static final String SESSION_KEY_COMMAND = "command";

	@Inject
	private UserService userService;

	@Value("${org.osiam.administration.createUser.defaultActive:true}")
	private boolean defaultActive;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleCreateUser() {
		ModelAndView modelAndView = new ModelAndView("user/createUser");

		clearSession();

		modelAndView.addObject(MODEL, new CreateUserCommand());

		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, params = REQUEST_PARAMETER_ERROR_RESET_VALUES + "=true")
	public ModelAndView handleUserCreateFailure() {
		ModelAndView modelAndView = new ModelAndView("user/createUser");

		CreateUserCommand cmd = (CreateUserCommand)restoreFromSession(SESSION_KEY_COMMAND);

		modelAndView.addObject(MODEL, cmd);

		enrichBindingResultFromSession(MODEL, modelAndView);

		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String handleCreateGroup(
			@Valid @ModelAttribute(MODEL) CreateUserCommand command,
			BindingResult bindingResult) {

		boolean isDuplicated = false;
		final RedirectBuilder redirect = new RedirectBuilder()
				.setPath(CONTROLLER_PATH);

		try {
			if (!bindingResult.hasErrors()) {
				command.setActive(defaultActive);

				User createdUser = userService.createUser(command.getAsUser());

				redirect.setPath(EditUserController.CONTROLLER_PATH);
				redirect.addParameter("id", createdUser.getId());
				return redirect.build();
			}
		} catch (SCIMDataValidationException e) {
			LOG.warn("Could not create user.", e);
		} catch (ConflictException e) {
			LOG.warn("Could not create user.", e);
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

	private void clearSession() {
		removeFromSession(SESSION_KEY_COMMAND);
		removeBindingResultFromSession(MODEL);
	}
}
