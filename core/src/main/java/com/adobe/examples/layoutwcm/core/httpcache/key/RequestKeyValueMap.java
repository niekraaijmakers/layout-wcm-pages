package com.adobe.examples.layoutwcm.core.httpcache.key;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;

/**
 * RequestKeyValueMap
 * <p>
 * Basically a HashMap with a nice toString function for the CookieCacheKey to hold cookies into.
 * </p>
 */
public class RequestKeyValueMap extends HashMap<String,String> {

    private final String type;

    public RequestKeyValueMap(String type) {
        this.type = type;
    }


    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        Iterator<Entry<String,String>> entries = entrySet().iterator();

        if(!isEmpty()){
            result.append("[" + type + ":");
            while (entries.hasNext()) {

                Entry<String, String> entry = entries.next();
                String key = entry.getKey();
                String value = entry.getValue();

                if (StringUtils.isNotEmpty(value)) {
                    result.append(key + "=" + value);
                } else {
                    //cookie is only present, but no value.
                    result.append(key);
                }

                if (entries.hasNext()) {
                    result.append(",");
                }

            }
            result.append("]");
        }


        return result.toString();

    }
}
