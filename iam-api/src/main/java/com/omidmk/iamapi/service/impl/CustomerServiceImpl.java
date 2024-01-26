package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.UserRepository;
import com.omidmk.iamapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public UserModel findUserById(UUID uuid) throws UserNotFoundException {
        return userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserModel saveUser(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<UserModel> findAllCustomers(Pageable pageable) {
        return userRepository.findAllByIsAdmin(false, pageable);
    }

    @Override
    public Page<UserModel> findAllAdmins(Pageable pageable) {
        return userRepository.findAllByIsAdmin(true, pageable);
    }

    @Override
    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }
}
