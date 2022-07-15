package com.daangndaangn.apiserver.service.category;

import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.repository.category.CategoryRepository;
import com.daangndaangn.common.error.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category mockCategory;

    @BeforeEach
    public void init() {
        mockCategory = Category.builder().id(1L).name("mockCategory").build();
    }

    @Test
    @DisplayName("name에 해당하는 카테고리를 생성할 수 있다")
    void create() {
        //given
        given(categoryRepository.save(any())).willReturn(mockCategory);

        //when
        Long categoryId = categoryService.create(mockCategory.getName());

        //then
        assertThat(categoryId).isEqualTo(mockCategory.getId());
        verify(categoryRepository).save(any());
    }

    @Test
    @DisplayName("id에 해당하는 카테고리를 반환한다")
    void getCategory1() {
        //given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockCategory));

        //when
        Category category = categoryService.getCategory(mockCategory.getId());

        //then
        assertThat(category).isEqualTo(mockCategory);
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    @DisplayName("id에 해당하는 카테고리가 없을 시 예외를 반환한다")
    void getCategory2() {
        //given
        Long invalidId = 5L;

        //when
        Assertions.assertThatThrownBy(() -> categoryService.getCategory(invalidId))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    @DisplayName("전체 카테고리 목록을 반환한다")
    void getCategories() {
        //given
        given(categoryRepository.findAll()).willReturn(List.of(mockCategory, mockCategory, mockCategory));

        //when
        List<Category> categories = categoryService.getCategories();

        //then
        assertThat(categories.size()).isEqualTo(3);
        verify(categoryRepository).findAll();
    }
}