package com.adobe.examples.layoutwcm.core.navigation.impl;

import com.adobe.acs.commons.models.injectors.annotation.AemObject;
import com.adobe.cq.wcm.core.components.models.Navigation;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections.IteratorUtils.toList;

@Model(adaptables = SlingHttpServletRequest.class,
        resourceType = {NavigationImpl.RESOURCE_TYPE})

public class NavigationImpl implements Navigation {
    
    @Self
    private SlingHttpServletRequest request;
    @OSGiService
    @AemObject
    private Page resourcePage;
    
    public static final String RESOURCE_TYPE = "layout-wcm/components/layout/navigation";
    
    
    public List<NavigationItem> getItems() {
        return ((List<Page>)toList(
                resourcePage.listChildren((page) -> NavigationUtils.filterNavigationPage(page, request)))
        ).stream().map(
                        (page) -> page.adaptTo(NavigationItem.class)
                ).collect(
                        Collectors.toList()
                );
        
    }
    
    @Nonnull
    @Override
    public String getExportedType() {
        return request.getResource().getResourceType();
    }
}
