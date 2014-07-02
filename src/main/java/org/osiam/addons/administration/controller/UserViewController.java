package org.osiam.addons.administration.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.UserlistSession;
import org.osiam.addons.administration.paging.PagingBuilder;
import org.osiam.addons.administration.paging.PagingLinks;
import org.osiam.addons.administration.service.UserService;
import org.osiam.addons.administration.util.RedirectBuilder;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public static final String MODEL_USER_LIST = "userlist";
    public static final String MODEL_SESSION_DATA = "sessionData";
    public static final String MODEL_PAGING_LINKS = "paging";

    private static final Integer DEFAULT_LIMIT = 20;

    @Inject
    private UserService userService;

    @Inject
    private UserlistSession session;

    @RequestMapping
    public ModelAndView handleList(
            @RequestParam(value = REQUEST_PARAMETER_QUERY, required = false) String query,
            @RequestParam(value = REQUEST_PARAMETER_LIMIT, required = false) Integer limit,
            @RequestParam(value = REQUEST_PARAMETER_OFFSET, required = false) Long offset,
            @RequestParam(value = REQUEST_PARAMETER_ORDER_BY, required = false) String orderBy,
            @RequestParam(value = REQUEST_PARAMETER_ASCENDING, required = false) Boolean ascending) {

        ModelAndView modelAndView = new ModelAndView("user/list");

        final String attributes = "userName, name.givenName, name.familyName";
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        }

        SCIMSearchResult<User> userList = userService.searchUser(query, limit, offset, orderBy, ascending, attributes);
        PagingLinks pagingLinks = generatePagingLinks(userList, query, orderBy, ascending);
        modelAndView.addObject(MODEL_USER_LIST, userList);
        modelAndView.addObject(MODEL_SESSION_DATA, session);
        modelAndView.addObject(MODEL_PAGING_LINKS, pagingLinks);

        session.setQuery(query);
        session.setLimit(limit);
        session.setOffset(offset);
        session.setOrderBy(orderBy);
        session.setAscending(ascending);

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
    public String handleFilterAction(
            @RequestParam Map<String, String> allParameters) {

        Map<String, String> filterParameter = extractFilterParameter(allParameters);
        String filterQuery = buildFilterQuery(filterParameter);

        session.setFilterFields(filterParameter);

        return new RedirectBuilder()
                .setPath(CONTROLLER_PATH)
                .addParameter(REQUEST_PARAMETER_QUERY, filterQuery)
                .addParameter(REQUEST_PARAMETER_LIMIT, session.getLimit())
                .addParameter(REQUEST_PARAMETER_OFFSET, session.getOffset())
                .addParameter(REQUEST_PARAMETER_ORDER_BY, session.getOrderBy())
                .addParameter(REQUEST_PARAMETER_ASCENDING, session.getAscending())
                .build();
    }

    private Map<String, String> extractFilterParameter(Map<String, String> allParameters) {
        Map<String, String> result = new HashMap<String, String>();

        for (Entry<String, String> param : allParameters.entrySet()) {
            if (param.getKey().startsWith(REQUEST_PARAMETER_QUERY_PREFIX)) {
                result.put(param.getKey(), param.getValue());
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
                .addParameter(REQUEST_PARAMETER_QUERY, session.getQuery())
                .addParameter(REQUEST_PARAMETER_LIMIT, session.getLimit())
                .addParameter(REQUEST_PARAMETER_OFFSET, session.getOffset())
                .addParameter(REQUEST_PARAMETER_ORDER_BY, orderBy)
                .addParameter(REQUEST_PARAMETER_ASCENDING, ascending)
                .build();
    }

    @RequestMapping(params = REQUEST_PARAMETER_ACTION + "=limit")
    public String handleLimitAction(
            @RequestParam(value = REQUEST_PARAMETER_LIMIT) Integer limit) {

        return new RedirectBuilder()
                .setPath(CONTROLLER_PATH)
                .addParameter(REQUEST_PARAMETER_QUERY, session.getQuery())
                .addParameter(REQUEST_PARAMETER_LIMIT, limit)
                .addParameter(REQUEST_PARAMETER_OFFSET, session.getOffset())
                .addParameter(REQUEST_PARAMETER_ORDER_BY, session.getOrderBy())
                .addParameter(REQUEST_PARAMETER_ASCENDING, session.getAscending())
                .build();
    }
}
