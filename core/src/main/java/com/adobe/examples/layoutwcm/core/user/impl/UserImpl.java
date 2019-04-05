package com.adobe.examples.layoutwcm.core.user.impl;

import com.adobe.examples.layoutwcm.core.user.User;
import com.adobe.examples.layoutwcm.core.user.UserGroup;


public class UserImpl implements User {
    
    private final UserGroup userGroup;
    private final String firstName;
    private final String lastName;
    
    public UserImpl(UserGroup userGroup, String firstName, String lastName){
        
        this.userGroup = userGroup;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    @Override
    public UserGroup getGroup() {
        return userGroup;
    }
    
    @Override
    public String getFirstName() {
        return firstName;
    }
    
    @Override
    public String getLastName() {
        return lastName;
    }
}
