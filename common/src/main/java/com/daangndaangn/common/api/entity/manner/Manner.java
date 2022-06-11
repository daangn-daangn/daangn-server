package com.daangndaangn.common.api.entity.manner;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.common.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

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
        checkArgument(isNotEmpty(user), "user must not be null");
        checkArgument(isNotEmpty(evaluator), "evaluator must not be null");
        checkArgument(-5 <= score && score <= 5, "score value must be -5 ~ 5");

        this.id = id;
        this.user = user;
        this.evaluator = evaluator;
        this.score = score;
    }
}
