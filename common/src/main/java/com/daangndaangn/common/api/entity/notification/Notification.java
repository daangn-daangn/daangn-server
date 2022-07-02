package com.daangndaangn.common.api.entity.notification;

import com.daangndaangn.common.api.entity.AuditingCreateEntity;
import com.daangndaangn.common.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notifications")
public class Notification extends AuditingCreateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false, length = 50)
    private String identifier;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private boolean isValid;

    public void update(boolean read) {
        this.isRead = read;
    }

    public void remove() {
        this.isValid = false;
    }

    @Builder
    private Notification(Long id, User user, NotificationType notificationType, String identifier) {
        checkArgument(user != null, "사용자 값은 필수입니다.");
        checkArgument(notificationType != null, "리뷰 작성자 정보값은 필수입니다.");
        checkArgument(isNotEmpty(identifier), "식별자값은 필수입니다.");
        checkArgument(identifier.length() <= 50, "식별자의 길이는 50자 이하여야 합니다.");

        this.id = id;
        this.user = user;
        this.notificationType = notificationType;
        this.identifier = identifier;
        this.isValid = true;
    }
}
