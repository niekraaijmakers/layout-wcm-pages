package com.adobe.examples.layoutwcm.core.httpcache;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.adobe.acs.commons.httpcache.invalidator.CacheInvalidationJobConstants.PAYLOAD_KEY_DATA_CHANGE_PATH;
import static com.adobe.acs.commons.httpcache.invalidator.CacheInvalidationJobConstants.TOPIC_HTTP_CACHE_INVALIDATION_JOB;
import static java.util.Collections.singletonMap;

/**
 * Automated Memory Cache Flusher
 * <p>
 * Helps reducing maintenance by automatically flushing the ACS commons memory cache by implementing ResourceChangeListener.
 * </p>
 */
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
// @formatter:on
public class AutomatedLayoutPageInvalidator implements ResourceChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(AutomatedLayoutPageInvalidator.class);

    private static final Pattern PATTERN = Pattern.compile("((/content/layout-wcm/(headers|footers)/[a-zA-Z0-9_-]{1,99}))(.*)/(.*)");
    
    @Reference
    private JobManager jobManager;
    @Override
    public void onChange(List<ResourceChange> changes) {
        for (ResourceChange change : changes) {

            LOG.debug("Attempting to extract header path from: {}", change.getPath());
            String layoutPagePath = extractHeaderPath(change.getPath());

            if (StringUtils.isNotEmpty(layoutPagePath)) {
                LOG.debug("Flushing path {}", layoutPagePath);
                jobManager.addJob(TOPIC_HTTP_CACHE_INVALIDATION_JOB, singletonMap(PAYLOAD_KEY_DATA_CHANGE_PATH,layoutPagePath));
            }
            
        }
    }

    private String extractHeaderPath(String layoutPageChildResourcePath) {
        Matcher matcher = PATTERN.matcher(layoutPageChildResourcePath);

        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
