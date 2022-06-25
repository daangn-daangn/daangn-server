package com.daangndaangn.common.event;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventExceptionHandler implements SubscriberExceptionHandler {

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        log.error("Unexpected exception occurred [{} > {} with {}]: {}",
            context.getSubscriber(), context.getSubscriberMethod(), context.getEvent(), exception.getMessage(), exception);
    }
}
