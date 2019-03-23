package com.adobe.examples.layoutwcm.core.httpcache;

import com.adobe.acs.commons.httpcache.config.AuthenticationStatusConfigConstants;
import com.adobe.acs.commons.httpcache.engine.HttpCacheEngine;
import com.adobe.acs.commons.httpcache.exception.HttpCacheException;
import com.adobe.acs.commons.httpcache.exception.HttpCacheKeyCreationException;
import com.adobe.acs.commons.httpcache.exception.HttpCachePersistenceException;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
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
public class ExpensiveComponentInvalidator  implements ResourceChangeListener{
    
    private static final Logger LOG = LoggerFactory.getLogger(ExpensiveComponentInvalidator.class);
    
    @Reference
    private HttpCacheEngine engine;
    
    @Override
    public void onChange(@Nonnull List<ResourceChange> changes) {
    
        for(ResourceChange change : changes){
            
            String path = change.getPath();
    
            if(engine.isPathPotentialToInvalidate(path)){
                invalidateWithAuthenticationSuffix(path, AuthenticationStatusConfigConstants.ANONYMOUS_REQUEST);
                invalidateWithAuthenticationSuffix(path, AuthenticationStatusConfigConstants.AUTHENTICATED_REQUEST);
                invalidateWithAuthenticationSuffix(path, AuthenticationStatusConfigConstants.BOTH_ANONYMOUS_AUTHENTICATED_REQUESTS);
            }
        }
        
        
    }
    
    private void invalidateWithAuthenticationSuffix(String path, String authenticationSuffix){
    
        String computedCachePath = path + ".content.html [AUTH_REQ:" + authenticationSuffix + "]";
    
        try {
            engine.invalidateCache(computedCachePath);
        } catch (HttpCacheException e) {
            LOG.error("Error invalidating path {} in httpcache: {}", path, e);
        }
    
    }
}
