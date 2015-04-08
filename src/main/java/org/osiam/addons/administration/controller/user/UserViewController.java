package org.osiam.addons.administration.controller.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.mail.EmailSender;
import org.osiam.addons.administration.model.session.UserlistSession;
import org.osiam.addons.administration.paging.PagingBuilder;
import org.osiam.addons.administration.paging.PagingLinks;
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
 * This controller is responsible for handling user list. This is the main page of the "userview".
 */
@Controller
@RequestMapping(UserViewController.CONTROLLER_PATH)
public class UserViewController {

	public static final String CONTROLLER_PATH = AdminController.CONTROLLER_PATH + "/user/list";

	public static final String REQUEST_PARAMETER_ACTION = "action";
	public static final String REQUEST_PARAMETER_QUERY = "query";
	public static final String REQUEST_PARAMETER_LIMIT = "limit";
	public static final String REQUEST_PARAMETER_OFFSET = "offset";
	public static final String REQUEST_PARAMETER_ORDER_BY = "orderBy";
	public static final String REQUEST_PARAMETER_ASCENDING = "asc";
	public static final String REQUEST_PARAMETER_QUERY_PREFIX = "query.";
	public static final String REQUEST_PARAMETER_USER_ID = "id";
	public static final String REQUEST_PARAMETER_USER_ID_ARRAY = "idArray";
	public static final String REQUEST_PARAMETER_SEND_MAIL = "sendMail";

	public static final String REQUEST_PARAMETER_DELETE_SUCCESS = "deleteSuccess";
	public static final String REQUEST_PARAMETER_DELETE_SELECTED_SUCCESS = "deleteSelectedSuccess";
	public static final String REQUEST_PARAMETER_ACTIVATE_SUCCESS = "activateSuccess";
	public static final String REQUEST_PARAMETER_ACTIVATE_SELECTED_SUCCESS="activateSelectedSuccess";
	public static final String REQUEST_PARAMETER_DEACTIVATE_SUCCESS = "deactivateSuccess";
	public static final String REQUEST_PARAMETER_DEACTIVATE_SELECTED_SUCCESS = "deactivateSelectedSuccess";

	public static final String MODEL_USER_LIST = "userlist";
	public static final String MODEL_SESSION_DATA = "sessionData";
	public static final String MODEL_PAGING_LINKS = "paging";
	public static final String MODEL_ORDER_BY = "orderBy";
	public static final String MODEL_ORDER_DIRECTION = "orderDirection";

	private static final Integer DEFAULT_LIMIT = 20;
	private static final String DEFAULT_SORT_BY = "userName";
	private static final Boolean DEFAULT_SORT_DIRECTION = true;

	@Inject
	private UserService userService;

	@Inject
	private UserlistSession session;

	@Inject
	private EmailSender emailSender;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleList(
			@RequestParam(value = REQUEST_PARAMETER_QUERY, required = false) String query,
			@RequestParam(value = REQUEST_PARAMETER_LIMIT, required = false) Integer limit,
			@RequestParam(value = REQUEST_PARAMETER_OFFSET, required = false) Long offset,
			@RequestParam(value = REQUEST_PARAMETER_ORDER_BY, required = false) String orderBy,
			@RequestParam(value = REQUEST_PARAMETER_ASCENDING, required = false) Boolean ascending) {

		ModelAndView modelAndView = new ModelAndView("user/list");

		final String attributes = "id, userName, active, name.givenName, name.familyName, groups.display";

		limit = limit == null ? DEFAULT_LIMIT : limit;
		orderBy = orderBy == null ? DEFAULT_SORT_BY : orderBy;
		ascending = ascending == null ? DEFAULT_SORT_DIRECTION : ascending;

		SCIMSearchResult<User> userList = userService.searchUser(query, limit, offset, orderBy, ascending, attributes);
		PagingLinks pagingLinks = generatePagingLinks(userList, query, orderBy, ascending);
		modelAndView.addObject(MODEL_USER_LIST, userList);
		modelAndView.addObject(MODEL_SESSION_DATA, session);
		modelAndView.addObject(MODEL_PAGING_LINKS, pagingLinks);
		modelAndView.addObject(MODEL_ORDER_BY, orderBy);
		modelAndView.addObject(MODEL_ORDER_DIRECTION, ascending);

		session.getPagingInformation().setQuery(query);
		session.getPagingInformation().setLimit(limit);
		session.getPagingInformation().setOffset(offset);
		session.getPagingInformation().setOrderBy(orderBy);
		session.getPagingInformation().setAscending(ascending);

		return modelAndView;
	}

