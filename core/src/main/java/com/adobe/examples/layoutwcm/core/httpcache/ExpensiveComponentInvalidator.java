package com.adobe.examples.layoutwcm.core.httpcache;

import com.adobe.acs.commons.httpcache.config.AuthenticationStatusConfigConstants;
import com.adobe.acs.commons.httpcache.engine.HttpCacheEngine;
import com.adobe.acs.commons.httpcache.exception.HttpCacheException;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component(
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        immediate = true,
        property = {
                ResourceChangeListener.PATHS + "=/content/layout-wcm",
                ResourceChangeListener.CHANGES + "=ADDED",
                ResourceChangeListener.CHANGES + "=CHANGED",
                ResourceChangeListener.CHANGES + "=REMOVED",
        }
)
public class ExpensiveComponentInvalidator implements ResourceChangeListener {
    
    private static final Logger LOG = LoggerFactory.getLogger(AutomatedLayoutPageInvalidator.class);
 
    @Reference
    private HttpCacheEngine engine;
    
    private static final String PATH_SUFFIX = ".content.html";
    
    @Override
    public void onChange(List<ResourceChange> changes) {
        for (ResourceChange change : changes) {
            String jcrPath = change.getPath();
            
            invalidateWithAuthenticationSuffix(jcrPath, AuthenticationStatusConfigConstants.ANONYMOUS_REQUEST);
            invalidateWithAuthenticationSuffix(jcrPath, AuthenticationStatusConfigConstants.AUTHENTICATED_REQUEST);
            invalidateWithAuthenticationSuffix(jcrPath, AuthenticationStatusConfigConstants.BOTH_ANONYMOUS_AUTHENTICATED_REQUESTS);
        }
    }
    
    private void invalidateWithAuthenticationSuffix(String path, String authenticationSuffix){
        String computedPath = path + PATH_SUFFIX + " [AUTH_REQ:" + authenticationSuffix + "]";
        if(engine.isPathPotentialToInvalidate(computedPath)){
            try {
                engine.invalidateCache(computedPath);
            } catch (HttpCacheException e) {
                LOG.error("Error invalidating path {}, {} ",path, e);
            }
        }
    }
    
}
