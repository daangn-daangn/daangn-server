package com.daangndaangn.common.api.entity.category;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import lombok.AccessLevel;
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

    private Category(String name) {
        checkArgument(isNotEmpty(name), "category name must not be null");
        this.name = name;
    }

    public static Category from(String name) {
        return new Category(name);
    }
}
