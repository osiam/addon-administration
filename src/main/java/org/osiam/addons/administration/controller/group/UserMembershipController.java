package org.osiam.addons.administration.controller.group;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the create group view.
 */
@Controller
@RequestMapping(UserMembershipController.CONTROLLER_PATH)
public class UserMembershipController extends GenericController {

	public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/group/user";


	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleCreateGroup() {
		ModelAndView modelAndView = new ModelAndView("group/userMembership");

		return modelAndView;
	}
}
