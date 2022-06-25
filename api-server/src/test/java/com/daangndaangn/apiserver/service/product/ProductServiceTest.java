package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductState;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import com.daangndaangn.common.util.UploadUtils;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
                .name("name")
                .price(10000L)
                .description("description")
                .build();
    }

    @Test
    public void 물품_등록시_판매자의_닉네임이과_지역이_없으면_예외를_반환한다() {
        //given
        given(userService.getUser(anyLong())).willReturn(mockUser);

        String title = "test title";
        String name = "test name";
        Long price = 123L;
        String description = "test description";

        //when
        assertThrows(IllegalStateException.class,
            () -> productService.create(mockUser.getId(), mockCategory.getId(), title, name, price, description, null));

        //then
        verify(uploadUtils, never()).isNotImageFile(anyString());
        verify(categoryService, never()).getCategory(anyLong());
        verify(userService).getUser(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    public void 이미지_없이_product를_생성할_수_있다() {
        //given
        mockUser.update("testNickname", Location.from("test address"));

        given(categoryService.getCategory(anyLong())).willReturn(mockCategory);
        given(userService.getUser(anyLong())).willReturn(mockUser);

        String title = "test title";
        String name = "test name";
        Long price = 123L;
        String description = "test description";

        //when
        productService.create(mockUser.getId(), mockCategory.getId(), title, name, price, description, null);

        //then
        verify(uploadUtils, never()).isNotImageFile(anyString());
        verify(categoryService).getCategory(anyLong());
        verify(userService).getUser(anyLong());
        verify(productRepository).save(any());
    }

    @Test
    public void 이미지와_함께_product를_생성할_수_있다() {
        //given
        mockUser.update("testNickname", Location.from("test address"));

        given(uploadUtils.isNotImageFile(anyString())).willReturn(false);
        given(categoryService.getCategory(anyLong())).willReturn(mockCategory);
        given(userService.getUser(anyLong())).willReturn(mockUser);

        String title = "test title";
        String name = "test name";
        Long price = 123L;
        String description = "test description";
        List<String> productImages = List.of("image1.png", "image2.jpeg");

        //when
        productService.create(mockUser.getId(), mockCategory.getId(), title, name, price, description, productImages);

        //then
        verify(uploadUtils, times(productImages.size())).isNotImageFile(anyString());
        verify(categoryService).getCategory(anyLong());
        verify(userService).getUser(anyLong());
        verify(productRepository).save(any());
    }

    @Test
    public void 업로드_가능한_이미지_형식은_jpg_jpeg_png만_가능하다() {
        //given
        mockUser.update("testNickname", Location.from("test address"));

        given(uploadUtils.isNotImageFile(anyString())).willReturn(true);

        String title = "test title";
        String name = "test name";
        Long price = 123L;
        String description = "test description";
        List<String> invalidProductImages = List.of("image1", "image2.jpeg");

        //when
        assertThrows(IllegalArgumentException.class,
                () -> productService.create(mockUser.getId(), mockCategory.getId(), title, name, price, description, invalidProductImages));
        //then
        verify(uploadUtils).isNotImageFile(anyString());
        verify(categoryService, never()).getCategory(anyLong());
        verify(userService, never()).getUser(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    public void 물품의_구매자를_업데이트_할_수_있다() {
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
    public void 판매_완료_처리시_물품상태변경과_Buyer를_업데이트_한다() {
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
    public void 물품의_상태를_변경할_수_있다() {
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
    public void 물품정보를_업데이트_할_수_있다() {
        //given
        given(productRepository.findByProductId(anyLong())).willReturn(Optional.ofNullable(mockProduct));
        given(categoryService.getCategory(anyLong())).willReturn(mockCategory);

        String newName = "newName";
        String newTitle = "newTitle";
        long newPrice = 12345L;
        String newDescription = "newDescription";

        long beforePrice = mockProduct.getPrice();
        if (newPrice < beforePrice) {
            doNothing().when(eventBus).post(any());
        }

        //when
        productService.update(mockProduct.getId(), newTitle, newName, mockCategory.getId(), newPrice, newDescription);

        //then
        assertThat(mockProduct.getName()).isEqualTo(newName);
        assertThat(mockProduct.getTitle()).isEqualTo(newTitle);
        assertThat(mockProduct.getPrice()).isEqualTo(newPrice);
        assertThat(mockProduct.getDescription()).isEqualTo(newDescription);
        verify(productRepository).findByProductId(anyLong());
        verify(categoryService).getCategory(anyLong());
    }

    @Test
    public void 물품을_삭제할_수_있다() {
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
    public void 물품_판매자를_식별할_수_있다() {
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
    public void 끌어올리기를_할_수_있다() {
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
    public void 끌어올리기는_5회까지만_할_수_있다() {
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
