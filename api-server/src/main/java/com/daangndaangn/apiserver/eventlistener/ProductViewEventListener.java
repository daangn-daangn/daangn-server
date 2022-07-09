package com.daangndaangn.apiserver.eventlistener;

import com.daangndaangn.apiserver.service.product.views.ProductViewService;
import com.daangndaangn.common.event.ProductViewEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ProductViewEventListener implements AutoCloseable {

    private final EventBus eventBus;

    private final ProductViewService productViewService;

    public ProductViewEventListener(EventBus eventBus, ProductViewService productViewService) {
        this.eventBus = eventBus;
        this.productViewService = productViewService;

        eventBus.register(this);
    }

    @Subscribe
    public void handleProductViewingEvent(ProductViewEvent event) {
        Long productId = event.getProductId();
        productViewService.addProductViewCount(productId);
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
