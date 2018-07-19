package com.adobe.examples.layoutwcm.core.httpcache.key;

import com.adobe.acs.commons.httpcache.config.HttpCacheConfig;
import com.adobe.acs.commons.httpcache.keys.AbstractCacheKey;
import com.adobe.acs.commons.httpcache.keys.CacheKey;
import com.day.cq.commons.PathInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;

/**
 * HeaderPageCacheKey
 * <p>
 * A key that only contains a resource path
 * </p>
 */
public class HeaderPageCacheKey extends AbstractCacheKey implements CacheKey {

    private final CookieKeyValueMap keyValueMap;

    private final String selector;
    private final String extension;

    public HeaderPageCacheKey(SlingHttpServletRequest request, HttpCacheConfig cacheConfig, CookieKeyValueMap keyValueMap) {
        super(request, cacheConfig);
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        this.keyValueMap = keyValueMap;
        this.selector =      pathInfo.getSelectorString();
        this.extension     = pathInfo.getExtension();
    }

    public HeaderPageCacheKey(String uri, HttpCacheConfig cacheConfig, CookieKeyValueMap keyValueMap){
        super(uri, cacheConfig);
        RequestPathInfo pathInfo = new PathInfo(uri);
        this.selector =      pathInfo.getSelectorString();
        this.extension     = pathInfo.getExtension();
        this.keyValueMap = keyValueMap;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        HeaderPageCacheKey that = (HeaderPageCacheKey) o;
        return new EqualsBuilder()
                .append(resourcePath, that.resourcePath)
                .append(keyValueMap, that.keyValueMap)
                .append(getExtension(), that.getExtension())
                .append(getSelector(), that.getSelector())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(resourcePath)
                .toHashCode();
    }

    @Override
    public String toString(){
        StringBuilder formattedString = new StringBuilder(resourcePath + "." + getSelector() + "." + getExtension());
        formattedString.append(keyValueMap.toString());
        return formattedString.toString();
    }


    public String getSelector() {
        return selector;
    }

    public String getExtension() {
        return extension;
    }

    public CookieKeyValueMap getKeyValueMap() {
        return keyValueMap;
    }
}
