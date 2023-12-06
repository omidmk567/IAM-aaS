package com.omidmk.iamapi.config;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {
    private final KeycloakProperties keycloakProperties;

    public KeycloakConfiguration(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getBaseUrl())
                .realm(keycloakProperties.getMasterRealm())
                .clientId(keycloakProperties.getMasterClientId())
                .username(keycloakProperties.getMasterUsername())
                .password(keycloakProperties.getMasterPassword())
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
                .build();
    }
}
