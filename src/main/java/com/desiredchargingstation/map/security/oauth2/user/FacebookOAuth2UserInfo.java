package com.desiredchargingstation.map.security.oauth2.user;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        if (attributes.containsKey("picture")) {
            Map<String, Object> pictureMap = (Map<String, Object>) attributes.get("picture");
            if (pictureMap.containsKey("data")) {
                Map<String, Object> dataMap = (Map<String, Object>) pictureMap.get("data");
                if (dataMap.containsKey("url")) {
                    return (String) dataMap.get("url");
                }
            }
        }
        return null;
    }
}
