package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.user.UserRepository;
import com.daangndaangn.common.api.repository.category.CategoryRepository;
import com.daangndaangn.common.api.repository.favorite.FavoriteProductRepository;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FavoriteProductServiceTest {

    private final Long MOCK_USER_OAUTH_ID = 111L;
    private Long mockUserId, mockCategoryId, mockProductId;

    @Autowired
    private FavoriteProductService favoriteProductService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;    // deleteAll 호출 용도

    @Autowired
    private CategoryRepository categoryRepository;  // deleteAll 호출 용도

    @Autowired
    private UserRepository userRepository;  // deleteAll 호출 용도

    @Autowired
    private FavoriteProductRepository favoriteProductRepository;    // deleteAll 호출 용도

    @BeforeAll
    public void init() {
        mockCategoryId = categoryService.create("test_category1");

        mockUserId = userService.create(MOCK_USER_OAUTH_ID, null);
        User user = userService.getUser(mockUserId);
        userService.update(user.getId(), "테스트 유저", "노원구 상계동");

        String title = "mockProduct 팝니다.";
        String name = "mockProduct";
        Long price = 10000L;
        String description = "mockProduct 팝니다. 새 상품입니다.";
        mockProductId = productService.create(mockUserId, mockCategoryId, title, name, price, description, null).getId();
    }

    @Test
    public void 찜하기를_누른_사용자를_식별할_수_있다() {
        //given
        Long invalidUserId = 99L;
        Long favoriteProductId = favoriteProductService.create(mockProductId, mockUserId);

        //when
        assertThat(favoriteProductService.isOwner(favoriteProductId, mockUserId)).isEqualTo(true);
        assertThat(favoriteProductService.isOwner(favoriteProductId, invalidUserId)).isEqualTo(false);
    }

    @AfterAll
    public void destroy() {
        favoriteProductRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
