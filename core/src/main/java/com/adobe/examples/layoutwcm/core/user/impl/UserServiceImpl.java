package com.adobe.examples.layoutwcm.core.user.impl;

import com.adobe.acs.commons.util.CookieUtil;
import com.adobe.examples.layoutwcm.core.user.User;
import com.adobe.examples.layoutwcm.core.user.UserGroup;
import com.adobe.examples.layoutwcm.core.user.UserService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

import javax.servlet.http.Cookie;

import static com.adobe.examples.layoutwcm.core.personalizedheader.Contants.USER_GROUP;

@Component
public class UserServiceImpl implements UserService {
    
    
    @Override
    public User getUser(SlingHttpServletRequest request) {
        
        String userGroupParam =  getCookieValue(request, USER_GROUP, "ANONYMOUS");
        String firstName = getCookieValue(request, "firstName", "null");
        String lastName = getCookieValue(request, "lastName", "null");
    
        UserGroup userGroup = UserGroup.valueOf(userGroupParam);
        return new UserImpl(userGroup,firstName, lastName);
    }
    
    @Override
    public boolean isLoggedIn(SlingHttpServletRequest request) {
        return getUser(request).getGroup() != UserGroup.ANONYMOUS;
    }
    
    private String getCookieValue(SlingHttpServletRequest request, String key, String defaultValue){
        Cookie cookie = CookieUtil.getCookie(request, key);
        if(cookie != null){
            return cookie.getValue();
        }
        return defaultValue;
    }
}
