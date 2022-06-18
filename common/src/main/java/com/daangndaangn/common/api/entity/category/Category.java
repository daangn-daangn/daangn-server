package com.daangndaangn.common.api.entity.category;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
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
@Table(name = "categories")
public class Category extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Builder
    private Category(Long id, String name) {
        checkArgument(isNotEmpty(name), "category name 값은 필수입니다.");
        checkArgument(name.length() <= 20, "카테고리명은 20자 이하여야 합니다.");

        this.id = id;
        this.name = name;
    }
}
