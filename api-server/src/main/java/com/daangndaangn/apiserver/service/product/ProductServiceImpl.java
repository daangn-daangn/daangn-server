package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.apiserver.entity.category.Category;
import com.daangndaangn.apiserver.entity.product.Product;
import com.daangndaangn.apiserver.entity.product.ProductState;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.repository.ProductRepository;
import com.daangndaangn.apiserver.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Override
    public ProductResponse.GetResponse getProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(Product.class, String.format("productId = %s", productId)));
        return convertToDto(product);
    }
    @Override
    @Transactional
    public ProductResponse.CreateResponse createProduct(String title, String name, Long categoryId, Long price, String description, List<String> imgUrlList) {
        Category category = categoryService.getCategory(categoryId);
        Product product = Product.builder()
                .title(title)
                .name(name)
                .category(category)
                .price(price)
                .state(ProductState.FOR_SALE)
                .description(description)
                .seller(null)
                .buyer(null)
                .imgUrlList(imgUrlList)
                .build();
        return ProductResponse.CreateResponse.of(productRepository.save(product).getId());
    }

    @Override
    @Transactional
    public ProductResponse.UpdateResponse updateProduct(Long productId, String title, String name, Long categoryId, Long price, String description, List<String> imgUrlList){
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(Product.class, String.format("productId = %s", productId)));
        Category category = categoryService.getCategory(categoryId);
        product.setTitle(title);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setDescription(description);
        product.setProductImageList(imgUrlList);
        product.setThumbNailImage(imgUrlList);

        return ProductResponse.UpdateResponse.of(productRepository.save(product).getId());
    }

    @Override
    @Transactional
    public ProductResponse.DeleteResponse deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(Product.class, String.format("productId = %s", productId)));
        productRepository.delete(product);
        return ProductResponse.DeleteResponse.of(true);
    }

    private ProductResponse.GetResponse convertToDto(Product product) {
        return ProductResponse.GetResponse.from(product);
    }
}
