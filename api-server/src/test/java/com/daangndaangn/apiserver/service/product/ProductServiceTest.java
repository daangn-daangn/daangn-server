package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.entity.category.Category;
import com.daangndaangn.apiserver.entity.product.ProductState;
import com.daangndaangn.apiserver.entity.user.Location;
import com.daangndaangn.apiserver.entity.user.User;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.error.UnauthorizedException;
import com.daangndaangn.apiserver.repository.CategoryRepository;
import com.daangndaangn.apiserver.repository.UserRepository;
import com.daangndaangn.apiserver.repository.product.ProductRepository;
import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.user.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

    private final Long USER_OAUTH_ID = 111L;
    private final Long GENERATED_USER_ID = 1L;
    private final Long CATEGORY_ID = 1L;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void init() {
        userService.join(USER_OAUTH_ID, "프로필 주소");
        userService.update(USER_OAUTH_ID, "Test", Location.from("Address"), "프로필 주소");
        categoryRepository.save(Category.from("포켓몬"));
        User seller = userService.findUserByOauthId(USER_OAUTH_ID);
        productService.createProduct("피카츄 스티커 팔아요", "피카츄", CATEGORY_ID, 10000L, "싸게 팔아요~", null, seller.getId());
        productService.createProduct("파이리 스티커 팔아요", "파이리", CATEGORY_ID, 20000L, "싸게 팔아요~", null, seller.getId());
        productService.createProduct("꼬부기 스티커 팔아요", "꼬부기", CATEGORY_ID, 30000L, "싸게 팔아요~", null, seller.getId());
    }


    @Test
    public void 물품을_조회하면_해당_물품을_반환한다() {
        Long ProductId = 1L;

        assertThat(productService.findProduct(ProductId).getId()).isEqualTo(ProductId);
    }

    @Test
    public void 없는_물품을_조회하면_예외를_발생한다() {
        Long InvalidProductId = 11111L;

        assertThrows(NotFoundException.class, () -> productService.getProduct(InvalidProductId));
    }

    @Test
    public void 물품을_등록한다() {
        assertThat(productService.createProduct("리자몽 스티커 팔아요", "리자몽", CATEGORY_ID, 10000L, "싸게 팔아요~", null, GENERATED_USER_ID).getId()).isEqualTo(4L);
    }

    @Test
    public void 물품을_등록할_때_userId가_null이면_예외를_발생한다() {
        Long NullUserId = null;

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct("리자몽 스티커 팔아요", "리자몽", CATEGORY_ID, 10000L, "싸게 팔아요~", null, NullUserId));
    }

    @Test
    public void 물품을_등록할_때_categoryId가_null이면_예외를_발생한다() {
        Long NullCategoryId = null;

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct("리자몽 스티커 팔아요", "리자몽", NullCategoryId, 10000L, "싸게 팔아요~", null, 1L));
    }

    @Test
    public void 물품을_등록할_때_존재하지_않는_회원이면_예외를_발생한다() {
        Long InvalidUserId = 1111L;

        assertThrows(NotFoundException.class, () -> productService.createProduct("리자몽 스티커 팔아요", "리자몽", CATEGORY_ID, 10000L, "싸게 팔아요~", null, InvalidUserId));
    }

    @Test
    public void 물품을_등록할_때_존재하지_않는_카테고리면_예외를_발생한다() {
        Long InvalidCategoryId = 1111L;

        assertThrows(NotFoundException.class, () -> productService.createProduct("리자몽 스티커 팔아요", "리자몽", InvalidCategoryId, 10000L, "싸게 팔아요~", null, 1L));
    }

    @Test
    public void 물품을_삭제한다() {
        Long ProductId = 1L;

        assertThat(productService.findProduct(ProductId).getId()).isEqualTo(ProductId);

        productService.deleteProduct(ProductId, GENERATED_USER_ID);

        assertThat(productService.findProduct(ProductId).getState()).isEqualTo(ProductState.DELETED);
    }

    @Test
    public void 삭제할_때_판매자의_ID와_다르다면_예외를_발생한다() {
        Long ProductId = 1L;
        Long DiffUserId = 3L;
        assertThat(productService.findProduct(ProductId).getId()).isEqualTo(ProductId);

        assertThrows(UnauthorizedException.class, () -> productService.deleteProduct(ProductId, DiffUserId));
    }

    @Test
    public void 물품_정보를_수정한다() {
        Long ProductId = 1L;

        assertThat(productService.findProduct(ProductId).getId()).isEqualTo(ProductId);

        productService.updateProduct(ProductId, "수정", "수정", CATEGORY_ID, 1000L, "수정", GENERATED_USER_ID);

        assertThat(productService.findProduct(ProductId).getTitle()).isEqualTo("수정");
    }

    @Test
    public void 수정할_때_판매자의_ID와_다르다면_예외를_발생한다() {
        Long ProductId = 1L;
        Long DiffUserId = 3L;
        assertThat(productService.findProduct(ProductId).getId()).isEqualTo(ProductId);

        assertThrows(UnauthorizedException.class, () -> productService.updateProduct(ProductId, "수정", "수정", CATEGORY_ID, 1000L, "수정", DiffUserId));
    }

    @AfterAll
    public void destroy() {
        productRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}