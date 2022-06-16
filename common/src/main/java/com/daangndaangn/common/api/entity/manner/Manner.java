package com.daangndaangn.common.api.entity.manner;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.common.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "manner_evaluations")
public class Manner extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id")
    private User evaluator;

    private int score;

    @Builder
    private Manner(Long id, User user, User evaluator, int score) {
        checkArgument(user != null, "user 값은 필수입니다.");
        checkArgument(evaluator != null, "evaluator 값은 필수입니다.");
        checkArgument(-5 <= score && score <= 5, "score는 -5점과 5점 사이여야 합니다.");

        this.id = id;
        this.user = user;
        this.evaluator = evaluator;
        this.score = score;
    }
}
