package com.adobe.examples.layoutwcm.core.httpcache;

import com.adobe.acs.commons.httpcache.config.HttpCacheConfig;
import com.adobe.acs.commons.httpcache.config.HttpCacheConfigExtension;
import com.adobe.acs.commons.httpcache.exception.HttpCacheKeyCreationException;
import com.adobe.acs.commons.httpcache.exception.HttpCacheRepositoryAccessException;
import com.adobe.acs.commons.httpcache.keys.CacheKey;
import com.adobe.acs.commons.httpcache.keys.CacheKeyFactory;
import com.adobe.examples.layoutwcm.core.httpcache.key.MyCustomResourceKey;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        service= {HttpCacheConfigExtension.class, CacheKeyFactory.class}
)
@Designate(ocd= MyCustomResourceExtension.Config.class, factory=true)
public class MyCustomResourceExtension implements HttpCacheConfigExtension, CacheKeyFactory {
    private static final Logger log = LoggerFactory.getLogger(MyCustomResourceExtension.class);
    @ObjectClassDefinition(name= "MyCustomResourceExtension")
    public @interface Config {
    
        @AttributeDefinition(name = "Allowed sling selectors",
                description = "String array of selectors that will be cached.")
        String[] httpcache_config_extension_selectors_allowed() default {};
    
        @AttributeDefinition(name = "Allowed paths",
                description = "Regex of content paths that can be cached.")
        String[] httpcache_config_extension_paths_allowed() default {};
    
        @AttributeDefinition(name = "Allowed resource types",
                description = "Regex of resource types that can be cached.")
        String[] httpcache_config_extension_resourcetypes_allowed() default {};
    
        @AttributeDefinition(name = "Check RT of ./jcr:content?",
                description = "Should the resourceType check be applied to ./jcr:content ?",
                defaultValue = "false")
        boolean httpcacheconfig_extension_resourcetypes_page_content() default false;
    
        @AttributeDefinition(name = "Config Name")
        String configName() default StringUtils.EMPTY;
    
    }
    
    // Custom cache config attributes
    
    private String[] selectors;
    private List<Pattern> pathPatterns;
    private List<Pattern> resourceTypePatterns;
    private boolean checkContentResourceType;
    
    //-------------------------<HttpCacheConfigExtension methods>
    
    @Override
    public boolean accepts(SlingHttpServletRequest request, HttpCacheConfig cacheConfig) throws
            HttpCacheRepositoryAccessException {
        
        if (log.isDebugEnabled()) {
            log.debug("ResourceType acceptance check on [ {} ~> {} ]", request.getResource(), request.getResource().getResourceType());
        }
        
        for (Pattern pattern : pathPatterns) {
            Matcher m = pattern.matcher(request.getResource().getPath());
            if (!m.matches()) {
                return false;
            }
        }
        // Passed the content path test..
        
        Resource candidateResource = request.getResource();
        if (checkContentResourceType) {
            candidateResource = candidateResource.getChild(JcrConstants.JCR_CONTENT);
            if (candidateResource == null) {
                return false;
            }
        }
        
        if(selectors.length > 0){
            boolean selectorMatched = false;
            for(String selector: selectors){
                final String[] presentSelectors = request.getRequestPathInfo().getSelectors();
                if(ArrayUtils.contains(presentSelectors, selector)){
                    selectorMatched = true;
                }
            }
            if(!selectorMatched){
                return false;
            }
        }
        
        
        log.debug("ResourceHttpCacheConfigExtension checking for resource type matches");
        // Match resource types.
        for (Pattern pattern : resourceTypePatterns) {
            Matcher m = pattern.matcher(candidateResource.getResourceType());
            
            if (m.matches()) {
                if (log.isTraceEnabled()) {
                    log.trace("ResourceHttpCacheConfigExtension accepts request [ {} ]", candidateResource);
                }
                return true;
            }
        }
        
        return false;
    }
    
    //-------------------------<CacheKeyFactory methods>
    
    @Override
    public CacheKey build(final SlingHttpServletRequest slingHttpServletRequest, final HttpCacheConfig cacheConfig)
            throws HttpCacheKeyCreationException {
        return new MyCustomResourceKey(slingHttpServletRequest, cacheConfig);
    }
    
    @Override
    public CacheKey build(final String resourcePath, final HttpCacheConfig cacheConfig)
            throws HttpCacheKeyCreationException {
        return new MyCustomResourceKey(resourcePath, cacheConfig);
    }
    
    @Override
    public boolean doesKeyMatchConfig(CacheKey key, HttpCacheConfig cacheConfig) throws HttpCacheKeyCreationException {
        
        // Check if key is instance of ResourcePathCacheKey.
        if (!(key instanceof MyCustomResourceKey)) {
            return false;
        }
        // Validate if key request uri can be constructed out of uri patterns in cache config.
        return new MyCustomResourceKey(key.getUri(), cacheConfig).equals(key);
    }
    
    //-------------------------<OSGi Component methods>
    
    @Activate
    protected void activate(Config config) {
        resourceTypePatterns = ParameterUtil.toPatterns(config.httpcache_config_extension_resourcetypes_allowed());
        pathPatterns = ParameterUtil.toPatterns(config.httpcache_config_extension_paths_allowed());
        checkContentResourceType = config.httpcacheconfig_extension_resourcetypes_page_content();
        selectors = config.httpcache_config_extension_selectors_allowed();
        
        log.info("ResourceHttpCacheConfigExtension activated/modified.");
    }
}
