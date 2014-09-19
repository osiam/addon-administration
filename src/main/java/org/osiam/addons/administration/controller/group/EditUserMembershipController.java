package org.osiam.addons.administration.controller.group;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *  This controller is responsible for edit the user membership of an group
 */
@Controller
@RequestMapping(EditUserMembershipController.CONTROLLER_PATH)
public class EditUserMembershipController extends GenericController {

	public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/group/user";

	public static final String REQUEST_PARAMETER_ID = "id";

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleEdituserMembership(
	        @RequestParam(value = REQUEST_PARAMETER_ID) final String id) {

		ModelAndView modelAndView = new ModelAndView("group/userMembership");

		return modelAndView;
	}
}
