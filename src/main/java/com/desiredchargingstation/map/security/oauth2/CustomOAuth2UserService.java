package com.desiredchargingstation.map.security.oauth2;

import com.desiredchargingstation.map.security.oauth2.util.CustomOAuth2UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final CustomOAuth2UserServiceUtils customOAuth2UserServiceUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return customOAuth2UserServiceUtils.getProcessedOAuth2User(oAuth2UserRequest, oAuth2User);
    }
}
