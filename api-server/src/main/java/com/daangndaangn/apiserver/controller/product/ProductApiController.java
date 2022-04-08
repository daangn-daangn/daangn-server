package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ApiResult<ProductResponse.CreateResponse> createProduct(@Valid @RequestBody ProductRequest.CreateRequest request, @AuthenticationPrincipal JwtAuthentication authentication) {
        String title = request.getTitle();
        String name = request.getName();
        String description = request.getDescription();
        Long category = request.getCategory();
        Long price = request.getPrice();
        List<String> imgUrlList = request.getImgUrlList();
        Long userId = authentication.getId();
        ProductResponse.CreateResponse createResponse = productService.createProduct(title, name, category, price, description, imgUrlList, userId);
        return OK(createResponse);
    }

    @GetMapping("/{productId}")
    public ApiResult<ProductResponse.GetResponse> getProduct(@PathVariable("productId") Long productId){
        ProductResponse.GetResponse getResponse = productService.getProduct(productId);
        return OK(getResponse);
    }

    @PutMapping("/{productId}")
    public ApiResult<ProductResponse.UpdateResponse> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody ProductRequest.CreateRequest request, @AuthenticationPrincipal JwtAuthentication authentication){
        String title = request.getTitle();
        String name = request.getName();
        String description = request.getDescription();
        Long category = request.getCategory();
        Long price = request.getPrice();
        Long userId = authentication.getId();
        ProductResponse.UpdateResponse updateResponse = productService.updateProduct(productId, title, name, category, price, description, userId);
        return OK(updateResponse);
    }

    @DeleteMapping("/{productId}")
    public ApiResult<ProductResponse.DeleteResponse> deleteProduct(@PathVariable("productId") Long productId, @AuthenticationPrincipal JwtAuthentication authentication){
        Long userId = authentication.getId();
        ProductResponse.DeleteResponse deleteResponse = productService.deleteProduct(productId, userId);
        return OK(deleteResponse);
    }
}
