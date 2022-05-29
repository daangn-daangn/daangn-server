package com.daangndaangn.common.api.repository.category;

import com.daangndaangn.common.api.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
