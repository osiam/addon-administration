package org.osiam.addons.administration.controller.group;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.addons.administration.service.UserService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;
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
	public static final String REQUEST_PAREMETER_USER_ID = "userId";
	public static final String REQUEST_PARAMETER_ACTION_PANEL = "panel";
	public static final String REQUEST_PARAMETER_ACTION_FILTER = "filter";

	public static final String MODEL_ASSIGNED_USERS = "assignedUsers";
	public static final String MODEL_UNASSIGNED_USERS = "unassignedUsers";
	public static final String MODEL_USER_LIST = "userList";

	@Inject
	private GroupService groupService;

	@Inject
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleEditUserMembership(@RequestParam(value = REQUEST_PARAMETER_ID) final String groupId) {
		ModelAndView modelAndView = new ModelAndView("group/userMembership");
		String attributes = "id, userName, name.givenName, name.familyName";

		SCIMSearchResult<User> assignedUsers = userService.getAssignedUsers(groupId, attributes);
		SCIMSearchResult<User> unassignedUsers = userService.getUnassignedUsers(groupId, attributes);

		modelAndView.addObject(MODEL_ASSIGNED_USERS, assignedUsers);
		modelAndView.addObject(MODEL_UNASSIGNED_USERS, unassignedUsers);

		return modelAndView;
	}

	@RequestMapping(params=REQUEST_PARAMETER_ACTION_PANEL + "=add")
	public String handleAddUser(@RequestParam(value = REQUEST_PARAMETER_ID) String groupId,
								@RequestParam(value = REQUEST_PAREMETER_USER_ID, required = false) String[] userIds) {

		groupService.addUsersToGroup(groupId, userIds);

		return new RedirectBuilder()
						.setPath(CONTROLLER_PATH)
						.addParameter(REQUEST_PARAMETER_ID, groupId)
					.build();
	}

	@RequestMapping(params=REQUEST_PARAMETER_ACTION_PANEL + "=remove")
	public String handleRemoveUser(@RequestParam(value = REQUEST_PARAMETER_ID) String	groupId,
									@RequestParam(value = REQUEST_PAREMETER_USER_ID, required = false) String[] userIds) {

		groupService.removeUsersFromGroup(groupId, userIds);

		return new RedirectBuilder()
						.setPath(CONTROLLER_PATH)
						.addParameter(REQUEST_PARAMETER_ID, groupId)
					.build();
	}
}
