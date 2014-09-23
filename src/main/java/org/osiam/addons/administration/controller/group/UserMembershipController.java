package org.osiam.addons.administration.controller.group;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.command.UpdateGroupCommand;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.addons.administration.service.UserService;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the create group view.
 */
@Controller
@RequestMapping(UserMembershipController.CONTROLLER_PATH)
public class UserMembershipController extends GenericController {

	public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/group/user";

	public static final String REQUEST_PARAMETER_ID = "id";
	public static final String REQUEST_PAREMETER_USER_ID = "userId";
	public static final String REQUEST_PARAMETER_ACTION = "action";
	private static final String SESSION_KEY_COMMAND = "command";

	public static final String MODEL = "model";
	public static final String MODEL_USER_LIST = "userList";

	@Inject
	private GroupService groupService;
	@Inject
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGroup(@RequestParam(value = REQUEST_PARAMETER_ID) final String id) {
		ModelAndView modelAndView = new ModelAndView("group/userMembership");

		clearSession();

		Group group = groupService.getGroup(id);
		Map<String, User> allUsers = getAllUsers();

		modelAndView.addObject(MODEL, new UpdateGroupCommand(group));
		modelAndView.addObject(MODEL_USER_LIST, allUsers);

		return modelAndView;
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=add")
	public ModelAndView handleAddUser(@RequestParam(value = REQUEST_PARAMETER_ID) String	id,
									@RequestParam(value = REQUEST_PAREMETER_USER_ID) String	userId) {
		ModelAndView modelAndView = new ModelAndView("group/userMembership");

		groupService.addUserToGroup(id, userId);

		clearSession();

		Group group = groupService.getGroup(id);
		Map<String, User> allUsers = getAllUsers();

		modelAndView.addObject(MODEL, new UpdateGroupCommand(group));
		modelAndView.addObject(MODEL_USER_LIST, allUsers);

		return modelAndView;
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=remove")
	public ModelAndView handleRemoveUser(@RequestParam(value = REQUEST_PARAMETER_ID) String	id,
									@RequestParam(value = REQUEST_PAREMETER_USER_ID) String	userId) {
		ModelAndView modelAndView = new ModelAndView("group/userMembership");

		groupService.removeUserFromGroup(id, userId);

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
		SCIMSearchResult<User> result = userService.searchUser("", 0, 0L, "userName", true, "id, userName, name.familyName, name.givenName");
		Map<String, User> idToUserMapping = new HashMap<String, User>();
		for (User user : result.getResources()) {
			idToUserMapping.put(user.getId(), user);
		}
		return idToUserMapping;
	}
}

