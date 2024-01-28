package com.omidmk.iamapi.oauth2;

import com.omidmk.iamapi.mapper.UserMapper;
import com.omidmk.iamapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class IAMJwtAuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final CustomerService customerService;
    private final UserMapper userMapper;

    @Value("${app.iam-aas.jwt.role.converter.clients}")
    private List<String> clientIds;

    @Value("${app.iam-aas.customer-initial-credit:100}")
    private Long customerInitialCredit;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream
                .concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream())
                .toList();
        return new IAMJwtAuthenticationToken(customerService, userMapper, customerInitialCredit, jwt, authorities);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, ?> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return Collections.emptyList();
        Stream<? extends GrantedAuthority> rolesStream = Stream.of();
        for (String clientId : clientIds) {
            Map<?, ?> resource;
            Collection<?> resourceRoles;
            if ((resource = (Map<?, ?>) resourceAccess.get(clientId)) != null && (resourceRoles = (Collection<?>) resource.get("roles")) != null) {
                Stream<SimpleGrantedAuthority> roles = resourceRoles
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_%s".formatted(role)));
                rolesStream = Stream.concat(rolesStream, roles);
            }
        }
        return rolesStream.toList();
    }
}