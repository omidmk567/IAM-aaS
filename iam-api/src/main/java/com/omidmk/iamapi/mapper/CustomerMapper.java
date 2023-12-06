package com.omidmk.iamapi.mapper;

import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.oauth2.model.IAMUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CustomerMapper {
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    UserModel iamUserToUserModel(IAMUser user);

    IAMUser userModelToIAMUser(UserModel userModel);
}
