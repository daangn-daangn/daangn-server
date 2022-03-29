package com.daangndaangn.apiserver.entity.user;

import com.daangndaangn.apiserver.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.apiserver.security.jwt.Jwt;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long oauthId;

    @Embedded
    @Column(nullable = false, length = 50)
    private Email email;

    @Column(length = 20)
    private String nickname;

    @Column(length = 20)
    private Location location;

    @Column(length = 500)
    private String profileUrl;

    @Column(nullable = false)
    private double manner;

    @Builder
    private User(Long oauthId, Email email, String nickname, Location location, String profileUrl) {
        this.oauthId = oauthId;
        this.email = email;
        this.nickname = nickname;
        this.location = location;
        this.profileUrl = profileUrl;
        this.manner = 0.0;
    }

    public String createApiToken(Jwt jwt, String[] roles) {
        Jwt.Claims claims = Jwt.Claims.of(id, oauthId, nickname, location, manner, email, roles);
        return jwt.createNewToken(claims);
    }
}
