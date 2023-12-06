package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.ApplicationException;
import org.springframework.lang.NonNull;

public interface KeycloakService {
    void createRealm(@NonNull String realm) throws ApplicationException;

    void createAdminUser(String realm, String username, String password, boolean temporary) throws ApplicationException;

    int getRealmUsersCount(String realm) throws ApplicationException;

    int getRealmClientsCount(String realm) throws ApplicationException;

    int getRealmGroupsCount(String realm) throws ApplicationException;

    int getRealmRolesCount(String realm) throws ApplicationException;
}
