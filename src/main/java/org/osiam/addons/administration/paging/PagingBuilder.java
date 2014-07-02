package org.osiam.addons.administration.paging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This builder is responsible for building a {@link PagingLinks} instance that contains all paging links.
 */
public class PagingBuilder {
    private String baseUrl = "";
    private final Map<String, Object[]> parameters = new HashMap<String, Object[]>();
    private String offsetParameter = "offset";
    private String limitParameter = "limit";
    private Long startIndex = 0L;
    private Long offset;
    private Long limit;
    private Long total;

    /**
     * Set the base url for the paging links. After that string the url-query will be appended.
     * 
     * @param baseUrl
     *        The base url.
     * @return this
     */
    public PagingBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * Set the name of the parameter which contains the offset value.
     * 
     * @param parameter
     *        The parameter name.
     * @return this
     */
    public PagingBuilder setOffsetParameter(String parameter) {
        this.offsetParameter = parameter;
        return this;
    }

    /**
     * Set the name of the parameter which contains the limit value.
     * 
     * @param parameter
     *        The parameter name.
     * @return this
     */
    public PagingBuilder setLimitParameter(String parameter) {
        this.limitParameter = parameter;
        return this;
    }

    /**
     * Set the current offset for paging.
     * 
     * @param offset
     *        The current offset.
     * @return this
     */
    public PagingBuilder setOffset(Long offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Set the current limit for paging.
     * 
     * @param limit
     *        The current limit.
     * @return this
     */
    public PagingBuilder setLimit(Long limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Set the total count of result for the current paging.
     * 
     * @param total
     *        Total count of results.
     * @return this
     */
    public PagingBuilder setTotal(Long total) {
        this.total = total;
        return this;
    }

    /**
     * Set the start index of paging. 
     * For example: 
     *  if you begin to count by 1, you need to set the start index to 1!
     * 
     * @param startIndex The start index.
     * @return this
     */
    public PagingBuilder setStartIndex(Long startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    /**
     * Add a parameter that will be always appending at the paging links.
     * 
     * @param parameter Name of the parameter.
     * @param values Parameter values.
     * @return this
     */
    public PagingBuilder addParameter(String parameter, Object... values) {
        this.parameters.put(parameter, values);
        return this;
    }

    public PagingLinks build() {
        List<String> urls = new ArrayList<String>();
        PagingLinks pagingList = new PagingLinks();

        if (offset == null || limit == null || total == null) {
            return pagingList;
        }

        for (long i = 0; i < total; i += limit) {
            StringBuilder url = new StringBuilder(baseUrl);
            url.append("?");

            url.append(buildParameter(offsetParameter, String.valueOf(startIndex + i)));
            url.append("&");
            url.append(buildParameter(limitParameter, String.valueOf(limit)));

            // insert additional parameters
            for (String curKey : parameters.keySet()) {
                Object[] value = parameters.get(curKey);
                if (value != null) {
                    url.append("&");
                    url.append(buildParameter(curKey, value));
                }
            }

            if (i + startIndex == offset) {
                pagingList.setCurLink(url.toString());
            }
            urls.add(url.toString());
        }

        if (pagingList.getCurLink() == null) {
            pagingList.setCurLink(urls.get(urls.size() - 1));
        }

        pagingList.setLinks(urls);

        if (pagingList.getLinks().size() > 0) {
            // set previous- and next- link
            int indexOfCurLink = pagingList.getLinks().indexOf(pagingList.getCurLink());
            if (indexOfCurLink != -1) {
                if ((indexOfCurLink + 1) < pagingList.getLinks().size()) {
                    pagingList.setNextLink(pagingList.getLinks().get(indexOfCurLink + 1));
                }
                if ((indexOfCurLink - 1) >= 0 && pagingList.getLinks().size() != 0) {
                    pagingList.setPrevLink(pagingList.getLinks().get(indexOfCurLink - 1));
                }
            }
        }

        return pagingList;
    }

    private String buildParameter(String parameter, Object... values) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            Object value = values[i];

            if (i > 0) {
                sb.append("&");
            }

            sb.append(parameter);
            sb.append("=");
            sb.append(value);
        }

        return sb.toString();
    }
}
