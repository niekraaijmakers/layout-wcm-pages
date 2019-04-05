package com.adobe.examples.layoutwcm.core.user;

import org.apache.sling.api.SlingHttpServletRequest;


public interface UserService {
    
    User getUser(SlingHttpServletRequest request);
    
    boolean isLoggedIn(SlingHttpServletRequest request);
    
}
