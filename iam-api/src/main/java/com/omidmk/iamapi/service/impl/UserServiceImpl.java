package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.UserRepository;
import com.omidmk.iamapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Optional<UserModel> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserModel> findUserById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public UserModel saveUser(UserModel userModel) {
        return userRepository.save(userModel);
    }
}
