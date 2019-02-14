package com.adobe.examples.layoutwcm.core.httpcache.key;

import com.adobe.acs.commons.httpcache.config.HttpCacheConfig;
import com.adobe.acs.commons.httpcache.exception.HttpCacheKeyCreationException;
import com.adobe.acs.commons.httpcache.keys.AbstractCacheKey;
import com.adobe.acs.commons.httpcache.keys.CacheKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Cachekey that differentiates based on:
 * <p>Resource path</p>
 * <p>Authentication requirement</p>
 */
public class MyCustomResourceKey extends AbstractCacheKey implements CacheKey, Serializable
{
    
    private final String selectorString;
    
    public MyCustomResourceKey(SlingHttpServletRequest request, HttpCacheConfig cacheConfig) throws
            HttpCacheKeyCreationException {
        super(request, cacheConfig);
    
        RequestPathInfo pathInfo = request.getRequestPathInfo();
        selectorString = pathInfo.getSelectorString();
    }
    
    public MyCustomResourceKey(String uri, HttpCacheConfig cacheConfig) throws HttpCacheKeyCreationException {
        super(uri, cacheConfig);
        selectorString = StringUtils.EMPTY;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        
        if (o == null) {
            return false;
        }
    
        MyCustomResourceKey that = (MyCustomResourceKey) o;
        return new EqualsBuilder()
                .append(getUri(), that.getUri())
                .append(getResourcePath(), that.getResourcePath())
                .append(selectorString, that.getSelectorString())
                .append(getAuthenticationRequirement(), that.getAuthenticationRequirement())
                .isEquals();
    }
    
    private String getSelectorString() {
        return selectorString;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getUri())
                .append(getAuthenticationRequirement()).toHashCode();
    }
    
    @Override
    public String toString() {
        return this.resourcePath + "." + selectorString + " [AUTH_REQ:" + getAuthenticationRequirement() + "]";
        
    }
    
    @Override
    public String getUri() {
        return this.resourcePath;
    }
    
    /** For Serialization **/
    private void writeObject(ObjectOutputStream o) throws IOException
    {
        parentWriteObject(o);
    }
    
    
    /** For De serialization **/
    private void readObject(ObjectInputStream o)
            throws IOException, ClassNotFoundException {
        
        parentReadObject(o);
    }
}
