/*
 * #%L
 * ACS AEM Commons Bundle
 * %%
 * Copyright (C) 2015 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
public class MyCustomResourceCacheKey extends AbstractCacheKey implements CacheKey, Serializable
{
    private String selectorString;
    private String extension;
    
    public MyCustomResourceCacheKey(SlingHttpServletRequest request, HttpCacheConfig cacheConfig) throws
            HttpCacheKeyCreationException {
        super(request, cacheConfig);
        selectorString = request.getRequestPathInfo().getSelectorString();
        extension = request.getRequestPathInfo().getExtension();
    }
    
    public MyCustomResourceCacheKey(String uri, HttpCacheConfig cacheConfig) throws HttpCacheKeyCreationException {
        super(uri, cacheConfig);
        selectorString = StringUtils.EMPTY;
        extension = ".html";
    }
    
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        
        if (o == null) {
            return false;
        }
        
        MyCustomResourceCacheKey that = (MyCustomResourceCacheKey) o;
        return new EqualsBuilder()
                .append(getUri(), that.getUri())
                .append(getResourcePath(), that.getResourcePath())
                .append(getSelectorString(), that.getSelectorString())
                .append(getAuthenticationRequirement(), that.getAuthenticationRequirement())
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getUri())
                .append(getAuthenticationRequirement()).toHashCode();
    }
    
    @Override
    public String toString() {
        return this.resourcePath + "." + selectorString + "." + extension + " [AUTH_REQ:" + getAuthenticationRequirement() + "]";
        
    }
    
    @Override
    public String getUri() {
        return this.resourcePath;
    }
    
    public String getSelectorString() {
        return selectorString;
    }
    
    /** For Serialization **/
    private void writeObject(ObjectOutputStream o) throws IOException
    {
        parentWriteObject(o);
        o.writeObject(selectorString);
        o.writeObject(extension);
    }
    
    
    /** For De serialization **/
    private void readObject(ObjectInputStream o)
            throws IOException, ClassNotFoundException {
        
        parentReadObject(o);
        selectorString = (String) o.readObject();
        extension = (String) o.readObject();
    }
}
