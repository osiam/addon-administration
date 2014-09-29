package org.osiam.addons.administration.controller.user;

import java.util.Map;

import javax.inject.Inject;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.GenericController;
import org.osiam.addons.administration.model.session.GroupMembershipSession;
import org.osiam.addons.administration.model.session.PagingInformation;
import org.osiam.addons.administration.paging.PagingBuilder;
import org.osiam.addons.administration.paging.PagingLinks;
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
	public static final String REQUEST_PARAMETER_ASSIGNED_OFFSET = "assignedOffset";
	public static final String REQUEST_PARAMETER_ASSIGNED_LIMIT = "assignedLimit";
	public static final String REQUEST_PARAMETER_ASSIGNED_ORDER_BY = "assignedOrderBy";
	public static final String REQUEST_PARAMETER_ASSIGNED_ASCENDING = "assignedAsc";

	public static final String REQUEST_PARAMETER_UNASSIGNED_QUERY = "unassignedQuery";
	public static final String REQUEST_PARAMETER_UNASSIGNED_OFFSET = "unassignedOffset";
	public static final String REQUEST_PARAMETER_UNASSIGNED_LIMIT = "unassignedLimit";
	public static final String REQUEST_PARAMETER_UNASSIGNED_ORDER_BY = "unassignedOrderBy";
	public static final String REQUEST_PARAMETER_UNASSIGNED_ASCENDING = "unassignedAsc";

	public static final String REQUEST_PARAMETER_QUERY_PREFIX = "query.";

	public static final String MODEL_ASSIGNED_GROUPS = "assignedGroups";
	public static final String MODEL_UNASSIGNED_GROUPS = "unassignedGroups";
	public static final String MODEL_PAGING_LINKS_ASSIGNED_GROUPS = "pagingAssignedGroups";
	public static final String MODEL_PAGING_LINKS_UNASSIGNED_GROUPS = "pagingUnassignedGroups";
	public static final String MODEL_PAGING_INFORMATION_ASSIGNED_GROUPS = "pagingInformationAssignedGroups";
	public static final String MODEL_PAGING_INFORMATION_UNASSIGNED_GROUPS = "pagingInformationUnassignedGroups";

	private static final Integer DEFAULT_LIMIT = 10;
	private static final String DEFAULT_SORT_BY = "displayName";
	private static final Boolean DEFAULT_SORT_DIRECTION = true;

	@Inject
	private GroupService groupService;

	@Inject
	private GroupMembershipSession session;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleEditGroupMembership(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) final String userId,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_QUERY, required = false) String assignedQuery,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_LIMIT, required = false) Integer assignedLimit,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_OFFSET, required = false) Long assignedOffset,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_ORDER_BY, required = false) String assignedOrderBy,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_ASCENDING, required = false) Boolean assignedAscending,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_QUERY, required = false) String unassignedQuery,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_LIMIT, required = false) Integer unassignedLimit,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_OFFSET, required = false) Long unassignedOffset,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_ORDER_BY, required = false) String unassignedOrderBy,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_ASCENDING, required = false) Boolean unassignedAscending){

		ModelAndView modelAndView = new ModelAndView("user/groupMembership");

		assignedLimit = assignedLimit == null ? DEFAULT_LIMIT : assignedLimit;
		assignedOrderBy = assignedOrderBy == null ? DEFAULT_SORT_BY : assignedOrderBy;
		assignedAscending = assignedAscending == null ? DEFAULT_SORT_DIRECTION : assignedAscending;

		unassignedLimit = unassignedLimit == null ? DEFAULT_LIMIT : unassignedLimit;
		unassignedOrderBy = unassignedOrderBy == null ? DEFAULT_SORT_BY : unassignedOrderBy;
		unassignedAscending = unassignedAscending == null ? DEFAULT_SORT_DIRECTION : unassignedAscending;

		session.getAssignedGroupsPagingInformation().setOffset(assignedOffset);
		session.getAssignedGroupsPagingInformation().setLimit(assignedLimit);
		session.getAssignedGroupsPagingInformation().setOrderBy(assignedOrderBy);
		session.getAssignedGroupsPagingInformation().setAscending(assignedAscending);
		session.getAssignedGroupsPagingInformation().setQuery(assignedQuery);

		session.getUnassignedGroupsPagingInformation().setOffset(unassignedOffset);
		session.getUnassignedGroupsPagingInformation().setLimit(unassignedLimit);
		session.getUnassignedGroupsPagingInformation().setOrderBy(unassignedOrderBy);
		session.getUnassignedGroupsPagingInformation().setAscending(unassignedAscending);
		session.getUnassignedGroupsPagingInformation().setQuery(unassignedQuery);

		SCIMSearchResult<Group> assignedGroups = groupService.getAssignedGroups(userId,
				session.getAssignedGroupsPagingInformation());
		SCIMSearchResult<Group> unassignedGroups = groupService.getUnassignedGroups(userId,
				session.getUnassignedGroupsPagingInformation());

		modelAndView.addObject(MODEL_ASSIGNED_GROUPS, assignedGroups);
		modelAndView.addObject(MODEL_UNASSIGNED_GROUPS, unassignedGroups);

		PagingLinks pagingLinksAssigned = generatePagingLinksForAssigned(userId, assignedGroups,
				session.getAssignedGroupsPagingInformation(), session.getUnassignedGroupsPagingInformation());

		PagingLinks pagingLinksUnassigned = generatePagingLinksForUnassigned(userId, unassignedGroups,
				session.getUnassignedGroupsPagingInformation(), session.getAssignedGroupsPagingInformation());

		modelAndView.addObject(MODEL_PAGING_LINKS_ASSIGNED_GROUPS, pagingLinksAssigned);
		modelAndView.addObject(MODEL_PAGING_LINKS_UNASSIGNED_GROUPS, pagingLinksUnassigned);

		modelAndView.addObject(MODEL_PAGING_INFORMATION_ASSIGNED_GROUPS, session.getAssignedGroupsPagingInformation());
		modelAndView.addObject(MODEL_PAGING_INFORMATION_UNASSIGNED_GROUPS, session.getUnassignedGroupsPagingInformation());

		return modelAndView;
	}

	private PagingLinks generatePagingLinksForAssigned(
			String userId,
			SCIMSearchResult<Group> groupList,
			PagingInformation assignedPagingInformation,
			PagingInformation unassignedPagingInformation) {

		PagingBuilder builder = new PagingBuilder()
				.setBaseUrl("")
				.setStartIndex(1L)
				// SCIMResult begins with 1!
				.setOffsetParameter(REQUEST_PARAMETER_ASSIGNED_OFFSET)
				.setOffset(groupList.getStartIndex())
				.setLimitParameter(REQUEST_PARAMETER_ASSIGNED_LIMIT)
				.setLimit(groupList.getItemsPerPage())
				.setTotal(groupList.getTotalResults())
				.addParameter(REQUEST_PARAMETER_USER_ID, userId);

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
			String userId,
			SCIMSearchResult<Group> groupList,
			PagingInformation unassignedPagingInformation,
			PagingInformation assignedPagingInformation) {

		PagingBuilder builder = new PagingBuilder()
				.setBaseUrl("")
				.setStartIndex(1L)
				// SCIMResult begins with 1!
				.setOffsetParameter(REQUEST_PARAMETER_UNASSIGNED_OFFSET)
				.setOffset(groupList.getStartIndex())
				.setLimitParameter(REQUEST_PARAMETER_UNASSIGNED_LIMIT)
				.setLimit(groupList.getItemsPerPage())
				.setTotal(groupList.getTotalResults())
				.addParameter(REQUEST_PARAMETER_USER_ID, userId);

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

		return defaultRedirect(userId)
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

		return defaultRedirect(userId)
				.addParameter(REQUEST_PARAMETER_ASSIGNED_QUERY, filterQuery)
			.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=assignedLimit")
	public String handleAssignedUserLimitAction(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) String	userId,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_LIMIT) Integer limit) {

		return defaultRedirect(userId)
				.addParameter(REQUEST_PARAMETER_ASSIGNED_LIMIT, limit)
				.addParameter(REQUEST_PARAMETER_ASSIGNED_OFFSET, null)
			.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=unassignedLimit")
	public String handleUnassignedUserLimitAction(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) String	userId,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_LIMIT) Integer limit) {

		return defaultRedirect(userId)
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_LIMIT, limit)
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_OFFSET, null)
			.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=assignedSort")
	public String handleAssignedUserSortAction(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) String	groupId,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_ORDER_BY) String orderBy,
			@RequestParam(value = REQUEST_PARAMETER_ASSIGNED_ASCENDING) Boolean ascending) {

		return defaultRedirect(groupId)
					.addParameter(REQUEST_PARAMETER_ASSIGNED_ORDER_BY, orderBy)
					.addParameter(REQUEST_PARAMETER_ASSIGNED_ASCENDING, ascending)
				.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=unassignedSort")
	public String handleUnassignedUserSortAction(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) String	groupId,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_ORDER_BY) String orderBy,
			@RequestParam(value = REQUEST_PARAMETER_UNASSIGNED_ASCENDING) Boolean ascending) {

		return defaultRedirect(groupId)
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_ORDER_BY, orderBy)
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_ASCENDING, ascending)
			.build();
	}

	private RedirectBuilder defaultRedirect(String userId) {
		return new RedirectBuilder().setPath(CONTROLLER_PATH)
				.addParameter(REQUEST_PARAMETER_USER_ID, userId)
				.addParameter(REQUEST_PARAMETER_ASSIGNED_QUERY, session.getAssignedGroupsPagingInformation().getQuery())
				.addParameter(REQUEST_PARAMETER_ASSIGNED_LIMIT, session.getAssignedGroupsPagingInformation().getLimit())
				.addParameter(REQUEST_PARAMETER_ASSIGNED_OFFSET, session.getAssignedGroupsPagingInformation().getOffset())
				.addParameter(REQUEST_PARAMETER_ASSIGNED_ORDER_BY, session.getAssignedGroupsPagingInformation().getOrderBy())
				.addParameter(REQUEST_PARAMETER_ASSIGNED_ASCENDING, session.getAssignedGroupsPagingInformation().getAscending())
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_QUERY, session.getUnassignedGroupsPagingInformation().getQuery())
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_LIMIT, session.getUnassignedGroupsPagingInformation().getLimit())
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_OFFSET, session.getUnassignedGroupsPagingInformation().getOffset())
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_ORDER_BY, session.getUnassignedGroupsPagingInformation().getOrderBy())
				.addParameter(REQUEST_PARAMETER_UNASSIGNED_ASCENDING, session.getUnassignedGroupsPagingInformation().getAscending());
	}
}
