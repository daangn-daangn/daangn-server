package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductState;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.category.CategoryRepository;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import org.junit.jupiter.api.*;
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
class ProductServiceTest {

    private final Long MOCK_SELLER_OAUTH_ID = 456L;
    private final Long MOCK_BUYER_OAUTH_ID = 111L;
    private Long mockSellerId, mockBuyerId, mockCategoryId, mockCategoryId2;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;  // deleteAll 호출 용도

    @Autowired
    private ProductRepository productRepository;    // deleteAll 호출 용도

    @BeforeAll
    public void init() {
        mockCategoryId = categoryService.create("test_category1");
        mockCategoryId2 = categoryService.create("test_category2");

        mockSellerId = userService.create(MOCK_SELLER_OAUTH_ID, null);
        User seller = userService.getUser(mockSellerId);
        userService.update(seller.getId(), "테스트 유저", "노원구 상계동");
        mockBuyerId = userService.create(MOCK_BUYER_OAUTH_ID, null);
    }

    @Test
    public void product를_생성할_수_있다() {
        //given
        String title = "mockProduct 팝니다.";
        String name = "mockProduct";
        Long price = 10000L;
        String description = "mockProduct 팝니다. 새 상품입니다.";

        //when
        Long productId = productService.create(mockSellerId, mockCategoryId, title, name, price, description);
        Product product = productService.getProduct(productId);

        //then
        assertThat(product.getSeller().getId()).isEqualTo(mockSellerId);
        assertThat(product.getCategory().getId()).isEqualTo(mockCategoryId);
        assertThat(product.getTitle()).isEqualTo(title);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getBuyer()).isNull();
    }

    @Test
    public void 물품의_구매자를_업데이트_할_수_있다() {
        //given
        String title = "mockProduct 팝니다.";
        String name = "mockProduct";
        Long price = 10000L;
        String description = "mockProduct 팝니다. 새 상품입니다.";

        Long productId = productService.create(mockSellerId, mockCategoryId, title, name, price, description);
        assertThat(productService.getProduct(productId).getBuyer()).isNull();

        //when
        User buyer = userService.getUser(mockBuyerId);
        productService.update(productId, buyer.getId());

        //then
        assertThat(productService.getProduct(productId).getBuyer()).isEqualTo(buyer);
    }

    @Test
    public void 물품의_상태를_변경할_수_있다() {
        //given
        String title = "mockProduct 팝니다.";
        String name = "mockProduct";
        Long price = 10000L;
        String description = "mockProduct 팝니다. 새 상품입니다.";

        Long productId = productService.create(mockSellerId, mockCategoryId, title, name, price, description);
        assertThat(productService.getProduct(productId).getState()).isEqualTo(ProductState.FOR_SALE);

        //when
        int soldOutCode = ProductState.SOLD_OUT.getCode();
        productService.update(productId, soldOutCode);
        assertThat(productService.getProduct(productId).getState()).isEqualTo(ProductState.SOLD_OUT);

        int deleteCode = ProductState.DELETED.getCode();
        productService.update(productId, deleteCode);
        assertThat(productService.getProduct(productId).getState()).isEqualTo(ProductState.DELETED);
    }

    //void update(Long id, String title, String name, Long categoryId, Long price, String description);
    @Test
    public void 물품정보를_업데이트_할_수_있다() {
        //given
        String beforeTitle = "mockProduct 팝니다.";
        String beforeName = "mockProduct";
        Long beforePrice = 10000L;
        String beforeDescription = "mockProduct 팝니다. 새 상품입니다.";

        Long productId = productService.create(mockSellerId,
                                                mockCategoryId,
                                                beforeTitle,
                                                beforeName,
                                                beforePrice,
                                                beforeDescription);

        //when
        String newTitle = "newProduct 팝니다.";
        String newName = "newProduct";
        Long newPrice = 9000L;
        String newDescription = "newProduct 팝니다. 새 상품입니다.";

        productService.update(productId, newTitle, newName, mockCategoryId2, newPrice, newDescription);

        //then
        Product product = productService.getProduct(productId);
        assertThat(product.getTitle()).isEqualTo(newTitle);
        assertThat(product.getName()).isEqualTo(newName);
        assertThat(product.getCategory().getId()).isEqualTo(mockCategoryId2);
        assertThat(product.getPrice()).isEqualTo(newPrice);
        assertThat(product.getDescription()).isEqualTo(newDescription);
    }

    @Test
    public void 물품을_삭제할_수_있다() {
        //given
        String title = "mockProduct 팝니다.";
        String name = "mockProduct";
        Long price = 10000L;
        String description = "mockProduct 팝니다. 새 상품입니다.";

        Long productId = productService.create(mockSellerId, mockCategoryId, title, name, price, description);

        //when
        productService.delete(productId);

        //then
        assertThat(productService.getProduct(productId).getState()).isEqualTo(ProductState.DELETED);
    }

    @AfterAll
    public void destroy() {
        userService.delete(mockSellerId);
        userService.delete(mockBuyerId);
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}