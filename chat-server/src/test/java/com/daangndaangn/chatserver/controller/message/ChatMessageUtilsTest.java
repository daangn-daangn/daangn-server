package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.util.UploadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChatMessageUtilsTest {

    @InjectMocks
    private ChatMessageUtils chatMessageUtils;

    @Mock
    private PresignerUtils presignerUtils;

    @Mock
    private UploadUtils uploadUtils;

    @Test
    void 유효하지_않은_이미지_형식이_들어오는_경우_예외를_반환한다() {
        //given
        List<String> inValidMockImages = List.of("test1", "test2", "test3.abc", "test4", "test5");
        given(uploadUtils.isNotImageFile(anyString())).willReturn(true);
        //when
        assertThrows(IllegalArgumentException.class, () -> chatMessageUtils.toImageUploadResponses(inValidMockImages));

        //then
        verify(uploadUtils).isNotImageFile(anyString());
        verify(presignerUtils, never()).getChatRoomPresignedPutUrl(anyString());
    }

    @Test
    void 유효한_이미지_형식이_들어오는_경우_ImageUploadResponse를_반환한다() {
        //given
        List<String> mockImages = List.of("test1.jpg", "test2.jpg", "test3.jpg", "test4.jpg", "test5.jpg");
        String mockPresignedPutUrl = "testPresignedUrl";

        given(uploadUtils.isNotImageFile(anyString())).willReturn(false);
        given(presignerUtils.getChatRoomPresignedPutUrl(anyString())).willReturn(mockPresignedPutUrl);

        //when
        List<ChatMessageResponse.ImageUploadResponse> result = chatMessageUtils.toImageUploadResponses(mockImages);

        //then
        assertThat(result.size()).isEqualTo(mockImages.size());
        result.stream().forEach(value -> {
            assertThat(value).isNotNull();
            assertThat(value.getPresignedUrl()).isEqualTo(mockPresignedPutUrl);
        });

        verify(uploadUtils, times(mockImages.size())).isNotImageFile(anyString());
        verify(presignerUtils, times(mockImages.size())).getChatRoomPresignedPutUrl(anyString());
    }
}