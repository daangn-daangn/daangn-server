package com.daangndaangn.common.api.entity.user;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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

    @Column
    private Location location;

    @Column(length = 250)
    private String profileUrl;

    @Column(nullable = false)
    private double manner;

    public void update(String nickname, Location location) {
        checkArgument(
                isEmpty(nickname) || nickname.length() <= 20,
                "닉네임은 20자 이하여야 합니다.");
        checkArgument(
                location == null || isEmpty(location.getAddress()) || location.getAddress().length() <= 20,
                "주소는 20자 이하여야 합니다.");

        this.nickname = isEmpty(nickname) ? this.nickname : nickname;
        this.location = (location == null || isEmpty(location.getAddress())) ? this.location : location;
    }

    public void update(Long oauthId, String nickname, Location location, String profileUrl) {
        checkArgument(
                isEmpty(nickname) || nickname.length() <= 20,
                "닉네임은 20자 이하여야 합니다.");
        checkArgument(
                location == null || isEmpty(location.getAddress()) || location.getAddress().length() <= 20,
                "주소는 20자 이하여야 합니다.");
        checkArgument(
                isEmpty(profileUrl) || profileUrl.length() <= 250,
                "프로필 URL은 250자 이하여야 합니다.");

        this.oauthId = oauthId == null ? this.oauthId : oauthId;
        this.nickname = nickname == null ? this.nickname : nickname;
        this.location = (location == null || isEmpty(location.getAddress())) ? this.location : location;
        this.profileUrl = isEmpty(profileUrl) ? this.profileUrl : profileUrl;
    }

    public void increaseManner() {
        this.manner = Math.min(100.0, this.manner + 0.1);
    }

    public void decreaseManner() {
        this.manner = Math.max(0.0, this.manner - 0.1);
    }

    public boolean isEmptyLocation() {
        return location == null || isEmpty(location.getAddress());
    }

    @Builder
    private User(Long id, Long oauthId, String profileUrl, Location location) {
        checkArgument(oauthId != null, "oauthId 값은 필수입니다.");
        checkArgument(
                isEmpty(profileUrl) || profileUrl.length() <= 250,
                "프로필 URL은 250자 이하여야 합니다.");

        this.id = id;
        this.oauthId = oauthId;
        this.profileUrl = profileUrl;
        this.location = location;
        this.manner = 36.5;
    }
}
