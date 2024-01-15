package com.gmail.j2ee.ecommerce.security.oauth2;

import com.gmail.j2ee.ecommerce.domain.User;
import com.gmail.j2ee.ecommerce.enums.AuthProvider;
import com.gmail.j2ee.ecommerce.enums.Role;
import com.gmail.j2ee.ecommerce.service.AuthenticationService;
import com.gmail.j2ee.ecommerce.service.UserService;
import com.gmail.j2ee.ecommerce.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Lazy private final AuthenticationService authenticationService;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        User user = userService.getUserInfo(oAuth2UserInfo.getEmail());

        if (user == null) {
            user = authenticationService.registerOauth2User(provider, oAuth2UserInfo);
        } else {
            if(user.getProvider() == AuthProvider.LOCAL)
                throw new OAuth2AuthenticationException(new OAuth2Error("User already registered"));
            user = authenticationService.updateOauth2User(user, provider, oAuth2UserInfo);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }
}
