package com.daangndaangn.apiserver.controller.chatting;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.controller.product.ProductRequest;
import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.chatting.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.daangndaangn.apiserver.controller.ApiResult.OK;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    @PostMapping
    public ApiResult createChatting(@Valid @RequestBody ChattingRequest.CreateRequest request, @AuthenticationPrincipal JwtAuthentication authentication) {
        chattingService.createChatting(request.getProductId(), authentication.getId());
        return OK();
    }
}
