package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.common.api.entity.review.SaleReview;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.common.api.repository.salereview.SaleReviewRepository;
import com.daangndaangn.common.api.repository.UserRepository;
import com.daangndaangn.apiserver.service.user.UserService;
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

    private final Long USER_OAUTH_ID = 111L;
    private final Pageable TEST_PAGINATION = PageRequest.of(0, 10);

    @Autowired
    private SaleReviewService saleReviewService;
    @Autowired
    private UserService userService;

    @Autowired
    private SaleReviewRepository saleReviewRepository;  // deleteAll 호출 용도
    @Autowired
    private UserRepository userRepository;  // deleteAll 호출 용도

    @BeforeAll
    public void init() {
        userService.join(USER_OAUTH_ID, "");
        userService.join(222L, "");
        userService.join(333L, "");

        User seller = userService.getUserByOauthId(USER_OAUTH_ID);
        User buyer = userService.getUserByOauthId(222L);
        User buyer2 = userService.getUserByOauthId(333L);

        saleReviewService.create(seller.getId(), buyer.getId(), "첫 번째 리뷰입니다.");
        saleReviewService.create(buyer.getId(), seller.getId(), "두 번째 리뷰입니다.");
        saleReviewService.create(seller.getId(), buyer2.getId(), "세 번째 리뷰입니다.");
    }

    @Test
    public void 없는_리뷰를_조회하면_예외를_반환한다() {
        //given
        Long InvalidReviewId = 999L;

        //then
        assertThrows(NotFoundException.class, () -> saleReviewService.findSaleReview(InvalidReviewId));
    }

    @Test
    public void 없는_리뷰를_조회하면_빈_리스트를_반환한다() {
        //given
        Long InvalidReviewId = 999L;

        //when
        List<SaleReview> userReviews = saleReviewService.findAllUserReview(InvalidReviewId, TEST_PAGINATION);
        List<SaleReview> sellerReviews = saleReviewService.findAllSellerReview(InvalidReviewId, TEST_PAGINATION);
        List<SaleReview> buyerReviews = saleReviewService.findAllBuyerReview(InvalidReviewId, TEST_PAGINATION);

        //then
        assertThat(userReviews.size()).isEqualTo(0);
        assertThat(sellerReviews.size()).isEqualTo(0);
        assertThat(buyerReviews.size()).isEqualTo(0);
    }

    @Test
    public void 전체_리뷰를_조회할_수_있다() {
        //given
        User user = userService.getUserByOauthId(USER_OAUTH_ID);

        //when
        List<SaleReview> userReviews = saleReviewService.findAllUserReview(user.getId(), TEST_PAGINATION);

        //then
        assertThat(userReviews.size()).isEqualTo(3);
    }

    @Test
    public void 판매자_리뷰를_조회할_수_있다() {
        //given
        User seller = userService.getUserByOauthId(USER_OAUTH_ID);

        //when
        List<SaleReview> sellerReviews = saleReviewService.findAllSellerReview(seller.getId(), TEST_PAGINATION);

        //then
        assertThat(sellerReviews.size()).isEqualTo(2);
    }

    @Test
    public void 구매자_리뷰를_조회할_수_있다() {
        //given
        User buyer = userService.getUserByOauthId(USER_OAUTH_ID);

        //when
        List<SaleReview> buyerReviews = saleReviewService.findAllBuyerReview(buyer.getId(), TEST_PAGINATION);

        //then
        assertThat(buyerReviews.size()).isEqualTo(1);
    }

    @AfterAll
    public void destroy() {
        saleReviewRepository.deleteAll();
        userRepository.deleteAll();
    }
}