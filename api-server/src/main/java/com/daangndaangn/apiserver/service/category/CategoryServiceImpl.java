package com.daangndaangn.apiserver.service.category;

import com.daangndaangn.apiserver.entity.category.Category;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Category.class, String.format("categoryId = %s", categoryId)));
    }
}
