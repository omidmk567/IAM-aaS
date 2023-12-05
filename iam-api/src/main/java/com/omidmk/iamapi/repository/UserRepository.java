package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends ListCrudRepository<UserModel, UUID>, PagingAndSortingRepository<UserModel, UUID> {
    Optional<UserModel> findByEmail(String email);
}
