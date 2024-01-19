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
    private final Long customerInitialCredit;

    public IAMJwtAuthenticationToken(CustomerService customerService, UserMapper userMapper, Long customerInitialCredit, Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities, jwt.getClaim("preferred_username"));
        this.customerService = customerService;
        this.userMapper = userMapper;
        this.customerInitialCredit = customerInitialCredit;
    }

    @Override
    public IAMUser getPrincipal() {
        UserModel userModel = constructUser();
        return userMapper.userModelToIAMUser(userModel);
    }

    private UserModel constructUser() {
        final String email = getToken().getClaimAsString("email");
        final String firstName = getToken().getClaimAsString("given_name");
        final String lastName = getToken().getClaimAsString("family_name");
        final UserModel userModel = customerService.findUserByEmail(email).orElseGet(() -> new UserModel(email, firstName, lastName, customerInitialCredit));

        if (userModel.getFirstName() == null && firstName != null)
            userModel.setFirstName(firstName);

        if (userModel.getLastName() == null && lastName != null)
            userModel.setLastName(lastName);

        return customerService.saveUser(userModel);
    }
}
