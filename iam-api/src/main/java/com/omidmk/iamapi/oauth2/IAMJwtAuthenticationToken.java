package com.omidmk.iamapi.oauth2;

import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.service.CustomerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Transient
public class IAMJwtAuthenticationToken extends JwtAuthenticationToken {
    private final CustomerService customerService;
    private final float customerInitialCredit;

    public IAMJwtAuthenticationToken(CustomerService customerService, float customerInitialCredit, Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities, jwt.getClaim("preferred_username"));
        this.customerService = customerService;
        this.customerInitialCredit = customerInitialCredit;
    }

    @Override
    public UserModel getPrincipal() {
        return constructUser();
    }

    private UserModel constructUser() {
        final boolean isAdmin = getAuthorities().stream().anyMatch(auth -> auth.getAuthority().contains("admin"));
        final String email = getToken().getClaimAsString("email");
        final String firstName = getToken().getClaimAsString("given_name");
        final String lastName = getToken().getClaimAsString("family_name");
        final UserModel userModel = customerService.findUserByEmail(email).orElseGet(() -> new UserModel(email, isAdmin, firstName, lastName, customerInitialCredit));

        if (userModel.getFirstName() == null && firstName != null)
            userModel.setFirstName(firstName);

        if (userModel.getLastName() == null && lastName != null)
            userModel.setLastName(lastName);

        userModel.setIsAdmin(isAdmin);

        return customerService.saveUser(userModel);
    }
}
