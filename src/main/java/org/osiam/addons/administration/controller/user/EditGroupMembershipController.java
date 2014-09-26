package org.osiam.addons.administration.controller.user;

import java.util.Map;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.session.GroupMembershipSession;
import org.osiam.addons.administration.service.GroupService;
import org.osiam.addons.administration.util.RedirectBuilder;
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

	public static final String REQUEST_PARAMETER_USER_ID = "id";
	public static final String REQUEST_PARAMETER_GROUP_ID = "groupId";
	public static final String REQUEST_PARAMETER_ACTION = "action";
	public static final String REQUEST_PARAMETER_PANEL = "panel";

	public static final String REQUEST_PARAMETER_ASSIGNED_QUERY = "assignedQuery";

	public static final String REQUEST_PARAMETER_UNASSIGNED_QUERY = "unassignedQuery";

	public static final String REQUEST_PARAMETER_QUERY_PREFIX = "query.";

	public static final String MODEL_ASSIGNED_GROUPS = "assignedGroups";
	public static final String MODEL_UNASSIGNED_GROUPS = "unassignedGroups";
	public static final String MODEL_PAGING_INFORMATION_ASSIGNED_USERS = "pagingInformationAssignedGroups";
	public static final String MODEL_PAGING_INFORMATION_UNASSIGNED_USERS = "pagingInformationUnassignedGroups";

	@Inject
	private GroupService groupService;

	@Inject
	private GroupMembershipSession session;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleEditGroupMembership(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) final String userId,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_QUERY, required = false) String assignedQuery,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_QUERY, required = false) String unassignedQuery){

		ModelAndView modelAndView = new ModelAndView("user/groupMembership");

		session.getAssignedGroupsPagingInformation().setQuery(assignedQuery);

		session.getUnassignedGroupsPagingInformation().setQuery(unassignedQuery);

		SCIMSearchResult<Group> assignedGroups = groupService.getAssignedGroups(userId,
				session.getAssignedGroupsPagingInformation());
		SCIMSearchResult<Group> unassignedGroups = groupService.getUnassignedGroups(userId,
				session.getUnassignedGroupsPagingInformation());

		modelAndView.addObject(MODEL_ASSIGNED_GROUPS, assignedGroups);
		modelAndView.addObject(MODEL_UNASSIGNED_GROUPS, unassignedGroups);

		modelAndView.addObject(MODEL_PAGING_INFORMATION_ASSIGNED_USERS, session.getAssignedGroupsPagingInformation());
		modelAndView.addObject(MODEL_PAGING_INFORMATION_UNASSIGNED_USERS, session.getUnassignedGroupsPagingInformation());

		return modelAndView;
	}

	@RequestMapping(params = REQUEST_PARAMETER_PANEL + "=add")
	public String handleAddGroup(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) final String userId,
			@RequestParam(value = REQUEST_PARAMETER_GROUP_ID, required = false) final String[] groupIds){

		groupService.addUserToGroups(userId, groupIds);

		return new RedirectBuilder()
					.setPath(CONTROLLER_PATH)
					.addParameter(REQUEST_PARAMETER_USER_ID, userId)
				.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_PANEL + "=remove")
	public String handleRemoveGroup(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) final String userId,
			@RequestParam(value = REQUEST_PARAMETER_GROUP_ID, required = false) final String[] groupIds){

		groupService.removeUserFromGroups(userId, groupIds);

		return new RedirectBuilder()
					.setPath(CONTROLLER_PATH)
					.addParameter(REQUEST_PARAMETER_USER_ID, userId)
				.build();
	}

	@RequestMapping(params={REQUEST_PARAMETER_PANEL + "=add", REQUEST_PARAMETER_ACTION + "=filter"})
	public String handleFilterUserAdd(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) String userId,
			@RequestParam Map<String, String> allParameters) {

		Map<String, String> filterParameter = extractFilterParameter(allParameters);
		String filterQuery = buildFilterQuery(filterParameter);

		session.getUnassignedGroupsPagingInformation().setFilterFields(filterParameter);

		return new RedirectBuilder()
					.setPath(CONTROLLER_PATH)
					.addParameter(REQUEST_PARAMETER_USER_ID, userId)
					.addParameter(REQUEST_PARAMETER_ASSIGNED_QUERY, session.getAssignedGroupsPagingInformation().getQuery())
					.addParameter(REQUEST_PARAMETER_UNASSIGNED_QUERY, filterQuery)
				.build();
	}

	@RequestMapping(params={REQUEST_PARAMETER_PANEL + "=remove", REQUEST_PARAMETER_ACTION + "=filter"})
	public String handleFilterUserRemove(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) String userId,
			@RequestParam Map<String, String> allParameters) {

		Map<String, String> filterParameter = extractFilterParameter(allParameters);
		String filterQuery = buildFilterQuery(filterParameter);

		session.getAssignedGroupsPagingInformation().setFilterFields(filterParameter);

		return new RedirectBuilder()
					.setPath(CONTROLLER_PATH)
					.addParameter(REQUEST_PARAMETER_USER_ID, userId)
					.addParameter(REQUEST_PARAMETER_ASSIGNED_QUERY, filterQuery)
					.addParameter(REQUEST_PARAMETER_UNASSIGNED_QUERY, session.getUnassignedGroupsPagingInformation().getQuery())
				.build();
	}
}
