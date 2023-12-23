package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.controller.dto.Customer;
import com.omidmk.iamapi.controller.dto.UpdateCustomerDTO;
import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.mapper.UserMapper;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.omidmk.iamapi.config.SwaggerConfig.BEARER_TOKEN_SECURITY_SCHEME;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final CustomerService customerService;
    private final UserMapper userMapper;

    @GetMapping("/customers")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Customer> getAllCustomers() {
        return userMapper.userModelListToCustomerList(customerService.findAll());
    }

    @GetMapping("/customers/{userId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Customer getSingleCustomer(@PathVariable UUID userId) throws ApplicationException {
        Optional<UserModel> foundUser = customerService.findUserById(userId);
        if (foundUser.isEmpty())
            throw new UserNotFoundException();

        return userMapper.userModelToCustomer(foundUser.get());
    }

    @PutMapping("/customers/{userId}")
    public Customer updateCustomer(@PathVariable UUID userId, @RequestBody UpdateCustomerDTO updateCustomerDTO) throws ApplicationException {
        if (updateCustomerDTO == null || updateCustomerDTO.getId() == null || !updateCustomerDTO.getId().equals(userId))
            throw new UserNotFoundException();

        Optional<UserModel> foundUser = customerService.findUserById(userId);
        if (foundUser.isEmpty())
            throw new UserNotFoundException();

        UserModel userModel = foundUser.get();
        userModel.setFirstName(updateCustomerDTO.getFirstName());
        userModel.setLastName(updateCustomerDTO.getLastName());
        userModel.setBalance(updateCustomerDTO.getBalance());
        return userMapper.userModelToCustomer(customerService.saveUser(userModel));
    }

    @DeleteMapping("/customers/{userId}")
    public void deleteCustomer(@PathVariable UUID userId) throws ApplicationException {
        Optional<UserModel> foundUser = customerService.findUserById(userId);
        if (foundUser.isEmpty())
            throw new UserNotFoundException();

        customerService.deleteUserById(foundUser.get().getId());
    }
}
