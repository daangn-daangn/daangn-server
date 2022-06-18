package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.review.SaleReview;
import com.daangndaangn.common.api.entity.review.SaleReviewType;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.category.CategoryRepository;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import com.daangndaangn.common.api.repository.salereview.SaleReviewRepository;
import com.daangndaangn.common.api.repository.user.UserRepository;
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


import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SaleReviewCreateTest {

    private final Long USER1_OAUTH_ID = 111L;
    private final Long USER2_OAUTH_ID = 222L;
    private Long productId1, productId2;

    @Autowired
    private SaleReviewService saleReviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @Autowired
    private SaleReviewRepository saleReviewRepository;  // deleteAll 호출 용도
    @Autowired
    private UserRepository userRepository;  // deleteAll 호출 용도
    @Autowired
    private CategoryRepository categoryRepository;  // deleteAll 호출 용도
    @Autowired
    private ProductRepository productRepository;  // deleteAll 호출 용도

    private User user1, user2;

    @BeforeAll
    public void init() {
        userService.create(USER1_OAUTH_ID, "");
        userService.create(USER2_OAUTH_ID, "");

        user1 = userService.getUserByOauthId(USER1_OAUTH_ID);
        user2 = userService.getUserByOauthId(USER2_OAUTH_ID);

        userService.update(user1.getId(), "테스트 닉네임","테스트 주소");
        userService.update(user2.getId(), "테스트 닉네임","테스트 주소");

        Long mockCategoryId = categoryService.create("test_category1");

        productId1 = productService.create(user1.getId(),
                                                mockCategoryId,
                                                "title",
                                                "name",
                                                123L,
                                                "description",
                                                null).getId();

        productId2 = productService.create(user2.getId(),
                mockCategoryId,
                "title",
                "name",
                123L,
                "description",
                null).getId();
    }

    @Test
    public void sellerReview_정상_생성_테스트() {
        //given
        productService.updateToSoldOut(productId2, user1.getId());

        String content = "판매자 리뷰입니다.";

        //when
        Long result = saleReviewService.create(productId2,
                user2.getId(),
                user1.getId(),
                SaleReviewType.SELLER_REVIEW,
                content);

        //then
        SaleReview findSaleReview = saleReviewService.getSaleReview(result);

        assertThat(findSaleReview.getProduct().getId()).isEqualTo(productId2);
        assertThat(findSaleReview.getReviewer().getId()).isEqualTo(user2.getId());
        assertThat(findSaleReview.getReviewee().getId()).isEqualTo(user1.getId());
        assertThat(findSaleReview.getSaleReviewType()).isEqualTo(SaleReviewType.SELLER_REVIEW);
        assertThat(findSaleReview.getContent()).isEqualTo(content);
    }

    @Test
    public void buyerReview_정상_생성_테스트() {
        //given
        productService.updateToSoldOut(productId1, user2.getId());

        String content = "구매자 리뷰입니다.";

        //when
        Long result = saleReviewService.create(productId1,
                user2.getId(),
                user1.getId(),
                SaleReviewType.BUYER_REVIEW,
                content);

        //then
        SaleReview findSaleReview = saleReviewService.getSaleReview(result);

        assertThat(findSaleReview.getProduct().getId()).isEqualTo(productId1);
        assertThat(findSaleReview.getReviewer().getId()).isEqualTo(user2.getId());
        assertThat(findSaleReview.getReviewee().getId()).isEqualTo(user1.getId());
        assertThat(findSaleReview.getSaleReviewType()).isEqualTo(SaleReviewType.BUYER_REVIEW);
        assertThat(findSaleReview.getContent()).isEqualTo(content);
    }


    @AfterAll
    public void destroy() {
        saleReviewRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}