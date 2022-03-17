package com.daangndaangn.apiserver.entity.user;

import com.daangndaangn.apiserver.entity.AuditingCreateUpdateEntity;
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

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 20)
    private String location;

    @Column(length = 500)
    private String profileUrl;

    @Column(nullable = false)
    private long manner;

    @Builder
    private User(String email, String nickname, String location, String profileUrl) {
        this.email = email;
        this.nickname = nickname;
        this.location = location;
        this.profileUrl = profileUrl;
        this.manner = 0;
    }
}
