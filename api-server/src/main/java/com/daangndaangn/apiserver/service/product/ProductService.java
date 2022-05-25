package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.controller.product.ProductRequest;
import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.common.api.entity.product.Product;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ProductService {
    /*
    For DTO
    */
    ProductResponse.GetResponse getProduct(Long productId);
    ProductResponse.CreateResponse createProduct(ProductRequest.CreateRequest request, Long userId);
    List<ProductResponse.GetListResponse> getProductList(String keyword, Long minPrice, Long maxPrice, Long categoryId, Long userId, Pageable pageable);


    /*
    For Entity
    */
    //단일 조회
    Product findProduct(Long productId);
    //생성
    Product createProduct(String title, String name, Long categoryId, Long price, String description, List<String> imgUrlList, Long userId);
    //복수 조회 with 조건
    List<Product> findProductList(String keyword, Long minPrice, Long maxPrice, Long categoryId, Long userId, Pageable pageable);
    /*
    For 공용
    */
    void updateProduct(Long productId, String title, String name, Long categoryId, Long price, String description, Long userId);
    void deleteProduct(Long productId, Long userId);

}
