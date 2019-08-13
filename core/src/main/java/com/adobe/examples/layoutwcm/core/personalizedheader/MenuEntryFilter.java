package com.adobe.examples.layoutwcm.core.personalizedheader;

import com.adobe.examples.layoutwcm.core.user.User;
import com.adobe.examples.layoutwcm.core.user.UserGroup;
import com.adobe.examples.layoutwcm.core.user.UserService;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Model(adaptables = SlingHttpServletRequest.class)
public class MenuEntryFilter {

    public static final String PN_SHOW_FOR_USER_GROUPS = "showForUserGroupsOnly";

    private boolean passingFilter;

    @Self
    private SlingHttpServletRequest request;

    @RequestAttribute
    private Page menuEntryPage;
    
    @OSGiService
    private UserService userService;

    @PostConstruct
    public void init(){

        List<UserGroup> userGroups = getShowForUserGroupsOnly();
        
        //only if the menuitem has show user groups defined, proceed with the filter
        if(!userGroups.isEmpty()){
            User user = userService.getUser(request);
            passingFilter = userGroups.contains(user.getGroup());
        }else{
            passingFilter = true;
        }

    }

    private List<UserGroup> getShowForUserGroupsOnly() {
        ValueMap properties = menuEntryPage.getContentResource().getValueMap();
        String[] userGroupsString = properties.get(PN_SHOW_FOR_USER_GROUPS, new String[]{});
        return Arrays.stream(userGroupsString).map(UserGroup::valueOf).collect(Collectors.toList());
    }


    public boolean isPassingFilter() {
        return passingFilter;
    }
}