	private PagingLinks generatePagingLinks(SCIMSearchResult<User> userList, String query, String orderBy,
			Boolean ascending) {
		PagingBuilder builder = new PagingBuilder()
				.setBaseUrl("")
				.setStartIndex(1L)  //SCIMResult begins with 1!
				.setOffsetParameter(REQUEST_PARAMETER_OFFSET)
				.setOffset(userList.getStartIndex())
				.setLimitParameter(REQUEST_PARAMETER_LIMIT)
				.setLimit(userList.getItemsPerPage())
				.setTotal(userList.getTotalResults());

		if (query != null) {
			builder.addParameter(REQUEST_PARAMETER_QUERY, query);
		}
		if (orderBy != null) {
			builder.addParameter(REQUEST_PARAMETER_ORDER_BY, orderBy);
		}
		if (ascending != null) {
			builder.addParameter(REQUEST_PARAMETER_ASCENDING, ascending);
		}

		return builder.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=filter")
	public String handleFilterAction(@RequestParam Map<String, String> allParameters) {

		Map<String, String> filterParameter = extractFilterParameter(allParameters);
		String filterQuery = buildFilterQuery(filterParameter);

		session.getPagingInformation().setFilterFields(filterParameter);

		return new RedirectBuilder()
				.setPath(CONTROLLER_PATH)
				.addParameter(REQUEST_PARAMETER_QUERY, filterQuery)
				.addParameter(REQUEST_PARAMETER_LIMIT, session.getPagingInformation().getLimit())
				.addParameter(REQUEST_PARAMETER_OFFSET, null)
				.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getPagingInformation().getOrderBy())
				.addParameter(REQUEST_PARAMETER_ASCENDING, session.getPagingInformation().getAscending())
				.build();
	}

	private Map<String, String> extractFilterParameter(Map<String, String> allParameters) {
		Map<String, String> result = new HashMap<String, String>();

		for (Entry<String, String> param : allParameters.entrySet()) {
			if (param.getKey().startsWith(REQUEST_PARAMETER_QUERY_PREFIX)) {
				if(param.getValue() != null){
					result.put(param.getKey(), param.getValue().trim());
				}
			}
		}

		return result;
	}

