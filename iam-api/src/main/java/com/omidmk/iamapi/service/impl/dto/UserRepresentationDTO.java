package com.omidmk.iamapi.service.impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.keycloak.representations.idm.UserProfileMetadata;
import org.keycloak.representations.idm.UserRepresentation;


public class UserRepresentationDTO extends UserRepresentation {
    @JsonIgnore
    private UserProfileMetadata userProfileMetadata;
}