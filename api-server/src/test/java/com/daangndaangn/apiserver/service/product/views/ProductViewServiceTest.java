package com.daangndaangn.apiserver.service.product.views;

import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ProductViewServiceTest {

    @SpyBean
    private ProductViewService productViewService;

    @Test
    public void 십초마다_스케줄링이_수행된다() {
        await()
            .atMost(new Duration(20, SECONDS))
            .untilAsserted(() -> verify(productViewService,atLeast(3)).updateProductViewCount());
    }
}