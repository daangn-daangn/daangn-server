package com.daangndaangn.apiserver.entity.user;

import com.daangndaangn.apiserver.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.apiserver.security.jwt.Jwt;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

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

    @Column(length = 20)
    private String nickname;

    @Column(length = 20)
    private Location location;

    @Column(length = 500)
    private String profileUrl;

    @Column(nullable = false)
    private double manner;

    public void update(Long oauthId, String nickname, Location location, String profileUrl) {
        this.oauthId = ObjectUtils.isEmpty(oauthId) ? this.oauthId : oauthId;
        this.nickname = ObjectUtils.isEmpty(nickname) ? this.nickname : nickname;
        this.location = ObjectUtils.isEmpty(location) ? this.location : location;
        this.profileUrl = ObjectUtils.isEmpty(profileUrl) ? this.profileUrl : profileUrl;
    }

    @Builder
    private User(Long oauthId, String profileUrl) {
        this.oauthId = oauthId;
        this.profileUrl = profileUrl;
        this.manner = 0.0;
    }

    public String createApiToken(Jwt jwt, String[] roles) {
        Jwt.Claims claims = Jwt.Claims.of(id, oauthId, nickname, location, manner, roles);
        return jwt.createNewToken(claims);
    }
}
