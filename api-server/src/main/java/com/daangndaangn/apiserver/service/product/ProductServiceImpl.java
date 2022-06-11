package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductState;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.util.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final UserService userService;

    private final CategoryService categoryService;

    private final UploadUtils uploadUtils;

    @Override
    @Transactional
    public Product create(Long sellerId, Long categoryId, String title, String name, Long price, String description, List<String> productImageUrls) {
        checkArgument(sellerId != null, "sellerId must not be null");
        checkArgument(categoryId != null, "categoryId must not be null");
        checkArgument(isNotEmpty(title), "title must not be null");
        checkArgument(isNotEmpty(name), "name must not be null");
        checkArgument(price != null, "price must not be null");
        checkArgument(isNotEmpty(description), "description must not be null");
        checkArgument(isEmpty(productImageUrls) || productImageUrls.size() <= 10, "사진은 최대 10장만 등록할 수 있습니다.");

        if (isNotEmpty(productImageUrls)) {

            boolean isNotValid = productImageUrls.stream().anyMatch(uploadUtils::isNotImageFile);

            if (isNotValid) {
                throw new IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");
            }
        }

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

        if (isNotEmpty(productImageUrls)) {

            List<String> randomProductImageUrls = productImageUrls.stream()
                                                    .filter(StringUtils::isNotEmpty)
                                                    .map(productImageUrl -> UUID.randomUUID() + productImageUrl)
                                                    .collect(toList());

            product.setThumbnailImage(randomProductImageUrls.get(0));

            for (String randomProductImageUrl : randomProductImageUrls) {
                product.addProductImage(randomProductImageUrl);
            }
        }

        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        checkArgument(id != null, "product id must not be null");

        return productRepository.findByProductId(id)
            .orElseThrow(() -> new NotFoundException(Product.class, String.format("productId = %s", id)));
    }

    @Override
    public Product getProductWithProductImages(Long id) {
        checkArgument(id != null, "product id must not be null");

        return productRepository.findByProductIdWithProductImages(id)
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
