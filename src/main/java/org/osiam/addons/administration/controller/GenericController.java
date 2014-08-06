package org.osiam.addons.administration.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * Generic controller with common functionality.
 */
public abstract class GenericController {
    private static final String SPRING_BR_PREFIX = BindingResult.MODEL_KEY_PREFIX;
    
    @Inject
    private HttpSession session;

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
        storeInSession(getClass().getName() + modelName, result);
    }

    protected Object restoreBindingResultFromSession(String modelName) {
        return restoreFromSession(getClass().getName() + modelName);
    }
    
    protected void restoreBindingResultFromSession(String modelName, ModelAndView model){
        model.addObject(SPRING_BR_PREFIX + modelName, restoreBindingResultFromSession(modelName));
    }

    protected void removeBindingResultFromSession(String modelName) {
        removeFromSession(getClass().getName() + modelName);
    }

    private String generateKey(String key) {
        return getClass().getName() + key;
    }
}
