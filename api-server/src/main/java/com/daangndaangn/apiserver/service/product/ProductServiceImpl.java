package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.controller.product.ProductRequest;
import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductState;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import com.daangndaangn.apiserver.service.category.CategoryService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
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
    public List<ProductResponse.GetListResponse> getProductList(Long userId, Pageable pageable){
        return this.getProductListWithLocation(userId, pageable).stream()
                .map(this::convertToDtoList)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductListWithLocation(Long userId, Pageable pageable) {
        Location location = userService.findUser(userId).getLocation();
        return productRepository.findAllProductByLocation(location, pageable);
    }

    @Override
    public List<ProductResponse.GetListResponse> getProductListFilter(String keyword, Long minPrice, Long maxPrice, Long categoryId, Pageable pageable, Long userId){
        return this.getProductListWithFilter(keyword, minPrice, maxPrice, categoryId, pageable, userId).stream()
                .map(this::convertToDtoList)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductListWithFilter(String keyword, Long minPrice, Long maxPrice, Long categoryId, Pageable pageable, Long userId){
        Preconditions.checkArgument(minPrice < maxPrice, "유효하지 않는 범위입니다.");
        Location location = userService.findUser(userId).getLocation();
        return productRepository.findAllProductByFilter(keyword, minPrice, maxPrice, categoryId, location, pageable);
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
                .chattingCount(0L)
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
