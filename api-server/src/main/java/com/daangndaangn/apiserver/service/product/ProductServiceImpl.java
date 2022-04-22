package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.apiserver.entity.category.Category;
import com.daangndaangn.apiserver.entity.product.Product;
import com.daangndaangn.apiserver.entity.product.ProductState;
import com.daangndaangn.apiserver.entity.user.User;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.repository.ProductRepository;
import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Override
    public Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, String.format("productId = %s", productId)));
    }

    @Override
    public ProductResponse.GetResponse getProduct(Long productId){
        Product product = this.findProduct(productId);
        return convertToDto(product);
    }
    @Override
    @Transactional(readOnly = true)
    public ProductResponse.CreateResponse createProduct(String title, String name, Long categoryId, Long price, String description, List<String> imgUrlList, Long userId) {
        Category category = categoryService.findCategory(categoryId);
        User user = userService.findUser(userId);
        Product product = Product.builder()
                .title(title)
                .name(name)
                .category(category)
                .price(price)
                .state(ProductState.FOR_SALE)
                .description(description)
                .seller(user)
                .buyer(null)
                .imgUrlList(imgUrlList)
                .build();
        return ProductResponse.CreateResponse.from(productRepository.save(product).getId());
    }

    @Override
    @Transactional
    public ProductResponse.UpdateResponse updateProduct(Long productId, String title, String name, Long categoryId, Long price, String description, Long userId){
        Product product = this.findProduct(productId);
        if(product.getSeller().getId() != userId){
            throw new  NotFoundException(Product.class, String.format("productId = %s", productId));
        }
        Category category = categoryService.findCategory(categoryId);
        product.updateInfo(title, name, category, price, description);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId, Long userId) {
        Product product = this.findProduct(productId);
        if(product.getSeller().getId() != userId){
            throw new  NotFoundException(Product.class, String.format("productId = %s", productId));
        }
        product.updateState(ProductState.DELETED);
        productRepository.save(product);
    }

    private ProductResponse.GetResponse convertToDto(Product product) {
        return ProductResponse.GetResponse.from(product);
    }

    private ProductResponse.GetListResponse convertToDtoList(Product product) {
        return ProductResponse.GetListResponse.from(product);
    }
}
