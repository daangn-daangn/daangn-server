package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductState;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.util.UploadUtils;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UserService userService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UploadUtils uploadUtils;

    @Mock
    private EventBus eventBus;

    private Category mockCategory;
    private User mockUser, mockSeller;
    private Product mockProduct;

    @BeforeEach
    public void init() {
        mockCategory = Category.builder()
                .id(1L)
                .name("testCategory")
                .build();

        mockUser = User.builder()
            .id(1L)
            .oauthId(1234L)
            .profileUrl("www.naver.com")
            .build();

        mockSeller = User.builder()
                .id(2L)
                .oauthId(567L)
                .profileUrl("www.naver.com")
                .build();

        mockSeller.update("testNickname", Location.from("test address"));

        mockProduct = Product.builder()
                .id(1L)
                .seller(mockSeller)
                .category(mockCategory)
                .title("title")
                .price(10000L)
                .description("description")
                .build();
    }

    @Test
    @DisplayName("??????_?????????_????????????_???????????????_?????????_?????????_?????????_????????????")
    public void create1() {
        //given
        given(userService.getUser(anyLong())).willReturn(mockUser);

        String title = "test title";
        Long price = 123L;
        String description = "test description";

        //when
        assertThrows(IllegalStateException.class,
            () -> productService.create(mockUser.getId(), mockCategory.getId(), title, price, description, null));

        //then
        verify(uploadUtils, never()).isNotImageFile(anyString());
        verify(categoryService, never()).getCategory(anyLong());
        verify(userService).getUser(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("??????_?????????_????????????_???????????????_?????????_?????????_?????????_????????????")
    public void create2() {
        //given
        String emptyNickname = "";
        mockUser.update(emptyNickname, Location.from("test address"));
        given(userService.getUser(anyLong())).willReturn(mockUser);


        String title = "test title";
        Long price = 123L;
        String description = "test description";

        //when
        assertThrows(IllegalStateException.class,
                () -> productService.create(mockUser.getId(), mockCategory.getId(), title, price, description, null));

        //then
        verify(uploadUtils, never()).isNotImageFile(anyString());
        verify(categoryService, never()).getCategory(anyLong());
        verify(userService).getUser(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("?????????_??????_product???_?????????_???_??????")
    public void create3() {
        //given
        mockUser.update("testNickname", Location.from("test address"));

        given(categoryService.getCategory(anyLong())).willReturn(mockCategory);
        given(userService.getUser(anyLong())).willReturn(mockUser);

        String title = "test title";
        Long price = 123L;
        String description = "test description";

        //when
        productService.create(mockUser.getId(), mockCategory.getId(), title, price, description, null);

        //then
        verify(uploadUtils, never()).isNotImageFile(anyString());
        verify(categoryService).getCategory(anyLong());
        verify(userService).getUser(anyLong());
        verify(productRepository).save(any());
    }

    @Test
    @DisplayName("????????????_??????_product???_?????????_???_??????")
    public void create4() {
        //given
        mockUser.update("testNickname", Location.from("test address"));

        given(uploadUtils.isNotImageFile(anyString())).willReturn(false);
        given(categoryService.getCategory(anyLong())).willReturn(mockCategory);
        given(userService.getUser(anyLong())).willReturn(mockUser);

        String title = "test title";
        Long price = 123L;
        String description = "test description";
        List<String> productImages = List.of("image1.png", "image2.jpeg");

        //when
        productService.create(mockUser.getId(), mockCategory.getId(), title, price, description, productImages);

        //then
        verify(uploadUtils, times(productImages.size())).isNotImageFile(anyString());
        verify(categoryService).getCategory(anyLong());
        verify(userService).getUser(anyLong());
        verify(productRepository).save(any());
    }

    @Test
    @DisplayName("?????????_?????????_?????????_?????????_jpg_jpeg_png???_????????????")
    public void create5() {
        //given
        mockUser.update("testNickname", Location.from("test address"));

        given(uploadUtils.isNotImageFile(anyString())).willReturn(true);

        String title = "test title";
        Long price = 123L;
        String description = "test description";
        List<String> invalidProductImages = List.of("image1", "image2.jpeg");

        //when
        assertThrows(IllegalArgumentException.class,
                () -> productService.create(mockUser.getId(), mockCategory.getId(), title, price, description, invalidProductImages));
        //then
        verify(uploadUtils).isNotImageFile(anyString());
        verify(categoryService, never()).getCategory(anyLong());
        verify(userService, never()).getUser(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("product ????????? ?????? ????????? ?????? 10???????????? ?????? ????????????")
    public void create6() {
        //given
        mockUser.update("testNickname", Location.from("test address"));

        String title = "test title";
        Long price = 123L;
        String description = "test description";
        List<String> overSizeProductImages = new LinkedList<>();

        for (int loop = 0; loop < 11; ++loop) {
            overSizeProductImages.add("testImage.png");
        }

        //when
        assertThrows(IllegalArgumentException.class,
                () -> productService.create(mockUser.getId(), mockCategory.getId(), title, price, description, overSizeProductImages));
        //then
        verify(uploadUtils, never()).isNotImageFile(anyString());
        verify(categoryService, never()).getCategory(anyLong());
        verify(userService, never()).getUser(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("id??? ????????? ????????? ??? ??????")
    public void getProduct1() {
        //given
        given(productRepository.findByProductId(any())).willReturn(Optional.ofNullable(mockProduct));

        //when
        Product product = productService.getProduct(mockProduct.getId());

        //then
        assertThat(product).isEqualTo(mockProduct);
        verify(productRepository).findByProductId(anyLong());
    }

    @Test
    @DisplayName("id??? ????????? ?????? ??? ????????? ????????? ????????????")
    public void getProduct2() {
        //given
        given(productRepository.findByProductId(any())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> productService.getProduct(mockProduct.getId()))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(productRepository).findByProductId(anyLong());
    }

    @Test
    @DisplayName("?????? ?????? ??? id??? ????????????")
    public void getProduct3() {
        //given
        Long invalidId = null;

        //when
        assertThatThrownBy(() -> productService.getProduct(invalidId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(productRepository, never()).findByProductId(anyLong());
    }

    @Test
    @DisplayName("product??? productImage??? ?????? ????????? ??? ??????")
    public void getProductWithProductImages1() {
        //given
        given(productRepository.findByProductIdWithProductImages(anyLong())).willReturn(Optional.ofNullable(mockProduct));

        //when
        Product productWithProductImages = productService.getProductWithProductImages(mockProduct.getId());

        //then
        assertThat(productWithProductImages).isEqualTo(mockProduct);
        verify(productRepository).findByProductIdWithProductImages(anyLong());
    }

    @Test
    @DisplayName("id??? ???????????? product??? ?????? ??? ????????? ????????????")
    public void getProductWithProductImages2() {
        //given
        given(productRepository.findByProductIdWithProductImages(anyLong())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> productService.getProductWithProductImages(mockProduct.getId()))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(productRepository).findByProductIdWithProductImages(anyLong());
    }

    @Test
    @DisplayName("??????_??????_?????????_?????????????????????_Buyer???_????????????_??????")
    public void updateToSoldOut() {
        //given
        User mockBuyer = User.builder()
                .id(3L)
                .oauthId(89L)
                .profileUrl("www.naver.com")
                .build();

        assertThat(mockProduct.getProductState()).isEqualTo(ProductState.FOR_SALE);

        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));
        given(userService.getUser(anyLong())).willReturn(mockBuyer);
        doNothing().when(eventBus).post(any());

        //when
        productService.updateToSoldOut(mockProduct.getId(), mockBuyer.getId());

        //then
        assertThat(mockProduct.getProductState()).isEqualTo(ProductState.SOLD_OUT);
        assertThat(mockProduct.getBuyer()).isNotNull();
        assertThat(mockProduct.getBuyer().getId()).isEqualTo(mockBuyer.getId());
        verify(productRepository).findByProductId(anyLong());
        verify(userService).getUser(anyLong());
        verify(eventBus, times(2)).post(any());
    }

    @Test
    @DisplayName("?????????_?????????_?????????_???_??????")
    public void update1() {
        //given
        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));

        assertThat(mockProduct.getProductState()).isEqualTo(ProductState.FOR_SALE);

        //when
        productService.update(mockProduct.getId(), ProductState.REVERSED.getCode());
        assertThat(mockProduct.getProductState()).isEqualTo(ProductState.REVERSED);

        productService.update(mockProduct.getId(), ProductState.HIDE.getCode());
        assertThat(mockProduct.getProductState()).isEqualTo(ProductState.HIDE);

        //then
        verify(productRepository, times(2)).findByProductId(anyLong());
    }

    @Test
    @DisplayName("???????????????_????????????_???_???_??????")
    public void update2() {
        //given
        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));
        given(categoryService.getCategory(anyLong())).willReturn(mockCategory);

        String newTitle = "newTitle";
        long newPrice = 12345L;
        String newDescription = "newDescription";

        long beforePrice = mockProduct.getPrice();
        if (newPrice < beforePrice) {
            doNothing().when(eventBus).post(any());
        }

        //when
        productService.update(mockProduct.getId(), newTitle, mockCategory.getId(), newPrice, newDescription);

        //then
        assertThat(mockProduct.getTitle()).isEqualTo(newTitle);
        assertThat(mockProduct.getPrice()).isEqualTo(newPrice);
        assertThat(mockProduct.getDescription()).isEqualTo(newDescription);
        verify(productRepository).findByProductId(anyLong());
        verify(categoryService).getCategory(anyLong());
    }

    @Test
    @DisplayName("?????? ????????? ????????? ?????? PriceDownEvent??? ????????????")
    public void update3() {
        //given
        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));
        given(categoryService.getCategory(anyLong())).willReturn(mockCategory);

        String newTitle = "newTitle";
        long halfPrice = mockProduct.getPrice() / 2;
        String newDescription = "newDescription";

        //when
        productService.update(mockProduct.getId(), newTitle, mockCategory.getId(), halfPrice, newDescription);

        //then
        assertThat(mockProduct.getTitle()).isEqualTo(newTitle);
        assertThat(mockProduct.getPrice()).isEqualTo(halfPrice);
        assertThat(mockProduct.getDescription()).isEqualTo(newDescription);
        verify(productRepository).findByProductId(anyLong());
        verify(categoryService).getCategory(anyLong());
        verify(eventBus).post(any());
    }

    @Test
    @DisplayName("?????????_????????????_????????????_???_???_??????")
    public void update4() {
        //given
        User mockBuyer = User.builder()
                .id(3L)
                .oauthId(89L)
                .profileUrl("www.naver.com")
                .build();

        assertThat(mockProduct.getBuyer()).isNull();

        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));
        given(userService.getUser(anyLong())).willReturn(mockBuyer);

        //when
        productService.update(mockProduct.getId(), mockBuyer.getId());

        //then
        assertThat(mockProduct.getBuyer()).isNotNull();
        assertThat(mockProduct.getBuyer().getId()).isEqualTo(mockBuyer.getId());
        verify(productRepository).findByProductId(anyLong());
        verify(userService).getUser(anyLong());
    }

    @Test
    @DisplayName("?????? ???????????? ??? categoryId??? ?????? ????????? ????????????")
    public void update5() {
        //given
        String newTitle = "newTitle";
        Long invalidCategoryId = null;
        Long invalidPrice = null;
        String newDescription = "newDescription";

        //when
        assertThatThrownBy(() -> productService.update(mockProduct.getId(), newTitle, invalidCategoryId, 1L, newDescription))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.update(mockProduct.getId(), newTitle, 1L, invalidPrice, newDescription))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(productRepository, never()).findByProductId(anyLong());
        verify(categoryService, never()).getCategory(anyLong());
        verify(userService, never()).getUser(anyLong());
        verify(eventBus, never()).post(any());
    }

    @Test
    @DisplayName("?????????_?????????_???_??????")
    public void delete() {
        //given
        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));

        assertThat(mockProduct.getProductState()).isEqualTo(ProductState.FOR_SALE);

        //when
        productService.delete(mockProduct.getId());

        //then
        assertThat(mockProduct.getProductState()).isEqualTo(ProductState.DELETED);
        verify(productRepository).findByProductId(anyLong());
    }

    @Test
    @DisplayName("??????_????????????_?????????_???_??????")
    public void isSeller1() {
        //given
        given(productRepository.findByProductIdWithOnlySeller(anyLong())).willReturn(Optional.ofNullable(mockProduct));

        //when
        boolean result1 = productService.isSeller(mockProduct.getId(), mockSeller.getId());
        boolean result2 = productService.isSeller(mockProduct.getId(), mockUser.getId());

        //then
        assertThat(result1).isEqualTo(true);
        assertThat(result2).isEqualTo(false);
        verify(productRepository, times(2)).findByProductIdWithOnlySeller(anyLong());
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ?????? ????????? ????????????")
    public void isSeller2() {
        //given
        given(productRepository.findByProductIdWithOnlySeller(anyLong())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> productService.isSeller(mockProduct.getId(), mockSeller.getId()))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(productRepository).findByProductIdWithOnlySeller(anyLong());
    }

    @Test
    @DisplayName("?????? id??? userId??? ???????????? ????????? ????????? ????????????")
    public void isSeller3() {
        //given
        Long invalidProductId = null;
        Long invalidUserId = null;

        //when
        assertThatThrownBy(() -> productService.isSeller(mockProduct.getId(), invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> productService.isSeller(invalidProductId, mockSeller.getId()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> productService.isSeller(invalidProductId, invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(productRepository, never()).findByProductIdWithOnlySeller(anyLong());
    }

    @Test
    @DisplayName("??????????????????_???_???_??????")
    public void refresh1() {
        //given
        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));

        int before = mockProduct.getRefreshCnt();

        //when
        productService.refresh(mockProduct.getId());

        //then
        int after = mockProduct.getRefreshCnt();

        assertThat(before).isLessThan(after);
        verify(productRepository).findByProductId(anyLong());
    }

    @Test
    @DisplayName("??????????????????_5????????????_???_???_??????")
    public void refresh2() {
        //given
        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));

        //when
        productService.refresh(mockProduct.getId());
        productService.refresh(mockProduct.getId());
        productService.refresh(mockProduct.getId());
        productService.refresh(mockProduct.getId());
        productService.refresh(mockProduct.getId());

        assertThrows(IllegalStateException.class, () -> productService.refresh(mockProduct.getId()));

        //then
        verify(productRepository, times(6)).findByProductId(anyLong());
    }
}
