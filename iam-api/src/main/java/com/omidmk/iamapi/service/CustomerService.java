package com.omidmk.iamapi.service;

import com.omidmk.iamapi.model.user.UserModel;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<UserModel> findUserByEmail(String email);

    Optional<UserModel> findUserById(UUID uuid);

    UserModel saveUser(UserModel userModel);
}
