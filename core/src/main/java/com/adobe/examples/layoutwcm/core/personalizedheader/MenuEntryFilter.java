package com.adobe.examples.layoutwcm.core.personalizedheader;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.util.Set;

import static com.adobe.examples.layoutwcm.core.personalizedheader.Contants.USER_GROUP;

/**
 * WHAT IS IT ???
 * <p>
 * WHAT PURPOSE THAT IT HAS ???
 * </p>
 *
 * @author niek.raaijkmakers@external.cybercon.de
 * @since 2018-07-19
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class MenuEntryFilter {

    public static final String PN_SHOW_FOR_USER_GROUPS = "showForUserGroupsOnly";

    private boolean filtered = false;

    @Self
    private SlingHttpServletRequest request;

    @RequestAttribute
    private Page menuEntryPage;

    @PostConstruct
    public void init(){

        @Nullable String[] showForUserGroupsArray = getShowForUserGroupOnlyArray();

        //only if the menuitem has show user groups defined, proceed with the filter
        if(!ArrayUtils.isEmpty(showForUserGroupsArray)){
            String userGroup = getUserGroup();
            Set<String> showForUserGroups = ImmutableSet.copyOf(showForUserGroupsArray);
            filtered = !showForUserGroups.contains(userGroup);
        }

    }

    private String[] getShowForUserGroupOnlyArray() {
        ValueMap properties = menuEntryPage.getContentResource().getValueMap();
        return properties.get(PN_SHOW_FOR_USER_GROUPS, String[].class);
    }

    private @Nonnull String getUserGroup(){
        @Nullable Cookie userGroupCookie = request.getCookie(USER_GROUP);

        if(userGroupCookie == null){
            return "anonymous";
        }else{
            return userGroupCookie.getValue();
        }
    }

    public boolean isFiltered() {
        return filtered;
    }
}
