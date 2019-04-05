package com.adobe.examples.layoutwcm.core.navigation.impl;

import com.adobe.acs.commons.models.injectors.annotation.AemObject;
import com.adobe.cq.wcm.core.components.models.Navigation;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = {Navigation.class},
        resourceType = {NavigationItemImpl.RESOURCE_TYPE})
public class NavigationItemImpl implements NavigationItem {
    
    @Self
    private SlingHttpServletRequest request;
    @AemObject
    private Page resourcePage;
    
    @ValueMapValue
    private String customTemplatePath;
    
    private List<NavigationItem> children;
    
    public static final String RESOURCE_TYPE = "layout-wcm/components/layout/navigation";
    
    @PostConstruct
    public void init() {
        this.children = initChildren();
    }
    
    @Override
    public Page getPage() {
        return resourcePage;
    }
    
    @Override
    public boolean isActive() {
        return false;
    }
    
    public boolean isCustomTemplateConfigured(){
        return isNotBlank(customTemplatePath);
    }
    
    public String getCustomTemplatePath(){
        return customTemplatePath;
    }
    
    public Resource getNavigationResource(){
        return resourcePage.getContentResource();
    }
    
    @Override
    public List<NavigationItem> getChildren() {
        return children;
    }
    
    @Override
    public int getLevel() {
        return resourcePage.getDepth();
    }
    
    private List<NavigationItem> initChildren(){
        return ((List<Page>)toList(
                resourcePage.listChildren((page) -> NavigationUtils.filterNavigationPage(page, request)))
        ).stream().map(
                        (page) -> page.adaptTo(NavigationItem.class)
        ).collect(
                Collectors.toList()
        );
    }
}
