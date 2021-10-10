package com.desiredchargingstation.map.security.oauth2;

import com.desiredchargingstation.map.security.oauth2.util.CustomOAuth2UserServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final CustomOAuth2UserServiceUtils customOAuth2UserServiceUtils;

    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(oidcUserRequest);
        return (OidcUser) customOAuth2UserServiceUtils.getProcessedOAuth2User(oidcUserRequest, oidcUser);
    }
}