	protected String buildFilterQuery(Map<String, String> filterParameter) {
		StringBuilder filterQuery = new StringBuilder();

		for (Entry<String, String> param : filterParameter.entrySet()) {
			final String queryPrefixRegEx = "^" + REQUEST_PARAMETER_QUERY_PREFIX.replace(".", "\\.");
			final String queryField = param.getKey().replaceAll(queryPrefixRegEx, "");
			final String queryFieldValue = param.getValue();
			if (!"".equals(queryFieldValue)) {
				if (filterQuery.length() > 0) {
					filterQuery.append(" AND ");
				}

				filterQuery.append(queryField);
				filterQuery.append(" sw = \"");
				filterQuery.append(queryFieldValue);
				filterQuery.append("\"");
			}
		}

		return filterQuery.toString();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=sort")
	public String handleSortAction(
			@RequestParam(value = REQUEST_PARAMETER_ORDER_BY) String orderBy,
			@RequestParam(value = REQUEST_PARAMETER_ASCENDING) Boolean ascending) {

		return new RedirectBuilder()
				.setPath(CONTROLLER_PATH)
				.addParameter(REQUEST_PARAMETER_QUERY, session.getPagingInformation().getQuery())
				.addParameter(REQUEST_PARAMETER_LIMIT, session.getPagingInformation().getLimit())
				.addParameter(REQUEST_PARAMETER_OFFSET, session.getPagingInformation().getOffset())
				.addParameter(REQUEST_PARAMETER_ORDER_BY, orderBy)
				.addParameter(REQUEST_PARAMETER_ASCENDING, ascending)
				.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=limit")
	public String handleLimitAction(@RequestParam(value = REQUEST_PARAMETER_LIMIT) Integer limit) {

		return new RedirectBuilder()
				.setPath(CONTROLLER_PATH)
				.addParameter(REQUEST_PARAMETER_QUERY, session.getPagingInformation().getQuery())
				.addParameter(REQUEST_PARAMETER_LIMIT, limit)
				.addParameter(REQUEST_PARAMETER_OFFSET, null)
				.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getPagingInformation().getOrderBy())
				.addParameter(REQUEST_PARAMETER_ASCENDING, session.getPagingInformation().getAscending())
				.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=deactivateSelected",method = RequestMethod.POST)
	public String handlerUserDeactivationSelected (
			@RequestParam(value = REQUEST_PARAMETER_USER_ID_ARRAY) final String[] idArray,
			@RequestParam(value = REQUEST_PARAMETER_SEND_MAIL) final Boolean sendMail,
			HttpServletRequest request) {

		User user;

		for(String userId : idArray){
			userService.deactivateUser(userId);
			if (sendMail) {
				user = userService.getUser(userId);

				if(user.getLocale() == null) {
					emailSender.sendDeactivateMail(user, request.getLocale());
				} else {
					emailSender.sendDeactivateMail(user);
				}
			}
		}

		return new RedirectBuilder()
				.setPath(CONTROLLER_PATH)
				.addParameter(REQUEST_PARAMETER_DEACTIVATE_SELECTED_SUCCESS, true)
				.addParameter(REQUEST_PARAMETER_QUERY, session.getPagingInformation().getQuery())
				.addParameter(REQUEST_PARAMETER_LIMIT, session.getPagingInformation().getLimit())
				.addParameter(REQUEST_PARAMETER_OFFSET, session.getPagingInformation().getOffset())
				.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getPagingInformation().getOrderBy())
				.addParameter(REQUEST_PARAMETER_ASCENDING, session.getPagingInformation().getAscending())
				.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=activateSelected",method = RequestMethod.POST)
	public String handlerUserActivationSelected (
			@RequestParam(value = REQUEST_PARAMETER_USER_ID_ARRAY) final String[] idArray,
			@RequestParam(value = REQUEST_PARAMETER_SEND_MAIL) final Boolean sendMail,
			HttpServletRequest request) {

		User user;

		for(String userId : idArray){
			userService.activateUser(userId);

			if (sendMail) {
				user = userService.getUser(userId);
				if(user.getLocale() == null) {
					emailSender.sendActivateMail(user, request.getLocale());
				} else {
					emailSender.sendActivateMail(user);
				}
			}
		}

		return new RedirectBuilder()
				.setPath(CONTROLLER_PATH)
				.addParameter(REQUEST_PARAMETER_ACTIVATE_SELECTED_SUCCESS, true)
				.addParameter(REQUEST_PARAMETER_QUERY, session.getPagingInformation().getQuery())
				.addParameter(REQUEST_PARAMETER_LIMIT, session.getPagingInformation().getLimit())
				.addParameter(REQUEST_PARAMETER_OFFSET, session.getPagingInformation().getOffset())
				.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getPagingInformation().getOrderBy())
				.addParameter(REQUEST_PARAMETER_ASCENDING, session.getPagingInformation().getAscending())
				.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=deleteSelected", method = RequestMethod.POST)
	public String handleUserDeletionSelected(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID_ARRAY) final String[] idArray,
			HttpServletRequest request){

		for(String userId : idArray){
			userService.deleteUser(userId);
		}

		return new RedirectBuilder()
			.setPath(CONTROLLER_PATH)
			.addParameter(REQUEST_PARAMETER_DELETE_SELECTED_SUCCESS, true)
			.addParameter(REQUEST_PARAMETER_QUERY, session.getPagingInformation().getQuery())
			.addParameter(REQUEST_PARAMETER_LIMIT, session.getPagingInformation().getLimit())
			.addParameter(REQUEST_PARAMETER_OFFSET, session.getPagingInformation().getOffset())
			.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getPagingInformation().getOrderBy())
			.addParameter(REQUEST_PARAMETER_ASCENDING, session.getPagingInformation().getAscending())
			.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=deactivate", method = RequestMethod.POST)
	public String handleUserDeactivation(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) final String id,
			@RequestParam(value = REQUEST_PARAMETER_SEND_MAIL) final Boolean sendMail,
			HttpServletRequest request){

		userService.deactivateUser(id);

		if (sendMail) {
			User user = userService.getUser(id);

			if(user.getLocale() == null) {
				emailSender.sendDeactivateMail(user, request.getLocale());
			} else {
				emailSender.sendDeactivateMail(user);
			}
		}

		return new RedirectBuilder()
			.setPath(CONTROLLER_PATH)
			.addParameter(REQUEST_PARAMETER_DEACTIVATE_SUCCESS, true)
			.addParameter(REQUEST_PARAMETER_QUERY, session.getPagingInformation().getQuery())
			.addParameter(REQUEST_PARAMETER_LIMIT, session.getPagingInformation().getLimit())
			.addParameter(REQUEST_PARAMETER_OFFSET, session.getPagingInformation().getOffset())
			.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getPagingInformation().getOrderBy())
			.addParameter(REQUEST_PARAMETER_ASCENDING, session.getPagingInformation().getAscending())
			.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=activate", method = RequestMethod.POST)
	public String handleUserActivation(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) final String id,
			@RequestParam(value = REQUEST_PARAMETER_SEND_MAIL) final Boolean sendMail,
			HttpServletRequest request){

		userService.activateUser(id);

		if (sendMail) {
			User user = userService.getUser(id);

			if(user.getLocale() == null) {
				emailSender.sendActivateMail(user, request.getLocale());
			} else {
				emailSender.sendActivateMail(user);
			}
		}

		return new RedirectBuilder()
			.setPath(CONTROLLER_PATH)
			.addParameter(REQUEST_PARAMETER_ACTIVATE_SUCCESS, true)
			.addParameter(REQUEST_PARAMETER_QUERY, session.getPagingInformation().getQuery())
			.addParameter(REQUEST_PARAMETER_LIMIT, session.getPagingInformation().getLimit())
			.addParameter(REQUEST_PARAMETER_OFFSET, session.getPagingInformation().getOffset())
			.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getPagingInformation().getOrderBy())
			.addParameter(REQUEST_PARAMETER_ASCENDING, session.getPagingInformation().getAscending())
			.build();
	}

	@RequestMapping(params = REQUEST_PARAMETER_ACTION + "=delete", method = RequestMethod.POST)
	public String handleUserDeletion(
			@RequestParam(value = REQUEST_PARAMETER_USER_ID) final String id,
			HttpServletRequest request){

			userService.deleteUser(id);

		return new RedirectBuilder()
			.setPath(CONTROLLER_PATH)
			.addParameter(REQUEST_PARAMETER_DELETE_SUCCESS, true)
			.addParameter(REQUEST_PARAMETER_QUERY, session.getPagingInformation().getQuery())
			.addParameter(REQUEST_PARAMETER_LIMIT, session.getPagingInformation().getLimit())
			.addParameter(REQUEST_PARAMETER_OFFSET, session.getPagingInformation().getOffset())
			.addParameter(REQUEST_PARAMETER_ORDER_BY, session.getPagingInformation().getOrderBy())
			.addParameter(REQUEST_PARAMETER_ASCENDING, session.getPagingInformation().getAscending())
			.build();
	}
}
