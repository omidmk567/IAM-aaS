package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<UserModel> findUserByEmail(String email);

    UserModel findUserById(UUID uuid) throws UserNotFoundException;

    UserModel saveUser(UserModel userModel);

    Page<UserModel> findAll(Pageable pageable);

    Page<UserModel> findAllCustomers(Pageable pageable);

    Page<UserModel> findAllAdmins(Pageable pageable);

    void deleteUserById(UUID userId);
}
