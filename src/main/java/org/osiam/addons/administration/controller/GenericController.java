package org.osiam.addons.administration.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

/**
 * Generic controller with common functionality.
 */
public abstract class GenericController {

    @Inject
    private HttpSession session;

    /**
     * 
     * @param binder Binds the web request parameters to JavaBean object
     * Trims the request parameters
     * false set them to empty
     * true set them to null
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    /**
     * Store an object into the session.
     *
     * @param key Under which key should this object stored.
     * @param toStore Object that should be stored.
     */
    public void storeInSession(String key, Object toStore){
        session.setAttribute(generateKey(key), toStore);
    }

    /**
     * Get the object from the session.
     *
     * @param key The key under which the object will be found.
     * @return The object if one was found under the given key. Otherwise <b>null</b>.
     */
    public Object restoreFromSession(String key){
        return session.getAttribute(generateKey(key));
    }

    /**
     * Remove the object from the session.
     *
     * @param key The key under which the object will be found.
     */
    public void removeFromSession(String key){
        session.removeAttribute(generateKey(key));
    }

    protected void storeBindingResultIntoSession(BindingResult result, String modelName) {
        storeInSession(generateKey(modelName), result);
    }

    protected Object restoreBindingResultFromSession(String modelName) {
        return restoreFromSession(generateKey(modelName));
    }

    protected void enrichBindingResultFromSession(String modelName, ModelAndView model){
        model.addObject(BindingResult.MODEL_KEY_PREFIX + modelName, restoreBindingResultFromSession(modelName));
    }

    protected void removeBindingResultFromSession(String modelName) {
        removeFromSession(generateKey(modelName));
    }

    private String generateKey(String key) {
        return getClass().getName() + key;
    }
}
