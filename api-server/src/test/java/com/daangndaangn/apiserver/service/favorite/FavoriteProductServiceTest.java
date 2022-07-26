package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.favorite.FavoriteProductRepository;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.error.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteProductServiceTest {

    @InjectMocks
    private FavoriteProductServiceImpl favoriteProductService;

    @Mock
    private FavoriteProductRepository favoriteProductRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    private User mockUser;
    private Product mockProduct;
    private FavoriteProduct mockFavoriteProduct;

    @BeforeEach
    public void init() {
        mockUser = User.builder().id(1L).oauthId(1234L).build();
        User mockBuyer = User.builder().id(2L).oauthId(5678L).build();
        mockUser.update("테스트 유저", Location.from("노원구 상계동"));

        mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser)
                .category(Category.builder().id(1L).name("mockCategory").build())
                .price(10000)
                .title("mockProduct 팝니다.")
                .description("mockProduct 팝니다. 새 상품 입니다.")
                .build();

        mockProduct.updateBuyer(mockBuyer);

        mockFavoriteProduct = FavoriteProduct.builder().id(1L).product(mockProduct).user(mockUser).build();
    }

    @Test
    @DisplayName("물품을_찜할_수_있다_처음_찜하는_상품은_getUser와_getProduct와_save를_사용한다")
    public void create1() {
        //given
        given(favoriteProductRepository.findByProductAndUser(any(),any())).willReturn(Optional.empty());
        given(userService.getUser(any())).willReturn(mockUser);
        given(productService.getProduct(any())).willReturn(mockProduct);
        given(favoriteProductRepository.save(any())).willReturn(mockFavoriteProduct);
        given(favoriteProductRepository.findById(anyLong())).willReturn(Optional.of(mockFavoriteProduct));

        //when
        Long mockFavoriteProductId = mockFavoriteProduct.getId();
        favoriteProductService.create(1L, 1L);
        FavoriteProduct favoriteProduct = favoriteProductService.getFavoriteProduct(mockFavoriteProductId);

        //then
        assertThat(favoriteProduct).isEqualTo(mockFavoriteProduct);
        assertThat(favoriteProduct.isValid()).isEqualTo(true);
        assertThat(favoriteProduct.getUser()).isEqualTo(mockFavoriteProduct.getUser());
        assertThat(favoriteProduct.getProduct()).isEqualTo(mockFavoriteProduct.getProduct());
        assertThat(favoriteProduct.isValid()).isEqualTo(mockFavoriteProduct.isValid());

        verify(favoriteProductRepository).findByProductAndUser(any(),any());
        verify(userService).getUser(any());
        verify(productService).getProduct(any());
        verify(favoriteProductRepository).save(any());
    }

    @Test
    @DisplayName("물품을_찜할_수_있다_이미_찜했던_상품이지만_취소했던_경우_isValid_필드를_활성화한다")
    public void create2() {
        //given
        given(favoriteProductRepository.findByProductAndUser(any(),any())).willReturn(Optional.of(mockFavoriteProduct));
        lenient().when(userService.getUser(any())).thenReturn(mockUser);
        lenient().when(productService.getProduct(any())).thenReturn(mockProduct);
        given(favoriteProductRepository.findById(anyLong())).willReturn(Optional.of(mockFavoriteProduct));

        //when
        Long mockFavoriteProductId = mockFavoriteProduct.getId();
        favoriteProductService.create(1L, 1L);
        FavoriteProduct favoriteProduct = favoriteProductService.getFavoriteProduct(mockFavoriteProductId);

        //then
        assertThat(favoriteProduct).isEqualTo(mockFavoriteProduct);
        assertThat(favoriteProduct.isValid()).isEqualTo(true);
        assertThat(favoriteProduct.getUser()).isEqualTo(mockFavoriteProduct.getUser());
        assertThat(favoriteProduct.getProduct()).isEqualTo(mockFavoriteProduct.getProduct());
        assertThat(favoriteProduct.isValid()).isEqualTo(mockFavoriteProduct.isValid());

        verify(favoriteProductRepository).findByProductAndUser(any(),any());
        verify(favoriteProductRepository, never()).save(any());
        verify(userService, never()).getUser(any());
        verify(productService, never()).getProduct(any());
    }

    @Test
    @DisplayName("물품을_찜할_수_있다_이미_찜했던_상품이지만_취소했던_경우_isValid_필드를_활성화한다")
    public void create3() {
        //given
        Long invalidProductId = null;
        Long invalidUserId = null;

        //when
        assertThatThrownBy(() -> favoriteProductService.create(invalidProductId, mockUser.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> favoriteProductService.create(mockProduct.getId(), invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(favoriteProductRepository, never()).findByProductAndUser(any(),any());
        verify(favoriteProductRepository, never()).save(any());
        verify(userService, never()).getUser(any());
        verify(productService, never()).getProduct(any());
    }


    @Test
    @DisplayName("id로 favoriteProduct를 조회할 수 있다")
    public void getFavoriteProduct1() {
        //given
        given(favoriteProductRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockFavoriteProduct));

        //when
        FavoriteProduct favoriteProduct = favoriteProductService.getFavoriteProduct(mockFavoriteProduct.getId());

        //then
        assertThat(favoriteProduct).isEqualTo(mockFavoriteProduct);
        verify(favoriteProductRepository).findById(anyLong());
    }

    @Test
    @DisplayName("id로 favoriteProduct 조회 시 없으면 예외를 반환한다")
    public void getFavoriteProduct2() {
        //given
        Long invalidId = 5L;
        given(favoriteProductRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> favoriteProductService.getFavoriteProduct(invalidId))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(favoriteProductRepository).findById(anyLong());
    }

    @Test
    @DisplayName("사용자가_찜한_모든_물품목록을_반환한다")
    public void getFavoriteProductsByUser() {
        //given
        List<FavoriteProduct> mockFavoriteProducts
                = Stream.of(new FavoriteProduct[]{mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct,
                        mockFavoriteProduct,mockFavoriteProduct, mockFavoriteProduct,
                        mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct})
                .collect(toList());

        Long userId = mockUser.getId();
        Pageable mockPageRequest = PageRequest.of(0, mockFavoriteProducts.size());
        given(favoriteProductRepository.findAll(anyLong(), any(PageRequest.class))).willReturn(mockFavoriteProducts);

        //when
        List<FavoriteProduct> result = favoriteProductService.getFavoriteProductsByUser(userId, mockPageRequest);

        //then
        assertThat(result.size()).isEqualTo(mockFavoriteProducts.size());
        verify(favoriteProductRepository).findAll(anyLong(), any(PageRequest.class));
    }

    @Test
    @DisplayName("특정_물품을_찜한_모든_사용자목록을_반환한다")
    public void getFavoriteProductsByProduct() {
        //given
        List<FavoriteProduct> mockFavoriteProducts
                = Stream.of(new FavoriteProduct[]{mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct,
                        mockFavoriteProduct,mockFavoriteProduct, mockFavoriteProduct,
                        mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct})
                .collect(toList());

        Long productId = mockProduct.getId();
        given(favoriteProductRepository.findAll(anyLong())).willReturn(mockFavoriteProducts);

        //when
        List<FavoriteProduct> result = favoriteProductService.getFavoriteProductsByProduct(productId);

        //then
        assertThat(result.size()).isEqualTo(mockFavoriteProducts.size());
        verify(favoriteProductRepository).findAll(anyLong());
    }

    @Test
    @DisplayName("물품_찜하기를_해제하면_isValid_필드값이_false가_된다")
    public void deleteFavoriteProductId() {
        //given
        given(favoriteProductRepository.findById(1L)).willReturn(Optional.of(mockFavoriteProduct));
        assertThat(mockFavoriteProduct.isValid()).isEqualTo(true);

        //when
        favoriteProductService.delete(1L);

        //then
        assertThat(mockFavoriteProduct.isValid()).isEqualTo(false);
        verify(favoriteProductRepository).findById(1L);
    }

    @Test
    @DisplayName("물품_찜하기를_해제하면_isValid_필드값이_false가_된다")
    public void deleteByProductIdWithUserId1() {
        //given
        given(favoriteProductRepository.findByProductAndUser(anyLong(), anyLong()))
                .willReturn(Optional.of(mockFavoriteProduct));

        assertThat(mockFavoriteProduct.isValid()).isEqualTo(true);

        //when
        favoriteProductService.delete(mockProduct.getId(), mockUser.getId());

        //then
        assertThat(mockFavoriteProduct.isValid()).isEqualTo(false);
        verify(favoriteProductRepository).findByProductAndUser(anyLong(), anyLong());
    }

    @Test
    @DisplayName("productId와 userId에 해당하는 찜한 물품이 없다면 예외를 반환한다")
    public void deleteByProductIdWithUserId2() {
        //given
        given(favoriteProductRepository.findByProductAndUser(anyLong(), anyLong()))
                .willReturn(Optional.empty());

        assertThat(mockFavoriteProduct.isValid()).isEqualTo(true);

        //when
        assertThatThrownBy(() -> favoriteProductService.delete(mockProduct.getId(), mockUser.getId()))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(favoriteProductRepository).findByProductAndUser(anyLong(), anyLong());
    }

    @Test
    @DisplayName("찜하기를_누른_사용자를_식별할_수_있다")
    public void isOwner1() {
        //given
        given(favoriteProductRepository.findByIdWithUser(anyLong()))
            .willReturn(Optional.ofNullable(mockFavoriteProduct));

        long otherUserId = 5L;

        //when
        boolean result1 = favoriteProductService.isOwner(mockFavoriteProduct.getId(), otherUserId);
        boolean result2 = favoriteProductService.isOwner(mockFavoriteProduct.getId(), mockUser.getId());

        //then
        assertThat(result1).isEqualTo(false);
        assertThat(result2).isEqualTo(true);
        verify(favoriteProductRepository, times(2)).findByIdWithUser(anyLong());
    }

    @Test
    @DisplayName("찜하기를_누른_사용자가 없을 시 예외를 반환한다")
    public void isOwner2() {
        //given
        given(favoriteProductRepository.findByIdWithUser(anyLong()))
                .willReturn(Optional.empty());

        long invalidUserId = 5L;

        //when
        assertThatThrownBy(() -> favoriteProductService.isOwner(mockFavoriteProduct.getId(), invalidUserId))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(favoriteProductRepository).findByIdWithUser(anyLong());
    }

    @Test
    @DisplayName("찜하기를_누른_사용자 조회 시 id와 userId는 필수이다")
    public void isOwner3() {
        //given
        Long invalidId = null;
        long userId = 5L;
        Long invalidUserId = null;

        //when
        assertThatThrownBy(() -> favoriteProductService.isOwner(mockFavoriteProduct.getId(), invalidUserId))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> favoriteProductService.isOwner(invalidId, userId))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(favoriteProductRepository, never()).findByIdWithUser(anyLong());
    }


}