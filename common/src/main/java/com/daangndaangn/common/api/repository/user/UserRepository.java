package com.daangndaangn.common.api.repository.user;

import com.daangndaangn.common.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

    Optional<User> findByOauthId(Long oauthId);

    Optional<User> findById(Long id);
}
