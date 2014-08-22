package org.osiam.addons.administration.service;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.query.QueryBuilder;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.SCIMSearchResult;
import org.springframework.stereotype.Component;

@Component
public class GroupService {

    @Inject
    private GeneralSessionData sessionData;

    @Inject
    private OsiamConnector connector;

    public SCIMSearchResult<Group> searchGroup(String query, Integer limit, Long offset, String orderBy,
            Boolean ascending) {

        QueryBuilder qb = new QueryBuilder();
        qb.filter(query);
        qb.count(limit == null ? 0 : limit);
        qb.startIndex(offset == null ? 0 : offset);

        if (ascending) {
            qb.ascending(orderBy);
        } else {
            qb.descending(orderBy);
        }

        return connector.searchGroups(qb.build(), sessionData.getAccesstoken());
    }

}
