package com.adobe.examples.layoutwcm.core.navigation;


import org.apache.sling.api.SlingHttpServletRequest;

public interface FilteredNavigationItem {
    
    /**
     * Returns true if the item should be included in the navigation hierarchy for this particular request
     * @return
     */
    boolean isIncludedFor(SlingHttpServletRequest request);
    
}
