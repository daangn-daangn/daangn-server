package com.daangndaangn.apiserver.controller.category;

import com.daangndaangn.apiserver.controller.category.CategoryResponse.GetResponse;
import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@RequestMapping("/api/categories")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록을 반환한다.
     *
     * GET /api/categories
     */
    @GetMapping
    public ApiResult<List<GetResponse>> getCategories() {
        List<GetResponse> responses = categoryService.getCategories()
                .stream()
                .map(GetResponse::from)
                .collect(toList());

        return OK(responses);
    }

    /**
     * id에 해당하는 카테고리를 반환한다.
     *
     * GET /api/categories/:id
     */
    @GetMapping("/{id}")
    public ApiResult<GetResponse> getCategory(@PathVariable Long id) {
        return OK(GetResponse.from(categoryService.getCategory(id)));
    }
}
