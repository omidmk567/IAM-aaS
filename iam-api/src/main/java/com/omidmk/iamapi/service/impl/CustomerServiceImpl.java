package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.UserRepository;
import com.omidmk.iamapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;

    @Override
    public Optional<UserModel> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<UserModel> findUserById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public UserModel saveUser(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }
}
