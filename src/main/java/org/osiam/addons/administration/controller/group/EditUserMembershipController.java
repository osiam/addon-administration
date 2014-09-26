package org.osiam.addons.administration.controller.group;

import java.util.Map;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.session.PagingInformation;
import org.osiam.addons.administration.model.session.UserMembershipSession;
import org.osiam.addons.administration.paging.PagingBuilder;
import org.osiam.addons.administration.paging.PagingLinks;
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

	public static final String REQUEST_PARAMETER_ASSIGNED_ORDER_BY = "assignedOrderBy";
	public static final String REQUEST_PARAMETER_ASSIGNED_ASCENDING = "assignedAsc";
	public static final String REQUEST_PARAMETER_ASSIGNED_LIMIT = "assignedLimit";
	public static final String REQUEST_PARAMETER_ASSIGNED_OFFSET = "assignedOffset";
	public static final String REQUEST_PARAMETER_ASSIGNED_QUERY = "assignedQuery";

	public static final String REQUEST_PARAMETER_UNASSIGNED_ORDER_BY = "unassignedOrderBy";
	public static final String REQUEST_PARAMETER_UNASSIGNED_ASCENDING = "unassignedAsc";
	public static final String REQUEST_PARAMETER_UNASSIGNED_LIMIT = "unassignedLimit";
	public static final String REQUEST_PARAMETER_UNASSIGNED_OFFSET = "unassignedOffset";
	public static final String REQUEST_PARAMETER_UNASSIGNED_QUERY = "unassignedQuery";

	public static final String REQUEST_PARAMETER_QUERY_PREFIX = "query.";

	public static final String MODEL_ASSIGNED_USERS = "assignedUsers";
	public static final String MODEL_UNASSIGNED_USERS = "unassignedUsers";
	public static final String MODEL_ADD_PANEL_PAGING_INFORMATIONS = "addPanelPagingInformations";
	public static final String MODEL_REMOVE_PANEL_PAGING_INFORMATIONS = "removePanelPagingInformations";
	public static final String MODEL_USER_LIST = "userList";
	public static final String MODEL_PAGING_LINKS_ASSIGNED_USERS = "pagingAssignedUsers";
	public static final String MODEL_PAGING_LINKS_UNASSIGNED_USERS = "pagingUnassignedUsers";

	private static final Integer DEFAULT_LIMIT = 5;
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
												@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_QUERY, required = false) String assignedQuery,
												@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_LIMIT, required = false) Integer assignedLimit,
												@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_OFFSET, required = false) Long assignedOffset,
												@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_ORDER_BY, required = false) String assignedOrderBy,
												@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_ASCENDING, required = false) Boolean assignedAscending,
												@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_QUERY, required = false) String unassignedQuery,
												@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_LIMIT, required = false) Integer unassignedLimit,
												@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_OFFSET, required = false) Long unassignedOffset,
												@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_ORDER_BY, required = false) String unassignedOrderBy,
												@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_ASCENDING, required = false) Boolean unassignedAscending) {

		ModelAndView modelAndView = new ModelAndView("group/userMembership");
		String attributes = "id, userName, name.givenName, name.familyName";

		assignedLimit = assignedLimit == null ? DEFAULT_LIMIT : assignedLimit;
		assignedOrderBy = assignedOrderBy == null ? DEFAULT_SORT_BY : assignedOrderBy;
		assignedAscending = assignedAscending == null ? DEFAULT_SORT_DIRECTION : assignedAscending;

		unassignedLimit = unassignedLimit == null ? DEFAULT_LIMIT : unassignedLimit;
		unassignedOrderBy = unassignedOrderBy == null ? DEFAULT_SORT_BY : unassignedOrderBy;
		unassignedAscending = unassignedAscending == null ? DEFAULT_SORT_DIRECTION : unassignedAscending;

		session.getRemovePanelPagingInformation().setOffset(assignedOffset);
		session.getRemovePanelPagingInformation().setLimit(assignedLimit);
		session.getRemovePanelPagingInformation().setOrderBy(assignedOrderBy);
		session.getRemovePanelPagingInformation().setAscending(assignedAscending);
		session.getRemovePanelPagingInformation().setQuery(assignedQuery);

		session.getAddPanelPagingInformation().setOffset(unassignedOffset);
		session.getAddPanelPagingInformation().setLimit(unassignedLimit);
		session.getAddPanelPagingInformation().setOrderBy(unassignedOrderBy);
		session.getAddPanelPagingInformation().setAscending(unassignedAscending);
		session.getAddPanelPagingInformation().setQuery(unassignedQuery);

		SCIMSearchResult<User> assignedUsers = userService.getAssignedUsers(groupId,
													session.getRemovePanelPagingInformation(),
													attributes);

		SCIMSearchResult<User> unassignedUsers = userService.getAssignedUsers(groupId,	//TODO: testweise auf assigned gesetzt!
													session.getAddPanelPagingInformation(),
													attributes);

		modelAndView.addObject(MODEL_ASSIGNED_USERS, assignedUsers);
		modelAndView.addObject(MODEL_UNASSIGNED_USERS, unassignedUsers);

		modelAndView.addObject(MODEL_ADD_PANEL_PAGING_INFORMATIONS, session.getAddPanelPagingInformation());
		modelAndView.addObject(MODEL_REMOVE_PANEL_PAGING_INFORMATIONS, session.getRemovePanelPagingInformation());

		PagingLinks pagingLinksAssigned = generatePagingLinksForAssigned(groupId, assignedUsers,
				session.getRemovePanelPagingInformation(), session.getAddPanelPagingInformation());
		PagingLinks pagingLinksUnassigned = generatePagingLinksForUnassigned(groupId, unassignedUsers,
				session.getAddPanelPagingInformation(), session.getRemovePanelPagingInformation());

		modelAndView.addObject(MODEL_PAGING_LINKS_ASSIGNED_USERS, pagingLinksAssigned);
		modelAndView.addObject(MODEL_PAGING_LINKS_UNASSIGNED_USERS, pagingLinksUnassigned);

		return modelAndView;
	}

	private PagingLinks generatePagingLinksForAssigned(
			String groupId,
			SCIMSearchResult<User> userList,
			PagingInformation assignedPagingInformation,
			PagingInformation unassignedPagingInformation) {

		PagingBuilder builder = new PagingBuilder()
				.setBaseUrl("")
				.setStartIndex(1L)
				// SCIMResult begins with 1!
				.setOffsetParameter(REQUEST_PARAMETER_ASSIGNED_OFFSET)
				.setOffset(userList.getStartIndex())
				.setLimitParameter(REQUEST_PARAMETER_ASSIGNED_LIMIT)
				.setLimit(userList.getItemsPerPage())
				.setTotal(userList.getTotalResults())
				.addParameter(REQUEST_PARAMETER_ID, groupId);

		if(unassignedPagingInformation.getOffset() != null){
			builder.addParameter(
					REQUEST_PARAMETER_UNASSIGNED_OFFSET,
					unassignedPagingInformation.getOffset());
		}
		if(unassignedPagingInformation.getLimit() != null){
			builder.addParameter(
					REQUEST_PARAMETER_UNASSIGNED_LIMIT,
					unassignedPagingInformation.getLimit());
		}

		addAssignedPagingParameter(assignedPagingInformation, builder);
		addUnassignedPagingParameter(unassignedPagingInformation, builder);

		return builder.build();
	}

	private PagingLinks generatePagingLinksForUnassigned(
			String groupId,
			SCIMSearchResult<User> userList,
			PagingInformation unassignedPagingInformation,
			PagingInformation assignedPagingInformation) {

		PagingBuilder builder = new PagingBuilder()
				.setBaseUrl("")
				.setStartIndex(1L)
				// SCIMResult begins with 1!
				.setOffsetParameter(REQUEST_PARAMETER_UNASSIGNED_OFFSET)
				.setOffset(userList.getStartIndex())
				.setLimitParameter(REQUEST_PARAMETER_UNASSIGNED_LIMIT)
				.setLimit(userList.getItemsPerPage())
				.setTotal(userList.getTotalResults())
				.addParameter(REQUEST_PARAMETER_ID, groupId);

		if(assignedPagingInformation.getOffset() != null){
			builder.addParameter(
					REQUEST_PARAMETER_ASSIGNED_OFFSET,
					assignedPagingInformation.getOffset());
		}
		if(assignedPagingInformation.getLimit() != null){
			builder.addParameter(
					REQUEST_PARAMETER_ASSIGNED_LIMIT,
					assignedPagingInformation.getLimit());
		}

		addUnassignedPagingParameter(unassignedPagingInformation, builder);
		addAssignedPagingParameter(assignedPagingInformation, builder);

		return builder.build();
	}

	private void addAssignedPagingParameter(
			PagingInformation pagingInformation, PagingBuilder builder) {

		if (pagingInformation.getQuery() != null) {
			builder.addParameter(REQUEST_PARAMETER_ASSIGNED_QUERY, pagingInformation.getQuery());
		}
		if (pagingInformation.getOrderBy() != null) {
			builder.addParameter(REQUEST_PARAMETER_ASSIGNED_ORDER_BY, pagingInformation.getOrderBy());
		}
		if (pagingInformation.getAscending() != null) {
			builder.addParameter(REQUEST_PARAMETER_ASSIGNED_ASCENDING, pagingInformation.getAscending());
		}
	}

	private void addUnassignedPagingParameter(
			PagingInformation pagingInformation, PagingBuilder builder) {

		if (pagingInformation.getQuery() != null) {
			builder.addParameter(REQUEST_PARAMETER_UNASSIGNED_QUERY,
					pagingInformation.getQuery());
		}
		if (pagingInformation.getOrderBy() != null) {
			builder.addParameter(REQUEST_PARAMETER_UNASSIGNED_ORDER_BY,
					pagingInformation.getOrderBy());
		}
		if (pagingInformation.getAscending() != null) {
			builder.addParameter(REQUEST_PARAMETER_UNASSIGNED_ASCENDING,
					pagingInformation.getAscending());
		}
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
					.addParameter(REQUEST_PARAMETER_ASSIGNED_QUERY, filterQuery)
					.addParameter(REQUEST_PARAMETER_ASSIGNED_LIMIT, session.getRemovePanelPagingInformation().getLimit())
					.addParameter(REQUEST_PARAMETER_ASSIGNED_OFFSET, null)
					.addParameter(REQUEST_PARAMETER_ASSIGNED_ORDER_BY, session.getRemovePanelPagingInformation().getOrderBy())
					.addParameter(REQUEST_PARAMETER_ASSIGNED_ASCENDING, session.getRemovePanelPagingInformation().getAscending())
					.addParameter(REQUEST_PARAMETER_UNASSIGNED_QUERY, session.getAddPanelPagingInformation().getQuery())
					.addParameter(REQUEST_PARAMETER_UNASSIGNED_LIMIT, session.getAddPanelPagingInformation().getLimit())
					.addParameter(REQUEST_PARAMETER_UNASSIGNED_OFFSET, null)
					.addParameter(REQUEST_PARAMETER_UNASSIGNED_ORDER_BY, session.getAddPanelPagingInformation().getOrderBy())
					.addParameter(REQUEST_PARAMETER_UNASSIGNED_ASCENDING, session.getAddPanelPagingInformation().getAscending())
				.build();
	}

	@RequestMapping(params={REQUEST_PARAMETER_PANEL + "=remove", REQUEST_PARAMETER_ACTION + "=sort"})
	public String handleSortUserRemove(@RequestParam(value = REQUEST_PARAMETER_ID) String	groupId,
									@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_ORDER_BY) String orderBy,
									@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_ASCENDING) Boolean ascending) {

		return new RedirectBuilder()
						.setPath(CONTROLLER_PATH)
						.addParameter(REQUEST_PARAMETER_ID, groupId)
					.build();
	}
}
