package org.osiam.addons.administration.controller.user;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.SCIMSearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is responsible for edit the group membership of an user
 */
@Controller
@RequestMapping(EditGroupMembershipController.CONTROLLER_PATH)
public class EditGroupMembershipController extends GenericController {

	public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/user/group";

	public static final String REQUEST_PARAMETER_ID = "id";

	public static final String MODEL_USER_GROUPS = "userGroups";
	public static final String MODEL_OTHER_GROUPS = "otherGroups";

	@Inject
	private GroupService groupService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleEditGroupMembership(
			@RequestParam(value = REQUEST_PARAMETER_ID) final String id) {

		ModelAndView modelAndView = new ModelAndView("user/groupMembership");

		SCIMSearchResult<Group> userGroups = groupService.getUserGroups(id);
		SCIMSearchResult<Group> otherGroups = groupService.getOtherGroups(id);

		modelAndView.addObject(MODEL_USER_GROUPS, userGroups);
		modelAndView.addObject(MODEL_OTHER_GROUPS, otherGroups);

		return modelAndView;
	}
}
