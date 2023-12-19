package com.omidmk.iamapi.mapper;

import com.omidmk.iamapi.controller.dto.Customer;
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
public interface UserMapper {
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    UserModel customerToUserModel(Customer user);

    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    UserModel iamUserToUserModel(IAMUser user);

    IAMUser userModelToIAMUser(UserModel userModel);

    Customer userModelToCustomer(UserModel userModel);

    Customer iamUserToCustomer(IAMUser iamUser);
}