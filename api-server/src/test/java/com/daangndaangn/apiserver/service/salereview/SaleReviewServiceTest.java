package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.review.SaleReview;
import com.daangndaangn.common.api.entity.review.SaleReviewType;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.salereview.SaleReviewRepository;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.error.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SaleReviewServiceTest {

    @InjectMocks
    private SaleReviewServiceImpl saleReviewService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private SaleReviewRepository saleReviewRepository;

    private final Pageable TEST_PAGINATION = PageRequest.of(0, 10);
    private Category mockCategory;
    private User mockUser1, mockUser2, mockUser3;

    @BeforeEach
    public void init() {
        mockCategory = Category.builder().id(1L).name("mockCategory").build();

        mockUser1 = User.builder()
                .id(1L)
                .oauthId(12L)
                .profileUrl("www.naver.com")
                .build();

        mockUser2 = User.builder()
                .id(2L)
                .oauthId(34L)
                .profileUrl("www.naver.com")
                .build();

        mockUser2.update("testNickname", Location.from("test Address"));

        mockUser3 = User.builder()
                .id(3L)
                .oauthId(56L)
                .profileUrl("www.naver.com")
                .build();
    }

    /**
     * create test
     */
    @Test
    @DisplayName("?????????_?????????_????????????_????????????_????????????_?????????_?????????_????????????")
    public void create() {
        //given
        User mockUser = User.builder()
                .id(3L)
                .oauthId(56L)
                .profileUrl("www.naver.com")
                .build();

        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        given(productService.getProduct(anyLong())).willReturn(mockProduct);
        given(userService.getUser(anyLong())).willReturn(mockUser);

        //when
        assertThrows(IllegalStateException.class,
            () -> saleReviewService.create(mockProduct.getId(),
                    mockUser1.getId(),
                    mockUser2.getId(),
                    SaleReviewType.SELLER_REVIEW,
                    "test saleReview"));

        //then
        verify(productService).getProduct(anyLong());
        verify(userService).getUser(anyLong());
        verify(saleReviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("isValidCreateRequest???_seller???_buyer???_??????_????????????_true???_????????????")
    public void isValidCreateRequest1() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        mockProduct.updateBuyer(mockUser3);

        //when
        boolean result = saleReviewService.isValidCreateRequest(mockProduct, mockUser2, mockUser3, SaleReviewType.SELLER_REVIEW);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("SELLER_REVIEW???_seller???_??????_???_??????")
    public void isValidCreateRequest2() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        mockProduct.updateBuyer(mockUser3);

        //when
        boolean buyerResult = saleReviewService.isValidCreateRequest(mockProduct, mockUser3, mockUser2, SaleReviewType.SELLER_REVIEW);
        boolean invalidUserResult = saleReviewService.isValidCreateRequest(mockProduct, mockUser1, mockUser2, SaleReviewType.SELLER_REVIEW);

        //then
        assertThat(buyerResult).isEqualTo(false);
        assertThat(invalidUserResult).isEqualTo(false);
    }

    @Test
    @DisplayName("BUYER_REVIEW???_buyer???_??????_???_??????")
    public void isValidCreateRequest3() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        mockProduct.updateBuyer(mockUser3);

        //when
        boolean sellerResult = saleReviewService.isValidCreateRequest(mockProduct, mockUser2, mockUser3, SaleReviewType.BUYER_REVIEW);
        boolean invalidUserResult = saleReviewService.isValidCreateRequest(mockProduct, mockUser1, mockUser3, SaleReviewType.BUYER_REVIEW);

        //then
        assertThat(sellerResult).isEqualTo(false);
        assertThat(invalidUserResult).isEqualTo(false);
    }

    @Test
    @DisplayName("SELLER_REVIEW???_BUYER_REVIEW???_??????_?????????_false???_????????????")
    public void isValidCreateRequest4() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        mockProduct.updateBuyer(mockUser3);

        //when
        boolean result = saleReviewService.isValidCreateRequest(mockProduct, mockUser2, mockUser3, SaleReviewType.HIDE);

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("??????_?????????_????????????_?????????_????????????")
    public void getSaleReview() {
        //given
        Long InvalidReviewId = 999L;
        given(saleReviewRepository.findBySaleReviewId(anyLong())).willReturn(Optional.empty());

        //then
        assertThrows(NotFoundException.class, () -> saleReviewService.getSaleReview(InvalidReviewId));

        //then
        verify(saleReviewRepository).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("??????_?????????_????????????_???_????????????_????????????")
    public void ??????_?????????_????????????_???_????????????_????????????() {
        //given
        given(saleReviewRepository.findAllUserReview(anyLong(), any())).willReturn(Collections.emptyList());
        given(saleReviewRepository.findAllSellerReview(anyLong(), any())).willReturn(Collections.emptyList());
        given(saleReviewRepository.findAllBuyerReview(anyLong(), any())).willReturn(Collections.emptyList());

        //when
        List<SaleReview> userReviews = saleReviewService.getUserReviews(mockUser1.getId(), TEST_PAGINATION);
        List<SaleReview> sellerReviews = saleReviewService.getSellerReviews(mockUser1.getId(), TEST_PAGINATION);
        List<SaleReview> buyerReviews = saleReviewService.getBuyerReviews(mockUser1.getId(), TEST_PAGINATION);

        //then
        assertThat(userReviews.size()).isEqualTo(0);
        assertThat(sellerReviews.size()).isEqualTo(0);
        assertThat(buyerReviews.size()).isEqualTo(0);
        verify(saleReviewRepository).findAllUserReview(anyLong(), any());
        verify(saleReviewRepository).findAllSellerReview(anyLong(), any());
        verify(saleReviewRepository).findAllBuyerReview(anyLong(), any());
    }

    @Test
    @DisplayName("??????_??????_??????_?????????_?????????_???_??????")
    public void getUserReviews() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser2)
                .reviewee(mockUser1)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findAllUserReview(anyLong(), any())).willReturn(List.of(saleReview));

        //when
        List<SaleReview> result = saleReviewService.getUserReviews(mockUser1.getId(), TEST_PAGINATION);

        //then
        assertThat(result.size()).isEqualTo(1);
        verify(saleReviewRepository).findAllUserReview(anyLong(), any());
    }

    @Test
    @DisplayName("??????_??????_?????????_?????????_?????????_???_??????")
    public void getSellerReviews() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser2)
                .reviewee(mockUser1)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findAllSellerReview(anyLong(), any())).willReturn(List.of(saleReview));

        //when
        List<SaleReview> result = saleReviewService.getSellerReviews(mockUser1.getId(), TEST_PAGINATION);

        //then
        assertThat(result.size()).isEqualTo(1);
        verify(saleReviewRepository).findAllSellerReview(anyLong(), any());
    }

    @Test
    @DisplayName("??????_??????_?????????_?????????_?????????_???_??????")
    public void getBuyerReviews() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .saleReviewType(SaleReviewType.BUYER_REVIEW)
                .reviewer(mockUser1)
                .reviewee(mockUser2)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findAllBuyerReview(anyLong(), any())).willReturn(List.of(saleReview));

        //when
        List<SaleReview> result = saleReviewService.getBuyerReviews(mockUser2.getId(), TEST_PAGINATION);

        //then
        assertThat(result.size()).isEqualTo(1);
        verify(saleReviewRepository).findAllBuyerReview(anyLong(), any());
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ??????????????? ????????? ??? ??? ??????")
    public void isSellerReviewWriter1() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser1)
                .reviewee(mockUser2)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findBySaleReviewId(anyLong())).willReturn(Optional.ofNullable(saleReview));

        //when
        boolean buyerReviewWriter = saleReviewService.isSellerReviewWriter(saleReview.getId(), mockUser1.getId());

        //then
        assertThat(buyerReviewWriter).isEqualTo(true);
        verify(saleReviewRepository).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("????????? ?????? ??? saleReviewId??? userId ?????? ????????????")
    public void isSellerReviewWriter2() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser1)
                .reviewee(mockUser2)
                .product(mockProduct)
                .content("mockContent")
                .build();

        Long invalidReviewId = null;
        Long invalidUserId = null;

        //when
        assertThatThrownBy(() -> saleReviewService.isSellerReviewWriter(invalidReviewId, mockUser1.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> saleReviewService.isSellerReviewWriter(saleReview.getId(), invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(saleReviewRepository, never()).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ??????????????? ????????? ??? ??? ??????")
    public void isBuyerReviewWriter1() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.BUYER_REVIEW)
                .reviewer(mockUser1)
                .reviewee(mockUser2)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findBySaleReviewId(anyLong())).willReturn(Optional.ofNullable(saleReview));

        //when
        boolean buyerReviewWriter = saleReviewService.isBuyerReviewWriter(saleReview.getId(), mockUser1.getId());

        //then
        assertThat(buyerReviewWriter).isEqualTo(true);
        verify(saleReviewRepository).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("????????? ?????? ??? saleReviewId??? userId ?????? ????????????")
    public void isBuyerReviewWriter2() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.BUYER_REVIEW)
                .reviewer(mockUser1)
                .reviewee(mockUser2)
                .product(mockProduct)
                .content("mockContent")
                .build();

        Long invalidReviewId = null;
        Long invalidUserId = null;

        //when
        assertThatThrownBy(() -> saleReviewService.isBuyerReviewWriter(invalidReviewId, mockUser1.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> saleReviewService.isBuyerReviewWriter(saleReview.getId(), invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(saleReviewRepository, never()).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("??????_?????????_?????????_????????????_?????????_???_???_??????")
    public void existBuyerReview1() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        given(saleReviewRepository.existBuyerReview(anyLong(), anyLong())).willReturn(true);

        //when
        boolean result = saleReviewService.existBuyerReview(mockProduct.getId(), mockUser1.getId());

        //then
        assertThat(result).isEqualTo(true);
        verify(saleReviewRepository).existBuyerReview(anyLong(), anyLong());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ??? productId ?????? userId??? ???????????? ????????? ????????? ????????????")
    public void existBuyerReview2() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        Long invalidProductId = null;
        Long invalidUserId = null;

        //when
        assertThatThrownBy(() -> saleReviewService.existBuyerReview(invalidProductId, mockUser1.getId()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> saleReviewService.existBuyerReview(mockProduct.getId(), invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(saleReviewRepository, never()).existBuyerReview(anyLong(), anyLong());
    }

    @Test
    @DisplayName("??????_?????????_?????????_????????????_?????????_???_???_??????")
    public void existSellerReview1() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        given(saleReviewRepository.existSellerReview(anyLong(), anyLong())).willReturn(true);

        //when
        boolean result = saleReviewService.existSellerReview(mockProduct.getId(), mockUser1.getId());

        //then
        assertThat(result).isEqualTo(true);
        verify(saleReviewRepository).existSellerReview(anyLong(), anyLong());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ??? productId ?????? userId??? ???????????? ????????? ????????? ????????????")
    public void existSellerReview2() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        Long invalidProductId = null;
        Long invalidUserId = null;

        //when
        assertThatThrownBy(() -> saleReviewService.existSellerReview(invalidProductId, mockUser1.getId()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> saleReviewService.existSellerReview(mockProduct.getId(), invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(saleReviewRepository, never()).existSellerReview(anyLong(), anyLong());
    }

    @Test
    @DisplayName("????????? ????????? ??? ??????")
    public void update1() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser2)
                .reviewee(mockUser1)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findBySaleReviewId(anyLong())).willReturn(Optional.ofNullable(saleReview));

        String newContent = "newContent";

        //when
        saleReviewService.update(saleReview.getId(), newContent);

        //then
        verify(saleReviewRepository).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("?????? ?????? ??? review id??? ?????? ????????? ????????????")
    public void update2() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser2)
                .reviewee(mockUser1)
                .product(mockProduct)
                .content("mockContent")
                .build();


        String newContent = "newContent";
        Long invalidReviewId = null;
        String invalidContent = "";

        //when
        assertThatThrownBy(() -> saleReviewService.update(invalidReviewId, newContent))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> saleReviewService.update(saleReview.getId(), invalidContent))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(saleReviewRepository, never()).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("??????_?????????_?????????_??????_???_??????")
    public void hide1() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser2)
                .reviewee(mockUser1)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findBySaleReviewId(anyLong())).willReturn(Optional.ofNullable(saleReview));
        int before = saleReview.getSaleReviewType().getCode();

        //when
        saleReviewService.hide(saleReview.getId(), mockUser1.getId());
        int after = saleReview.getSaleReviewType().getCode();

        //then
        assertThat(before).isEqualTo(SaleReviewType.SELLER_REVIEW.getCode());
        assertThat(after).isEqualTo(SaleReviewType.HIDE.getCode());
        verify(saleReviewRepository).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("?????????_?????????_???????????????_?????????_??????_???_??????")
    public void hide2() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser2)
                .reviewee(mockUser1)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findBySaleReviewId(anyLong())).willReturn(Optional.ofNullable(saleReview));

        //when
        assertThrows(UnauthorizedException.class,
            () -> saleReviewService.hide(saleReview.getId(), mockUser2.getId()));

        //then
        verify(saleReviewRepository).findBySaleReviewId(anyLong());
    }

    @Test
    @DisplayName("????????? ????????? ??? ??????")
    public void delete() {
        //given
        Product mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser2)
                .category(mockCategory)
                .title("title")
                .price(1000L)
                .description("description")
                .build();

        SaleReview saleReview = SaleReview.builder()
                .id(1L)
                .saleReviewType(SaleReviewType.SELLER_REVIEW)
                .reviewer(mockUser2)
                .reviewee(mockUser1)
                .product(mockProduct)
                .content("mockContent")
                .build();

        given(saleReviewRepository.findBySaleReviewId(anyLong())).willReturn(Optional.ofNullable(saleReview));

        //when
        saleReviewService.delete(saleReview.getId());

        //then
        verify(saleReviewRepository).findBySaleReviewId(anyLong());
        verify(saleReviewRepository).delete(any());
    }
}
