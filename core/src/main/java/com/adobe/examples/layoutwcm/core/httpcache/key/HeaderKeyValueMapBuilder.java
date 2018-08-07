package com.adobe.examples.layoutwcm.core.httpcache.key;

import org.apache.sling.api.SlingHttpServletRequest;

import javax.servlet.http.Cookie;
import java.util.Enumeration;
import java.util.Set;


public class HeaderKeyValueMapBuilder {
    private final Set<String> allowedKeys;
    private final SlingHttpServletRequest slingHttpServletRequest;
    private final RequestKeyValueMap keyValueMap = new RequestKeyValueMap("HeaderKeyValueMap");

    public HeaderKeyValueMapBuilder(Set<String> allowedKeys, SlingHttpServletRequest slingHttpServletRequest){
        this.allowedKeys = allowedKeys;
        this.slingHttpServletRequest = slingHttpServletRequest;
    }


    public RequestKeyValueMap build(){

        Enumeration<String> headerNames = slingHttpServletRequest.getHeaderNames();

        while(headerNames.hasMoreElements()){
            String headerName=  headerNames.nextElement();
            if(allowedKeys.contains(headerName)){
                keyValueMap.put(headerName, slingHttpServletRequest.getHeader(headerName));
            }
        }
        return keyValueMap;
    }
}
