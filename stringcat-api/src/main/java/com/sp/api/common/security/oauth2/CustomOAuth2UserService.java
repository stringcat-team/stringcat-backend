package com.sp.api.common.security.oauth2;

import com.sp.domain.code.AuthProviders;
import com.sp.api.common.exception.OAuth2AuthenticationProcessingException;
import com.sp.api.common.security.oauth2.user.OAuth2UserInfo;
import com.sp.api.common.security.oauth2.user.OAuth2UserInfoFactory;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Users> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        Users user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getSocialId().equals(AuthProviders.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getSocialId() + " account. Please use your " + user.getSocialId() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private Users registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Users user = new Users();

        user.setAuthProvider(AuthProviders.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setSocialId(oAuth2UserInfo.getId());
        user.setNickname(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImagePath(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    private Users updateExistingUser(Users existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setNickname(oAuth2UserInfo.getName());
        existingUser.setImagePath(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}