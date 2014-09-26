package org.osiam.addons.administration.controller.group;

import java.util.Map;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.session.UserMembershipSession;
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
	public static final String REQUEST_PARAMETER_ACTION = "action";
	public static final String REQUEST_PARAMETER_PANEL = "panel";

	public static final String REQUEST_PARAMETER_ORDER_BY = "orderBy";
	public static final String REQUEST_PARAMETER_ASCENDING = "asc";
	public static final String REQUEST_PARAMETER_QUERY_PREFIX = "query.";
	public static final String REQUEST_PARAMETER_LIMIT = "limit";
	public static final String REQUEST_PARAMETER_OFFSET = "offset";
	public static final String REQUEST_PARAMETER_QUERY = "query";

	public static final String MODEL_ASSIGNED_USERS = "assignedUsers";
	public static final String MODEL_UNASSIGNED_USERS = "unassignedUsers";
	public static final String MODEL_ADD_PANEL_PAGING_INFORMATIONS = "addPanelPagingInformations";
	public static final String MODEL_REMOVE_PANEL_PAGING_INFORMATIONS = "removePanelPagingInformations";
	public static final String MODEL_USER_LIST = "userList";

	private static final Integer DEFAULT_LIMIT = 20;
	private static final String DEFAULT_SORT_BY = "userName";
	private static final Boolean DEFAULT_SORT_DIRECTION = true;

	@Inject
	private GroupService groupService;

	@Inject
	private UserService userService;

	@Inject
	private UserMembershipSession session;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleEditUserMembership(@RequestParam(value = REQUEST_PARAMETER_ID) final String groupId,
												@RequestParam(value = REQUEST_PARAMETER_QUERY, required = false) String query,
												@RequestParam(value = REQUEST_PARAMETER_LIMIT, required = false) Integer limit,
												@RequestParam(value = REQUEST_PARAMETER_OFFSET, required = false) Long offset,
												@RequestParam(value = REQUEST_PARAMETER_ORDER_BY, required = false) String orderBy,
												@RequestParam(value = REQUEST_PARAMETER_ASCENDING, required = false) Boolean ascending) {

		ModelAndView modelAndView = new ModelAndView("group/userMembership");
		String attributes = "id, userName, name.givenName, name.familyName";

		limit = limit == null ? DEFAULT_LIMIT : limit;
		orderBy = orderBy == null ? DEFAULT_SORT_BY : orderBy;
		ascending = ascending == null ? DEFAULT_SORT_DIRECTION : ascending;

		session.getRemovePanelPagingInformation().setLimit(limit);
		session.getRemovePanelPagingInformation().setOrderBy(orderBy);
		session.getRemovePanelPagingInformation().setAscending(ascending);
		session.getRemovePanelPagingInformation().setQuery(query);

		session.getAddPanelPagingInformation().setLimit(limit);
		session.getAddPanelPagingInformation().setOrderBy(orderBy);
		session.getAddPanelPagingInformation().setAscending(ascending);


		SCIMSearchResult<User> assignedUsers = userService.getAssignedUsers(groupId,
													session.getRemovePanelPagingInformation(),
													attributes);

		SCIMSearchResult<User> unassignedUsers = userService.getUnassignedUsers(groupId,
													session.getAddPanelPagingInformation(),
													attributes);

		modelAndView.addObject(MODEL_ASSIGNED_USERS, assignedUsers);
		modelAndView.addObject(MODEL_UNASSIGNED_USERS, unassignedUsers);

		modelAndView.addObject(MODEL_ADD_PANEL_PAGING_INFORMATIONS, session.getAddPanelPagingInformation());
		modelAndView.addObject(MODEL_REMOVE_PANEL_PAGING_INFORMATIONS, session.getRemovePanelPagingInformation());

		return modelAndView;
	}

	@RequestMapping(params=REQUEST_PARAMETER_PANEL + "=add")
	public String handleAddUser(@RequestParam(value = REQUEST_PARAMETER_ID) String groupId,
								@RequestParam(value = REQUEST_PAREMETER_USER_ID, required = false) String[] userIds) {

		groupService.addUsersToGroup(groupId, userIds);

		return new RedirectBuilder()
						.setPath(CONTROLLER_PATH)
						.addParameter(REQUEST_PARAMETER_ID, groupId)
					.build();
	}

	@RequestMapping(params=REQUEST_PARAMETER_PANEL + "=remove")
	public String handleRemoveUser(@RequestParam(value = REQUEST_PARAMETER_ID) String	groupId,
									@RequestParam(value = REQUEST_PAREMETER_USER_ID, required = false) String[] userIds) {

		groupService.removeUsersFromGroup(groupId, userIds);

		return new RedirectBuilder()
						.setPath(CONTROLLER_PATH)
						.addParameter(REQUEST_PARAMETER_ID, groupId)
					.build();
	}

	@RequestMapping(params={REQUEST_PARAMETER_PANEL + "=remove", REQUEST_PARAMETER_ACTION + "=filter"})
	public String handleFilterUserRemove(@RequestParam(value = REQUEST_PARAMETER_ID) String	groupId,
										@RequestParam Map<String, String> allParameters) {

		Map<String, String> filterParameter = extractFilterParameter(allParameters);
		String filterQuery = buildFilterQuery(filterParameter);

		session.getRemovePanelPagingInformation().setFilterFields(filterParameter);


			return new RedirectBuilder()
						.setPath(CONTROLLER_PATH)
						.addParameter(REQUEST_PARAMETER_ID, groupId)
						.addParameter(REQUEST_PARAMETER_QUERY, filterQuery)
						.addParameter(REQUEST_PARAMETER_LIMIT, session.getRemovePanelPagingInformation().getLimit())
						.addParameter(REQUEST_PARAMETER_OFFSET, null)
						.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getRemovePanelPagingInformation().getOrderBy())
						.addParameter(REQUEST_PARAMETER_ASCENDING, session.getRemovePanelPagingInformation().getAscending())
					.build();
	}

	@RequestMapping(params={REQUEST_PARAMETER_PANEL + "=remove", REQUEST_PARAMETER_ACTION + "=sort"})
	public String handleSortUserRemove(@RequestParam(value = REQUEST_PARAMETER_ID) String	groupId,
									@RequestParam(value = REQUEST_PARAMETER_ORDER_BY) String orderBy,
									@RequestParam(value = REQUEST_PARAMETER_ASCENDING) Boolean ascending) {

		return new RedirectBuilder()
						.setPath(CONTROLLER_PATH)
						.addParameter(REQUEST_PARAMETER_ID, groupId)
					.build();
	}
}
