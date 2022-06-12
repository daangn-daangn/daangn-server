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
import com.daangndaangn.common.error.NotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SaleReviewServiceTest {

    private final Long USER1_OAUTH_ID = 111L;
    private final Long USER2_OAUTH_ID = 222L;
    private final Pageable TEST_PAGINATION = PageRequest.of(0, 10);
    private Long productId1, productId2, productId3;

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

    @BeforeAll
    public void init() {
        userService.create(USER1_OAUTH_ID, "");
        userService.create(USER2_OAUTH_ID, "");
        userService.create(333L, "");
        userService.create(444L, "");

        User user1 = userService.getUserByOauthId(USER1_OAUTH_ID);
        User user2 = userService.getUserByOauthId(USER2_OAUTH_ID);
        User user3 = userService.getUserByOauthId(333L);
        User user4 = userService.getUserByOauthId(444L);

        userService.update(user1.getId(), "테스트 닉네임","테스트 주소");
        userService.update(user2.getId(), "테스트 닉네임","테스트 주소");
        userService.update(user3.getId(), "테스트 닉네임","테스트 주소");

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

        productId3 = productService.create(user3.getId(),
                mockCategoryId,
                "title",
                "name",
                123L,
                "description",
                null).getId();

        saleReviewService.create(productId2, user2.getId(), user1.getId(), SaleReviewType.SELLER_REVIEW, "첫 번째로 받은 판매자 리뷰입니다.");
        saleReviewService.create(productId1, user4.getId(), user1.getId(), SaleReviewType.BUYER_REVIEW, "첫 번째로 받은 구매자 리뷰입니다.");
        saleReviewService.create(productId3, user3.getId(), user1.getId(), SaleReviewType.SELLER_REVIEW, "두 번째로 받은 판매자 리뷰입니다.");
    }

    @Test
    public void 없는_리뷰를_조회하면_예외를_반환한다() {
        //given
        Long InvalidReviewId = 999L;

        //then
        assertThrows(NotFoundException.class, () -> saleReviewService.getSaleReview(InvalidReviewId));
    }

    @Test
    public void 없는_리뷰를_조회하면_빈_리스트를_반환한다() {
        //given
        Long InvalidReviewId = 999L;

        //when
        List<SaleReview> userReviews = saleReviewService.getUserReviews(InvalidReviewId, TEST_PAGINATION);
        List<SaleReview> sellerReviews = saleReviewService.getSellerReviews(InvalidReviewId, TEST_PAGINATION);
        List<SaleReview> buyerReviews = saleReviewService.getBuyerReviews(InvalidReviewId, TEST_PAGINATION);

        //then
        assertThat(userReviews.size()).isEqualTo(0);
        assertThat(sellerReviews.size()).isEqualTo(0);
        assertThat(buyerReviews.size()).isEqualTo(0);
    }

    @Test
    public void 내가_받은_전체_리뷰를_조회할_수_있다() {
        //given
        User user = userService.getUserByOauthId(USER1_OAUTH_ID);

        //when
        List<SaleReview> userReviews = saleReviewService.getUserReviews(user.getId(), TEST_PAGINATION);

        //then
        assertThat(userReviews.size()).isEqualTo(3);
    }

    @Test
    public void 내가_받은_판매자_리뷰를_조회할_수_있다() {
        //given
        User seller = userService.getUserByOauthId(USER1_OAUTH_ID);

        //when
        List<SaleReview> sellerReviews = saleReviewService.getSellerReviews(seller.getId(), TEST_PAGINATION);

        //then
        assertThat(sellerReviews.size()).isEqualTo(2);
    }

    @Test
    public void 내가_받은_구매자_리뷰를_조회할_수_있다() {
        //given
        User buyer = userService.getUserByOauthId(USER1_OAUTH_ID);

        //when
        List<SaleReview> buyerReviews = saleReviewService.getBuyerReviews(buyer.getId(), TEST_PAGINATION);

        //then
        assertThat(buyerReviews.size()).isEqualTo(1);
    }

    // existBuyerReview
    @Test
    public void 내가_구매자_리뷰를_남겼는지_여부를_알_수_있다() {
        //given
        User user = userService.getUserByOauthId(USER1_OAUTH_ID);
        boolean before = saleReviewService.existBuyerReview(productId2, user.getId());

        //when
        saleReviewService.create(productId2, user.getId(), 1L, SaleReviewType.BUYER_REVIEW, "구매자 리뷰");
        boolean after = saleReviewService.existBuyerReview(productId2, user.getId());

        //then
        assertThat(before).isEqualTo(false);
        assertThat(after).isEqualTo(true);
    }

    @AfterAll
    public void destroy() {
        saleReviewRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}