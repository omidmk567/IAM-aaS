package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.exception.RealmNotFoundException;
import org.springframework.lang.NonNull;

public interface KeycloakService {
    void createRealm(@NonNull String realm) throws ApplicationException;

    void deleteRealm(String realm) throws RealmNotFoundException;

    void createAdminUser(String realm, String username, String password, boolean temporary) throws ApplicationException;

    void disableRealm(String realm) throws RealmNotFoundException;

    int getRealmUsersCount(String realm) throws RealmNotFoundException;

    int getRealmClientsCount(String realm) throws RealmNotFoundException;

    int getRealmGroupsCount(String realm) throws RealmNotFoundException;

    int getRealmRolesCount(String realm) throws RealmNotFoundException;

    boolean isRealmAvailable(String realm);
}
