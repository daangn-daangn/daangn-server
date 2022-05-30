package com.daangndaangn.apiserver.service.category;

import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Long create(String name) {
        checkArgument(isNotEmpty(name), "category name must not be null");

        Category category = Category.from(name);
        return categoryRepository.save(category).getId();
    }

    @Override
    public Category getCategory(Long id) {
        checkArgument(id != null, "category id must not be null");

        return categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Category.class, String.format("categoryId = %s", id)));
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
