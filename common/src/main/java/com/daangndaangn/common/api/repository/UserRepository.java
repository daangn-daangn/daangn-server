package com.daangndaangn.common.api.repository;

import com.daangndaangn.common.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthId(Long oauthId);

    boolean existsUserByNickname(String nickname);

    Optional<User> findById(Long userId);
}
