package com.omidmk.iamapi.mapper;

import com.omidmk.iamapi.controller.dto.Customer;
import com.omidmk.iamapi.model.user.UserModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {TicketMapper.class, DeploymentMapper.class}
)
public interface UserMapper {
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    UserModel customerToUserModel(Customer user);

    List<Customer> userModelListToCustomerList(List<UserModel> userModels);

    Customer userModelToCustomer(UserModel userModel);
}
