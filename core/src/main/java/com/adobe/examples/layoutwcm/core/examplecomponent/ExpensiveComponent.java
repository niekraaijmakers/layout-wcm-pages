package com.adobe.examples.layoutwcm.core.examplecomponent;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class ExpensiveComponent {
    
    @PostConstruct
    public void init(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public String getUrl(){
        return "https://adobe-consulting-services.github.io/acs-aem-commons/features/fast-action-manager/thumbnail.png";
    }
}
