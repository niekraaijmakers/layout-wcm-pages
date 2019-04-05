package com.adobe.examples.layoutwcm.core.navigation.impl;

import com.adobe.examples.layoutwcm.core.navigation.FilteredNavigationItem;
import com.adobe.examples.layoutwcm.core.user.User;
import com.adobe.examples.layoutwcm.core.user.UserGroup;
import com.adobe.examples.layoutwcm.core.user.UserService;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Model(adaptables = Page.class,
        adapters = {FilteredNavigationItem.class},
        resourceType = {UserGroupSpecificNavigationItem.RESOURCE_TYPE})
public class UserGroupSpecificNavigationItem implements FilteredNavigationItem {
    
    public static final String RESOURCE_TYPE = "layout-wcm/components/structure/page/layout/headerpage/menupage-mainentry-userspecific";
    public static final String PN_SHOW_FOR_USER_GROUPS = "showForUserGroupsOnly";
    
    @Self
    private Page resourcePage;
    
    @OSGiService
    private UserService userStateService;
    
    @Override
    public boolean isIncludedFor(SlingHttpServletRequest request) {
        User user = userStateService.getUser(request);
        UserGroup userGroup = user.getGroup();
        List<UserGroup> userGroups = getShowForUserGroupsOnly(request.getResource());
        return userGroups.contains(userGroup);
        
    }
    
    private List<UserGroup> getShowForUserGroupsOnly(Resource contentResource) {
        ValueMap properties = contentResource.getValueMap();
        String[] userGroupsString = properties.get(PN_SHOW_FOR_USER_GROUPS, String[].class);
        return Arrays.stream(userGroupsString).map(UserGroup::valueOf).collect(Collectors.toList());
    }
}
