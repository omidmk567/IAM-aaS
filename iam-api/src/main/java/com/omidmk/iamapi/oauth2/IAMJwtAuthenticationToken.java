package com.omidmk.iamapi.oauth2;

import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.oauth2.model.IAMUser;
import com.omidmk.iamapi.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Transient
public class IAMJwtAuthenticationToken extends JwtAuthenticationToken {
    private final UserService userService;

    public IAMJwtAuthenticationToken(UserService userService, Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities, jwt.getClaim("preferred_username"));
        this.userService = userService;
    }

    @Override
    public IAMUser getPrincipal() {
        UserModel userModel = extractInfo(getToken().getClaimAsString("email"));
        var iamUser = new IAMUser();
        iamUser.setId(userModel.getId());
        iamUser.setEmail(userModel.getEmail());
        iamUser.setBalance(userModel.getBalance());
        iamUser.setDeployments(userModel.getDeployments());
        return iamUser;
    }

    private UserModel extractInfo(String email) {
        UserModel userModel = userService.findUserByEmail(email).orElseGet(() -> new UserModel(email));
        return userService.saveUser(userModel);
    }
}
