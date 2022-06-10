package com.daangndaangn.common.chat.document.message;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class ChatMessageReadConverter implements Converter<Integer, MessageType> {
    @Override
    public MessageType convert(Integer messageTypeCode) {
        return MessageType.from(messageTypeCode);
    }
}
