package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.review.SaleReviewType;
import com.daangndaangn.common.api.repository.salereview.SaleReviewRepository;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 리뷰 생성 로직이 모킹이 불가능하여, 생성 로직 중 '정상 케이스'만 분리하여 Spy 빈으로 테스트
 */
@SpringBootTest
public class SaleReviewServiceSpyTest {

    @Autowired
    private SaleReviewService saleReviewService;

    @SpyBean
    private CategoryService categoryService;

    @SpyBean
    private UserService userService;

    @SpyBean
    private ProductService productService;

    @SpyBean
    private SaleReviewRepository saleReviewRepository;

    @SpyBean
    private EventBus eventBus;

    private Long mockSellerId, mockBuyerId, mockCategoryId;

    private Product mockProduct;

    @BeforeEach
    public void init() throws ExecutionException, InterruptedException {
        mockCategoryId = categoryService.create("test");
        mockSellerId = userService.create(123L).get();
        mockBuyerId = userService.create(456L).get();

        userService.update(mockSellerId, "testNickname", "test address");

        mockProduct = productService.create(mockSellerId, mockCategoryId, "test", 123L, "test", null).get();
        productService.updateToSoldOut(mockProduct.getId(), mockBuyerId);
    }

    @Test
    @DisplayName("판매자 후기와 구매자 후기를 작성할 수 있다.")
    public void create() throws ExecutionException, InterruptedException {
        //given
        userService.update(mockBuyerId, "testNickname", "test address");

        //when
        saleReviewService.create(mockProduct.getId(), mockSellerId, mockBuyerId, SaleReviewType.SELLER_REVIEW, "test seller review").get();
        saleReviewService.create(mockProduct.getId(), mockBuyerId, mockSellerId, SaleReviewType.BUYER_REVIEW, "test buyer review").get();

        //then
        verify(userService, times(8)).getUser(anyLong());
        verify(saleReviewRepository, times(2)).save(any());
        verify(eventBus, times(3)).post(any());
    }
}
