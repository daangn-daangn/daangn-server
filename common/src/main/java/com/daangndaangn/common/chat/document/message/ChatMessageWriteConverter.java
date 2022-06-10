package com.daangndaangn.common.chat.document.message;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class ChatMessageWriteConverter implements Converter<MessageType, Integer> {
    @Override
    public Integer convert(MessageType messageType) {
        return messageType.getCode();
    }
}
