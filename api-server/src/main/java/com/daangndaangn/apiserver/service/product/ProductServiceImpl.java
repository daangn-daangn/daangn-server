package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductState;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final UserService userService;

    private final CategoryService categoryService;

    @Override
    @Transactional
    public Long create(Long sellerId, Long categoryId, String title, String name, Long price, String description) {
        checkArgument(sellerId != null, "sellerId must not be null");
        checkArgument(categoryId != null, "categoryId must not be null");
        checkArgument(isNotEmpty(title), "title must not be null");
        checkArgument(isNotEmpty(name), "name must not be null");
        checkArgument(price != null, "price must not be null");
        checkArgument(isNotEmpty(description), "description must not be null");

        User seller = userService.getUser(sellerId);
        Category category = categoryService.getCategory(categoryId);

        Product product = Product.builder()
                .seller(seller)
                .category(category)
                .title(title)
                .name(name)
                .price(price)
                .description(description)
                .build();

        return productRepository.save(product).getId();
    }

    //TODO
    @Override
    public List<Product> getProducts(Pageable pageable) {
        return null;
    }

    @Override
    public Product getProduct(Long id) {
        checkArgument(id != null, "product id must not be null");

        return productRepository.findByProductId(id)
            .orElseThrow(() -> new NotFoundException(Product.class, String.format("productId = %s", id)));
    }

    @Override
    @Transactional
    public void updateToSoldOut(Long id, Long buyerId) {
        checkArgument(buyerId != null, "buyerId id must not be null");

        Product updatedProduct = getProduct(id);
        User buyer = userService.getUser(buyerId);
        updatedProduct.updateState(ProductState.SOLD_OUT);
        updatedProduct.updateBuyer(buyer);
    }

    @Override
    @Transactional
    public void update(Long id, Long buyerId) {
        checkArgument(buyerId != null, "buyerId id must not be null");

        Product product = getProduct(id);
        User buyer = userService.getUser(buyerId);
        product.updateBuyer(buyer);
    }

    @Override
    @Transactional
    public void update(Long id, int productStateCode) {
        Product product = getProduct(id);
        ProductState productState = ProductState.from(productStateCode);
        product.updateState(productState);
    }

    @Override
    @Transactional
    public void update(Long id, String title, String name, Long categoryId, Long price, String description) {
        checkArgument(isNotEmpty(title), "title must not be null");
        checkArgument(isNotEmpty(name), "name must not be null");
        checkArgument(categoryId != null, "categoryId must not be null");
        checkArgument(price != null, "price must not be null");
        checkArgument(isNotEmpty(description), "description must not be null");

        Product updatedProduct = getProduct(id);
        Category category = categoryService.getCategory(categoryId);

        updatedProduct.updateInfo(title, name, category, price, description);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product deleteProduct = getProduct(id);
        deleteProduct.updateState(ProductState.DELETED);
    }

    @Override
    public boolean isSeller(Long productId, Long userId) {
        checkArgument(productId != null, "productId must not be null");
        checkArgument(userId != null, "userId must not be null");

        Product product = productRepository.findByProductIdWithOnlySeller(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, String.format("productId = %s", productId)));

        return userId.equals(product.getSeller().getId());
    }
}
