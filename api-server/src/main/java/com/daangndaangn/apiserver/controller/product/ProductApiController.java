package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
        Long userId = 1L;
        ProductResponse.CreateResponse createResponse = productService.createProduct(request, userId);
        return OK(createResponse);
    }

    @GetMapping("/{productId}")
    public ApiResult<ProductResponse.GetResponse> getProduct(@PathVariable("productId") Long productId){
        ProductResponse.GetResponse getResponse = productService.getProduct(productId);
        return OK(getResponse);
    }

    @GetMapping("/list")
    public ApiResult<List<ProductResponse.GetListResponse>> getProductList(Pageable pageable, @AuthenticationPrincipal JwtAuthentication authentication){
        Long userId = authentication.getId();
        return OK(productService.getProductList(userId, pageable));
    }

    @GetMapping("/list/filter")
    public ApiResult<List<ProductResponse.GetListResponse>> getProductListFilter(
            @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "minPrice", defaultValue = "0", required = false) Long minPrice, @RequestParam(value = "maxPrice", required = false) Long maxPrice,
            @RequestParam(value = "category", required = false) Long category, Pageable pageable, @AuthenticationPrincipal JwtAuthentication authentication){
        Long userId = authentication.getId();
        return OK(productService.getProductListFilter(keyword, minPrice, maxPrice, category, pageable, userId));
    }

    @PutMapping("/{productId}")
    public ApiResult updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody ProductRequest.CreateRequest request, @AuthenticationPrincipal JwtAuthentication authentication){
        String title = request.getTitle();
        String name = request.getName();
        String description = request.getDescription();
        Long category = request.getCategory();
        Long price = request.getPrice();
        Long userId = authentication.getId();
        productService.updateProduct(productId, title, name, category, price, description, userId);
        return OK();
    }

    @DeleteMapping("/{productId}")
    public ApiResult deleteProduct(@PathVariable("productId") Long productId, @AuthenticationPrincipal JwtAuthentication authentication){
        Long userId = authentication.getId();
        productService.deleteProduct(productId, userId);
        return OK();
    }
}
