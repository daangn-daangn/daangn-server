package com.daangndaangn.apiserver.service.category;

import com.daangndaangn.common.api.entity.category.Category;

import java.util.List;

public interface CategoryService {

    Long create(String name);

    Category getCategory(Long id);

    List<Category> getCategories();
}
