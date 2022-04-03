package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.apiserver.controller.product.ProductResponse;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ProductService {
    ProductResponse.GetResponse getProduct(Long productId);
    ProductResponse.CreateResponse createProduct(String title, String name, Long categoryId, Long price, String description, List<String> imgUrlList);
    ProductResponse.UpdateResponse updateProduct(Long productId, String title, String name, Long categoryId, Long price, String description, List<String> imgUrlList);
    ProductResponse.DeleteResponse deleteProduct(Long productId);


}
