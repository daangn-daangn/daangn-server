package com.daangndaangn.common.api.entity.user;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
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

    public void increaseManner() {
        this.manner = Math.min(100.0, this.manner + 0.1);
    }

    public void decreaseManner() {
        this.manner = Math.max(0.0, this.manner - 0.1);
    }

    @Builder
    private User(Long oauthId, String profileUrl) {
        this.oauthId = oauthId;
        this.profileUrl = profileUrl;
        this.manner = 36.5;
    }
}
