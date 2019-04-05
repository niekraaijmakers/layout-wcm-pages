package com.adobe.examples.layoutwcm.core.navigation.impl;

import com.adobe.examples.layoutwcm.core.navigation.FilteredNavigationItem;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;


public class NavigationUtils {
    
    public static final boolean filterNavigationPage(Page navigationResourcePage, SlingHttpServletRequest request) {
    
        FilteredNavigationItem filteredPage = navigationResourcePage.adaptTo(FilteredNavigationItem.class);
    
        if (filteredPage != null) {
            return filteredPage.isIncludedFor(request);
        }
        return true;
    
    }
    
}
