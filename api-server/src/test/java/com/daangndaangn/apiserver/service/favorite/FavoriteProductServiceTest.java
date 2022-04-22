package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.apiserver.entity.category.Category;
import com.daangndaangn.apiserver.entity.favorite.FavoriteProduct;
import com.daangndaangn.apiserver.entity.product.Product;
import com.daangndaangn.apiserver.entity.product.ProductState;
import com.daangndaangn.apiserver.entity.user.User;
import com.daangndaangn.apiserver.repository.favorite.FavoriteProductRepository;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
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
        mockUser = User.builder().oauthId(1234L).profileUrl(null).build();
        User mockBuyer = User.builder().oauthId(5678L).profileUrl(null).build();

        mockProduct = Product.builder()
                .seller(mockUser)
                .buyer(mockBuyer)
                .category(Category.from("mockCategory"))
                .name("mockProduct")
                .price(10000)
                .title("mockProduct 팝니다.")
                .description("mockProduct 팝니다. 새 상품 입니다.")
                .state(ProductState.FOR_SALE)
                .imgUrlList(Stream.of(new String[]{"mockImgUrl"}).collect(toList()))
                .build();

        mockFavoriteProduct = FavoriteProduct.builder().product(mockProduct).user(mockUser).build();
    }

    // create Test
    @Test
    public void 물품을_찜할_수_있다_처음_찜하는_상품은_findUser와_findProduct를_사용한다() {
        //given
        given(favoriteProductRepository.findByProductAndUser(any(),any())).willReturn(Optional.empty());
        given(userService.findUser(any())).willReturn(mockUser);
        given(productService.findProduct(any())).willReturn(mockProduct);
        given(favoriteProductRepository.save(any())).willReturn(mockFavoriteProduct);

        //when
        FavoriteProduct favoriteProduct = favoriteProductService.create(1L, 1L);

        //then
        assertThat(favoriteProduct).isEqualTo(mockFavoriteProduct);
        assertThat(favoriteProduct.isValid()).isEqualTo(true);
        assertThat(favoriteProduct.getUser()).isEqualTo(mockFavoriteProduct.getUser());
        assertThat(favoriteProduct.getProduct()).isEqualTo(mockFavoriteProduct.getProduct());
        assertThat(favoriteProduct.isValid()).isEqualTo(mockFavoriteProduct.isValid());

        verify(favoriteProductRepository).findByProductAndUser(any(),any());
        verify(userService).findUser(any());
        verify(productService).findProduct(any());
        verify(favoriteProductRepository).save(any());
    }

    // create Test
    @Test
    public void 물품을_찜할_수_있다_이미_찜했던_상품은_findUser와_findProduct를_하지_않는다() {
        //given
        given(favoriteProductRepository.findByProductAndUser(any(),any())).willReturn(Optional.of(mockFavoriteProduct));
        Mockito.lenient().when(userService.findUser(any())).thenReturn(mockUser);
        Mockito.lenient().when(productService.findProduct(any())).thenReturn(mockProduct);
        given(favoriteProductRepository.save(any())).willReturn(mockFavoriteProduct);

        //when
        FavoriteProduct favoriteProduct = favoriteProductService.create(1L, 1L);

        //then
        assertThat(favoriteProduct).isEqualTo(mockFavoriteProduct);
        assertThat(favoriteProduct.isValid()).isEqualTo(true);
        assertThat(favoriteProduct.getUser()).isEqualTo(mockFavoriteProduct.getUser());
        assertThat(favoriteProduct.getProduct()).isEqualTo(mockFavoriteProduct.getProduct());
        assertThat(favoriteProduct.isValid()).isEqualTo(mockFavoriteProduct.isValid());

        verify(favoriteProductRepository).findByProductAndUser(any(),any());
        verify(favoriteProductRepository).save(any());
        verify(userService, never()).findUser(any());
        verify(productService, never()).findProduct(any());
    }

    // findAll Test
    @Test
    public void 사용자가_찜한_모든_물품목록을_반환한다() {
        //given
        List<FavoriteProduct> mockFavoriteProducts
                = Stream.of(new FavoriteProduct[]{mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct,
                        mockFavoriteProduct,mockFavoriteProduct, mockFavoriteProduct,
                        mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct,mockFavoriteProduct})
                .collect(toList());

        Long userId = 1L;
        Pageable mockPageRequest = PageRequest.of(0, mockFavoriteProducts.size());
        given(favoriteProductRepository.findAll(anyLong(), any(PageRequest.class))).willReturn(mockFavoriteProducts);

        //when
        List<FavoriteProduct> result = favoriteProductService.findAll(userId, mockPageRequest);

        //then
        assertThat(result.size()).isEqualTo(mockFavoriteProducts.size());
        verify(favoriteProductRepository).findAll(anyLong(), any(PageRequest.class));
    }

    // delete Test
    @Test
    public void 물품_찜하기를_해제하면_isValid_필드값이_false가_된다() {
        //given
        given(favoriteProductRepository.findById(1L)).willReturn(Optional.of(mockFavoriteProduct));
        assertThat(mockFavoriteProduct.isValid()).isEqualTo(true);

        //when
        favoriteProductService.delete(1L);

        //then
        assertThat(mockFavoriteProduct.isValid()).isEqualTo(false);
        verify(favoriteProductRepository).findById(1L);
    }
}