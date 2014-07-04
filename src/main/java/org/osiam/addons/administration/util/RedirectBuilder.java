package org.osiam.addons.administration.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.UriBuilder;

/**
 * This class is responsible for creating an redirect string that can be returned by Spring-Request-Handler.
 */
public class RedirectBuilder {
    private String destination;
    private String path;
    private String query;
    private Map<String, Object> parameters = new HashMap<String, Object>();

    /**
     * Set the url-path of the redirect destination.
     * 
     * @param path
     *        The url path.
     * @return this
     */
    public RedirectBuilder setPath(String path) {
        this.path = path;

        return this;
    }

    /**
     * Set the url-query of the redirect destination. This query should be encoded before!
     * 
     * @param query
     *        The query of the url.
     * @return this
     */
    public RedirectBuilder setQuery(String query) {
        this.query = query;

        return this;
    }

    /**
     * Set the complete redirect destination.
     * 
     * @param destination
     *        The <b>complete</b> destination.
     * @return this
     */
    public RedirectBuilder setDestination(String destination) {
        this.destination = destination;

        return this;
    }

    /**
     * Add a parameter which will be append at the url query. The value of the parameter will be encode automatically.
     * 
     * @param key
     *        The name of the parameter.
     * @param value
     *        The value of the parameter.
     * @return this
     */
    public RedirectBuilder addParameter(String key, Object value) {
        this.parameters.put(key, value);

        return this;
    }

    /**
     * Return the redirect string. That can be used for spring-request-handler.
     * 
     * @return The redirect string.
     */
    public String build() {
        UriBuilder uri = null;

        if (destination != null) {
            uri = UriBuilder.fromUri(destination);
        } else {
            uri = UriBuilder.fromPath("");

            if (path != null) {
                uri.path(path);
            }

            if (query != null) {
                uri.replaceQuery(query);
            }

            if (!parameters.isEmpty()) {
                for (Entry<String, Object> param : parameters.entrySet()) {
                    uri.queryParam(param.getKey(), param.getValue() != null ? param.getValue() : "");
                }
            }
        }

        return "redirect:" + uri.toString();
    }
}
