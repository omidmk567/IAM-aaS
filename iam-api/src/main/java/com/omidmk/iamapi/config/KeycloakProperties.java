package com.omidmk.iamapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keycloak")
@Data
public class KeycloakProperties {
    private String baseUrl;
    private String masterRealm;
    private String masterUsername;
    private String masterPassword;
    private String masterClientId;
    private String authRealm;
}
