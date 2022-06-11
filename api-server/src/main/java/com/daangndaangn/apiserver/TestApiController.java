package com.daangndaangn.apiserver;

import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.apiserver.service.manner.MannerService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.product.query.ProductQueryService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductQueryRepository;
import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import com.daangndaangn.common.api.repository.user.query.UserQueryDto;
import com.daangndaangn.common.api.repository.user.query.UserQueryRepository;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.repository.ChatRoomRepository;
import com.daangndaangn.common.web.ApiResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api/test")
@RequiredArgsConstructor
@RestController
public class TestApiController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;
    private final ProductQueryRepository productQueryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MannerService mannerService;

    @GetMapping("/manner")
    public void getManners(@RequestParam(value = "id") Long userId) {
        mannerService.createManner(userId, 2L, -5);
        mannerService.createManner(userId, 2L, -4);
        mannerService.createManner(userId, 2L, -3);
        mannerService.createManner(userId, 2L, -2);
        mannerService.createManner(userId, 2L, -1);
        mannerService.createManner(userId, 3L, 1);
        mannerService.createManner(userId, 3L, 2);
        mannerService.createManner(userId, 3L, 3);
        mannerService.createManner(userId, 3L, 4);
        mannerService.createManner(userId, 3L, 5);
        mannerService.createManner(userId, 4L, 5);
        mannerService.createManner(userId, 4L, -5);
    }

    @GetMapping("/no-product-image")
    public TestProductDto funcNoProductImages(@RequestParam(value = "id", required = false) Long id) {
        return TestProductDto.from(productService.getProduct(id != null ? id : 3L));
    }

    @GetMapping("/product-image")
    public TestProductDto funcProductImages(@RequestParam(value = "id", required = false) Long id) {
        return TestProductDto.from(productService.getProductWithProductImages(id != null ? id : 3L));
    }

    @Getter
    @AllArgsConstructor
    public static class TestProductDto {
        private Long id;
        private List<String> productImages;

        public static TestProductDto from(Product product) {
            return new TestProductDto(product.getId(), product.getProductImages().stream().map(productImage -> productImage.getImageUrl()).collect(Collectors.toList()));
        }
    }

    @GetMapping
    public void func000() {

        ChatRoom chatRoom1 = ChatRoom.builder()
                .productId(1L)
                .productImage(null)
                .firstUserId(1L)
                .secondUserId(2L)
                .identifier("1-2")
                .build();

        ChatRoom chatRoom2 = ChatRoom.builder()
                .productId(1L)
                .productImage(null)
                .firstUserId(1L)
                .secondUserId(3L)
                .identifier("1-3")
                .build();

        ChatRoom chatRoom3 = ChatRoom.builder()
                .productId(1L)
                .productImage(null)
                .firstUserId(1L)
                .secondUserId(4L)
                .identifier("1-4")
                .build();

        ChatRoom chatRoom4 = ChatRoom.builder()
                .productId(1L)
                .productImage(null)
                .firstUserId(1L)
                .secondUserId(5L)
                .identifier("1-5")
                .build();

        chatRoomRepository.save(chatRoom1);
        chatRoomRepository.save(chatRoom2);
        chatRoomRepository.save(chatRoom3);
        chatRoomRepository.save(chatRoom4);
    }

    @GetMapping("/v1")
    public List<ProductQueryDto> func(ProductSearchOption productSearchOption) {
        return productQueryRepository.findAll(productSearchOption, null, PageRequest.of(0, 20));
    }

    @GetMapping("/v2")
    public List<ProductResponse.SimpleResponse> func2(ProductSearchOption productSearchOption) {
        return productQueryService.getProducts(productSearchOption, PageRequest.of(0, 20));
    }

    @GetMapping("/v3")
    public ApiResult<List<ProductResponse.SimpleResponse>> func3(ProductSearchOption productSearchOption,
                                                                 @PageableDefault(size = 5) Pageable pageable) {

        return ApiResult.OK(productQueryService.getProducts(productSearchOption, pageable));
    }
}
