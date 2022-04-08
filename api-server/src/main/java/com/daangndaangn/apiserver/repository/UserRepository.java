package com.daangndaangn.apiserver.repository;

import com.daangndaangn.apiserver.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthId(Long oauthId);
    Optional<User> findById(Long userId);
}
