package org.osiam.addons.administration.model.session;

import java.util.HashMap;
import java.util.Map;

public class PagingInformation {
	public String query;
	public Integer limit;
	public Long offset;
	public String orderBy;
	public Boolean ascending;
	public Map<String, String> filterFields = new HashMap<String, String>();

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Boolean getAscending() {
		return ascending;
	}

	public void setAscending(Boolean ascending) {
		this.ascending = ascending;
	}

	public Map<String, String> getFilterFields() {
		return filterFields;
	}

	public void setFilterFields(Map<String, String> filterFields) {
		this.filterFields = filterFields;
	}
}