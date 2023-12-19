package com.omidmk.iamapi.oauth2;

import com.omidmk.iamapi.mapper.UserMapper;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.oauth2.model.IAMUser;
import com.omidmk.iamapi.service.CustomerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Transient
public class IAMJwtAuthenticationToken extends JwtAuthenticationToken {
    private final CustomerService customerService;
    private final UserMapper userMapper;

    public IAMJwtAuthenticationToken(CustomerService customerService, UserMapper userMapper, Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities, jwt.getClaim("preferred_username"));
        this.customerService = customerService;
        this.userMapper = userMapper;
    }

    @Override
    public IAMUser getPrincipal() {
        UserModel userModel = extractInfo(getToken().getClaimAsString("email"));
        return userMapper.userModelToIAMUser(userModel);
    }

    private UserModel extractInfo(String email) {
        UserModel userModel = customerService.findUserByEmail(email).orElseGet(() -> new UserModel(email));
        return customerService.saveUser(userModel);
    }
}
