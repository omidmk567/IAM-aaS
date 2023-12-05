package com.omidmk.iamapi.service;

import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
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
