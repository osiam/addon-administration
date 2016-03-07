package org.osiam.addons.administration.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 * Generic controller with common functionality.
 */
public abstract class GenericController {

    private static final String REQUEST_PARAMETER_QUERY_PREFIX = "query.";

    @Inject
    private HttpSession httpSession;

    @Inject
    private GeneralSessionData generalSessionData;

    /**
     *
     * @param binder
     *            Binds the web request parameters to JavaBean object Trims the request parameters false set them to
     *            empty true set them to null
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn() {
        return generalSessionData.getAccessToken() != null;
    }

    /**
     * Store an object into the http session.
     *
     * @param key
     *            Under which key should this object stored.
     * @param toStore
     *            Object that should be stored.
     */
    public void storeInSession(String key, Object toStore) {
        httpSession.setAttribute(generateKey(key), toStore);
    }

    /**
     * Get the object from the http session.
     *
     * @param key
     *            The key under which the object will be found.
     * @return The object if one was found under the given key. Otherwise <b>null</b>.
     */
    public Object restoreFromSession(String key) {
        return httpSession.getAttribute(generateKey(key));
    }

    /**
     * Remove the object from the http session.
     *
     * @param key
     *            The key under which the object will be found.
     */
    public void removeFromSession(String key) {
        httpSession.removeAttribute(generateKey(key));
    }

    /**
     * Build filter query
     *
     * @param filterParameter
     *
     * @return filter query
     */
    public String buildFilterQuery(Map<String, String> filterParameter) {
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
                filterQuery.append(" sw \"");
                filterQuery.append(queryFieldValue);
                filterQuery.append("\"");
            }
        }
        return filterQuery.toString();
    }

    /**
     * Extract query params
     *
     * @param allParameters
     *
     * @return extracted params
     */
    public Map<String, String> extractFilterParameter(Map<String, String> allParameters) {
        Map<String, String> result = new HashMap<String, String>();
        for (Entry<String, String> param : allParameters.entrySet()) {
            if (param.getKey().startsWith(REQUEST_PARAMETER_QUERY_PREFIX)) {
                if (param.getValue() != null) {
                    result.put(param.getKey(), param.getValue().trim());
                }
            }
        }
        return result;
    }

    protected void storeBindingResultIntoSession(BindingResult result, String modelName) {
        storeInSession(generateKey(modelName), result);
    }

    protected Object restoreBindingResultFromSession(String modelName) {
        return restoreFromSession(generateKey(modelName));
    }

    protected void enrichBindingResultFromSession(String modelName, ModelAndView model) {
        model.addObject(BindingResult.MODEL_KEY_PREFIX + modelName, restoreBindingResultFromSession(modelName));
    }

    protected void removeBindingResultFromSession(String modelName) {
        removeFromSession(generateKey(modelName));
    }

    private String generateKey(String key) {
        return getClass().getName() + key;
    }

}
