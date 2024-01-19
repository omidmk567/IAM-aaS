package com.omidmk.iamapi.service;

import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<UserModel> findUserByEmail(String email);

    Optional<UserModel> findUserById(UUID uuid);

    UserModel saveUser(UserModel userModel);

    Page<UserModel> findAll(Pageable pageable);

    void deleteUserById(UUID userId);
}
