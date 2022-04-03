package com.daangndaangn.apiserver.repository;

import com.daangndaangn.apiserver.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long categoryId);
}
