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
    @Transactional
    public Product createProduct(String title, String name, Long categoryId, Long price, String description, List<String> imgUrlList, Long userId){
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
        return productRepository.save(product);
    }
    @Override
    @Transactional
    public ProductResponse.CreateResponse createProduct(ProductRequest.CreateRequest request, Long userId) {
        String title = request.getTitle();
        String name = request.getName();
        Long categoryId = request.getCategory();
        Long price = request.getPrice();
        String description = request.getDescription();
        List<String> imgUrlList = request.getImgUrlList();
        return ProductResponse.CreateResponse.from(this.createProduct(title, name, categoryId, price, description, imgUrlList, userId).getId());
    }

    @Override
    @Transactional
    public void updateProduct(Long productId, String title, String name, Long categoryId, Long price, String description, Long userId){
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
