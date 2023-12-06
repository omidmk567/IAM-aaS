package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.*;
import com.omidmk.iamapi.service.KeycloakService;
import com.omidmk.iamapi.service.impl.dto.UserRepresentationDTO;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {
    private final Keycloak keycloak;

    public KeycloakServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createRealm(@NonNull String realm) throws ApplicationException {
        boolean previousRealm = keycloak.realms()
                .findAll()
                .stream()
                .anyMatch(it -> it.getRealm().equals(realm));
        if (previousRealm) {
            log.error("Existing realm found with name: {}", realm);
            throw new RealmAlreadyExistException();
        }

        var realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(realm);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setUsers(List.of());
        keycloak.realms().create(realmRepresentation);
        log.info("Realm with name {} created successfully", realm);
    }

    public void createAdminUser(String realm, String username, String password, boolean temporary) throws ApplicationException {
        keycloak.realms()
                .findAll()
                .stream()
                .filter(it -> it.getRealm().equals(realm))
                .findAny()
                .orElseThrow(RealmNotFoundException::new);

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userResource = realmResource.users();
        if (!userResource.search(username, true).isEmpty()) {
            log.error("Duplicate user found with username: {}", username);
            throw new UserAlreadyExistException();
        }

        var userRepresentation = new UserRepresentationDTO();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(username);

        var credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        credentialRepresentation.setTemporary(temporary);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        try (Response response = userResource.create(userRepresentation)) {
            if ((response.getStatusInfo().getStatusCode() / 100) != 2) {
                log.error("Could not create user on keycloak: {}", response.readEntity(String.class));
                throw new UserCreationException();
            }
            String userId = CreatedResponseUtil.getCreatedId(response);
            log.info("Successfully created Admin user for realm: {} with username: {}, userId: {}", realm, username, userId);
            ClientRepresentation realmManagementClient = realmResource.clients().findByClientId("realm-management").get(0);
            RoleScopeResource roleScopeResource = userResource.get(userId).roles().clientLevel(realmManagementClient.getId());
            List<RoleRepresentation> availableRoles = roleScopeResource.listAvailable()
                    .stream()
                    .filter(role -> role.getContainerId().equals(realmManagementClient.getId()))
                    .toList();
            roleScopeResource.add(availableRoles);
            log.debug("Access granted to admin user: {} on realm: {}", username, realm);
        }
    }

    public int getRealmUsersCount(String realm) throws ApplicationException {
        keycloak.realms()
                .findAll()
                .stream()
                .filter(it -> it.getRealm().equals(realm))
                .findAny()
                .orElseThrow(RealmNotFoundException::new);

        return keycloak.realm(realm).users().count();
    }

    public int getRealmClientsCount(String realm) throws ApplicationException {
        keycloak.realms()
                .findAll()
                .stream()
                .filter(it -> it.getRealm().equals(realm))
                .findAny()
                .orElseThrow(RealmNotFoundException::new);

        return keycloak.realm(realm).clients().findAll().size();
    }

    public int getRealmGroupsCount(String realm) throws ApplicationException {
        keycloak.realms()
                .findAll()
                .stream()
                .filter(it -> it.getRealm().equals(realm))
                .findAny()
                .orElseThrow(RealmNotFoundException::new);

        return keycloak.realm(realm).groups().count().size(); // todo: check out this method
    }

    public int getRealmRolesCount(String realm) throws ApplicationException {
        keycloak.realms()
                .findAll()
                .stream()
                .filter(it -> it.getRealm().equals(realm))
                .findAny()
                .orElseThrow(RealmNotFoundException::new);

        return keycloak.realm(realm).roles().list().size();
    }

}