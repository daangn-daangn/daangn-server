package com.daangndaangn.apiserver.service.category;

import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.repository.category.CategoryRepository;
import com.daangndaangn.common.error.NotFoundException;
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
        checkArgument(isNotEmpty(name), "category name 값은 필수입니다.");

        Category category = Category.from(name);
        return categoryRepository.save(category).getId();
    }

    @Override
    public Category getCategory(Long id) {
        checkArgument(id != null, "category id 값은 필수입니다.");

        return categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Category.class, String.format("categoryId = %s", id)));
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
