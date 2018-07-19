package com.adobe.examples.layoutwcm.core.httpcache.definitions;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * HeaderCacheExtensionConfig
 * <p>
 * Configuration OCD object for the HeaderPageCacheExtension
 * </p>
 */
@ObjectClassDefinition(
    name = "HeaderCacheExtensionConfig - Configuration OCD object for the HeaderPageCacheExtension",
    description = "Extension for the ACS commons HTTP Cache. Based on resource paths, selectors and extensions."
)
public @interface HeaderCacheExtensionConfig {

    @AttributeDefinition(
        name = "Resource path patterns",
        description = "List of resource path patterns (regex) that will be valid for caching"
    )
    String[] resourcePathPatterns();

    @AttributeDefinition(
            name = "Resource types",
            description = "Resources types valid for caching"
    )
    String[] resourceTypes();

    @AttributeDefinition(
        name = "Selector patterns",
        description = "List of selector patterns (regex) that will be valid for caching"
    )
    String[] selectors();

    @AttributeDefinition(
        name = "Extension patterns",
        description = "List of extension patterns (regex) that will be valid for caching"
    )
    String[] extensions();

    @AttributeDefinition(
            name = "Configuration Name",
            description = "The unique identifier of this extension"
    )
    String configName() default "";

    @AttributeDefinition(
            name = "Allowed Cookies",
            description = "Cookie keys that will used to generate a cache key."
    )
    String[] allowedCookieKeys() default {};

    @AttributeDefinition(
            name = "Empty is allowed",
            description = "Cookie keys that will used to generate a cache key."
    )
    boolean emptyCookieKeySetAllowed() default false;
}