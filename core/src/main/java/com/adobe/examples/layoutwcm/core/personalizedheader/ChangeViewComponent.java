package com.adobe.examples.layoutwcm.core.personalizedheader;

import com.adobe.acs.commons.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class ChangeViewComponent {
    
    private final static List<Item> STANDARD_ITEMS = new ArrayList<>();
    
    static{
        STANDARD_ITEMS.add(new Item("Select to load different view", ""));
        STANDARD_ITEMS.add(new Item("Anonymous", "ANONYMOUS"));
        STANDARD_ITEMS.add(new Item("Corporate", "CORPORATE"));
        STANDARD_ITEMS.add(new Item("Customer", "CUSTOMER"));
        STANDARD_ITEMS.add(new Item("CoolKids", "COOLKIDS"));
    }
    
    private String value;
    
    public static class Item{
        private boolean selected;
        private String title;
        private String value;
    
        public Item(String title, String value) {
            this.title = title;
            this.value = value;
        }
    
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    
        public String getSelected() {
            return selected ? "selected" : "";
        }
    
        public String getTitle() {
            return title;
        }
    
        public String getValue() {
            return value;
        }
    }
    
    private List<Item> compiledList = new ArrayList<>(STANDARD_ITEMS);
    
    @Self
    private SlingHttpServletRequest request;
    
    @PostConstruct
    public void init(){
        Cookie cookie = CookieUtil.getCookie(request, "USER_GROUP");
        if(cookie != null){
           value = cookie.getValue();
        }else{
            value = StringUtils.EMPTY;
        }
        compiledList.stream().filter( item -> item.getValue().equals(value)).findFirst().ifPresent(item -> item.setSelected(true));
    }
    
    public List<Item> getCookieViews(){
        return compiledList;
    }
    
    public String getTitle(){
        return value;
    }
    
}
