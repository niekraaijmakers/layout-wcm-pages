package com.adobe.examples.layoutwcm.core.httpcache;

import com.adobe.acs.commons.httpcache.config.HttpCacheConfig;
import com.adobe.acs.commons.httpcache.config.HttpCacheConfigExtension;
import com.adobe.acs.commons.httpcache.exception.HttpCacheKeyCreationException;
import com.adobe.acs.commons.httpcache.exception.HttpCacheRepositoryAccessException;
import com.adobe.acs.commons.httpcache.keys.CacheKey;
import com.adobe.acs.commons.httpcache.keys.CacheKeyFactory;
import com.adobe.examples.layoutwcm.core.httpcache.definitions.HeaderCacheExtensionConfig;
import com.adobe.examples.layoutwcm.core.httpcache.key.CookieKeyValueMap;
import com.adobe.examples.layoutwcm.core.httpcache.key.CookieKeyValueMapBuilder;
import com.adobe.examples.layoutwcm.core.httpcache.key.HeaderPageCacheKey;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * HeaderPageCacheExtension
 * <p>
 * Simple cache extension to only create keys based on the resource path.
 * </p>
 */
@Component(configurationPolicy = ConfigurationPolicy.REQUIRE , service = {HttpCacheConfigExtension.class, CacheKeyFactory.class})
@Designate(ocd = HeaderCacheExtensionConfig.class, factory = true)
public class HeaderPageCacheExtension implements HttpCacheConfigExtension, CacheKeyFactory {

    private List<Pattern> resourcePathPatterns;
    private List<Pattern> selectorPatterns;
    private List<Pattern> extensionPatterns;
    private Set<String> cookieKeys;
    private Set<String> resourceTypes;
    private boolean  emptyCookieKeySetAllowed;

    private String configName;

    @Override
    public boolean accepts(SlingHttpServletRequest request, HttpCacheConfig cacheConfig) throws
            HttpCacheRepositoryAccessException {
        String resourcePath = request.getRequestPathInfo().getResourcePath();

        if(!matches(resourcePathPatterns, resourcePath)){
            return false;
        }

        if(!matches(selectorPatterns, request.getRequestPathInfo().getSelectorString())){
            return false;
        }

        if(!matches(extensionPatterns, request.getRequestPathInfo().getExtension())){
            return false;
        }

        if(CollectionUtils.isNotEmpty(resourceTypes)){
            if(!resourceTypeMatch(resourceTypes, request.getResource())){
                return false;
            }
        }

        if(!emptyCookieKeySetAllowed){
            Set<Cookie> presentCookies = ImmutableSet.copyOf(request.getCookies());
            return containsAtLeastOneMatch(presentCookies);
        }

        return true;
    }

    private boolean resourceTypeMatch(Set<String> resourceTypes, Resource resource) {
        ResourceResolver resourceResolver = resource.getResourceResolver();
        for(String resourceType : resourceTypes){
             if(resourceResolver.isResourceType(resource, resourceType)){
                 return true;
             }
        }
        return false;
    }

    private boolean containsAtLeastOneMatch(Set<Cookie> presentCookies){
        CookieKeyValueMapBuilder builder = new CookieKeyValueMapBuilder(cookieKeys, presentCookies);
        CookieKeyValueMap map = builder.build();
        return !map.isEmpty();
    }

    private boolean matches(List<Pattern> source, String query) {

        if(CollectionUtils.isEmpty(source)){
            return true;
        }

        if(StringUtils.isNotBlank(query)){
            for(Pattern pattern : source){
                if(pattern.matcher(query).find()){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public CacheKey build(final SlingHttpServletRequest slingHttpServletRequest, final HttpCacheConfig cacheConfig)
            throws HttpCacheKeyCreationException {
        ImmutableSet<Cookie> presentCookies = ImmutableSet.copyOf(slingHttpServletRequest.getCookies());
        CookieKeyValueMapBuilder builder = new CookieKeyValueMapBuilder(cookieKeys, presentCookies);
        return new HeaderPageCacheKey(slingHttpServletRequest, cacheConfig, builder.build());
    }


    public CacheKey build(String resourcePath, HttpCacheConfig httpCacheConfig) throws HttpCacheKeyCreationException {
        return new HeaderPageCacheKey(resourcePath, httpCacheConfig, new CookieKeyValueMap());
    }

    @Override
    public boolean doesKeyMatchConfig(CacheKey key, HttpCacheConfig cacheConfig) throws HttpCacheKeyCreationException {

        // Check if key is instance of GroupCacheKey.
        if (!(key instanceof HeaderPageCacheKey)) {
            return false;
        }

        HeaderPageCacheKey thatKey = (HeaderPageCacheKey) key;

        return new HeaderPageCacheKey(thatKey.getUri(), cacheConfig, thatKey.getKeyValueMap()).equals(key);
    }

    @Activate
    protected void activate(HeaderCacheExtensionConfig config){
        this.configName = config.configName();
        this.resourcePathPatterns = compileToPatterns(config.resourcePathPatterns());
        this.extensionPatterns = compileToPatterns(config.extensions());
        this.selectorPatterns = compileToPatterns(config.selectors());
        this.cookieKeys = ImmutableSet.copyOf(config.allowedCookieKeys());
        this.resourceTypes = ImmutableSet.copyOf(config.resourceTypes());
        this.configName = config.configName();
        this.emptyCookieKeySetAllowed = config.emptyCookieKeySetAllowed();
    }

    private List<Pattern> compileToPatterns(final String[] regexes) {
        final List<Pattern> patterns = new ArrayList<>();

        for (String regex : regexes) {
            if (StringUtils.isNotBlank(regex)) {
                patterns.add(Pattern.compile(regex));
            }
        }

        return patterns;
    }
}
