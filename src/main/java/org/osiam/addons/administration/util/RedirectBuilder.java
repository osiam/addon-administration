package org.osiam.addons.administration.util;

/**
 * This class is responsible for creating an redirect string that can be returned by Spring-Request-Handler.
 */
public class RedirectBuilder {
    private String destination;
    private String path;
    private String query;
    
    /**
     * Set the url-path of the redirect destination.
     * 
     * @param path The url path.
     * @return this
     */
    public RedirectBuilder setPath(String path) {
        this.path = path;
        
        return this;
    }
    
    /**
     * Set the url-query of the redirect destination.
     * 
     * @param query The query of the url.
     * @return this
     */
    public RedirectBuilder setQuery(String query) {
        this.query = query;
        
        return this;
    }
    
    /**
     * Set the complete redirect destination.
     * 
     * @param destination The <b>complete</b> destination.
     * @return this
     */
    public RedirectBuilder setDestination(String destination){
        this.destination = destination;
        
        return this;
    }

    /**
     * Return the redirect string. That can be used for spring-request-handler.
     * 
     * @return The redirect string.
     */
    public String build() {
        StringBuilder redirect = new StringBuilder("redirect:");
        
        if(destination != null){
            redirect.append(destination);
        }else{
            if(path != null){
                redirect.append(path);
            }
            
            if(query != null){
                redirect.append("?");
                redirect.append(query);
            }
        }
        
        return redirect.toString();
    }

}
