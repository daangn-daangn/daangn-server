package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.apiserver.entity.product.Product;


import java.util.List;

public interface ProductService {
    Product findProduct(Long productId);
    ProductResponse.GetResponse getProduct(Long productId);
    ProductResponse.CreateResponse createProduct(String title, String name, Long categoryId, Long price, String description, List<String> imgUrlList, Long userId);
    ProductResponse.UpdateResponse updateProduct(Long productId, String title, String name, Long categoryId, Long price, String description, Long userId);
    ProductResponse.DeleteResponse deleteProduct(Long productId, Long userId);


}
