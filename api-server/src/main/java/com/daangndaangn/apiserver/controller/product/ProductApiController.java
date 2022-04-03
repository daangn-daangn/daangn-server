package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.daangndaangn.apiserver.controller.ApiResult.OK;

@RequestMapping("/api/product")
@RestController
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    @PostMapping
    public ApiResult<ProductResponse.CreateResponse> createProduct(@Valid @RequestBody ProductRequest.CreateRequest request) {
        String title = request.getTitle();
        String name = request.getName();
        String description = request.getDescription();
        Long category = request.getCategory();
        Long price = request.getPrice();
        List<String> imgUrlList = request.getImgUrlList();
        ProductResponse.CreateResponse createResponse = productService.createProduct(title, name, category, price, description, imgUrlList);
        return OK(createResponse);
    }

    @GetMapping("/{productId}")
    public ApiResult<ProductResponse.GetResponse> getProduct(@PathVariable("productId") Long productId){
        ProductResponse.GetResponse getResponse = productService.getProduct(productId);
        return OK(getResponse);
    }

    @PutMapping("/{productId}")
    public ApiResult<ProductResponse.UpdateResponse> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody ProductRequest.CreateRequest request){
        String title = request.getTitle();
        String name = request.getName();
        String description = request.getDescription();
        Long category = request.getCategory();
        Long price = request.getPrice();
        List<String> imgUrlList = request.getImgUrlList();
        ProductResponse.UpdateResponse updateResponse = productService.updateProduct(productId, title, name, category, price, description, imgUrlList);
        return OK(updateResponse);
    }

    @DeleteMapping("/{productId}")
    public ApiResult<ProductResponse.DeleteResponse> updateProduct(@PathVariable("productId") Long productId){
        ProductResponse.DeleteResponse deleteResponse = productService.deleteProduct(productId);
        return OK(deleteResponse);
    }
}
