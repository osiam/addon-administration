package org.osiam.addons.administration.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
     * Set the url-query of the redirect destination. 
     * This query should be encoded before!
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
     * Add a parameter which will be append at the url query.
     * The value of the parameter will be encode automatically.
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
        StringBuilder redirect = new StringBuilder("redirect:");

        if (destination != null) {
            redirect.append(destination);
        } else {
            if (path != null) {
                redirect.append(path);
            }

            if (query != null) {
                redirect.append("?");
                redirect.append(query);
            }

            if (!parameters.isEmpty()) {
                if (query == null) {
                    redirect.append("?");
                } else {
                    redirect.append("&");
                }

                boolean isFirst = true;
                for (Entry<String, Object> param : parameters.entrySet()) {
                    if (!isFirst) {
                        redirect.append("&");
                    }

                    redirect.append(param.getKey());
                    if (param.getValue() != null) {
                        redirect.append("=");

                        try {
                            String encodedValue = URLEncoder.encode(param.getValue().toString(), "UTF-8");
                            redirect.append(encodedValue);
                        } catch (UnsupportedEncodingException e) {
                            throw new IllegalStateException(e);
                        }
                    }

                    isFirst = false;
                }
            }
        }

        return redirect.toString();
    }
}
