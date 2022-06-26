package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.chatserver.controller.message.ChatMessageResponse.ImageUploadResponse;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.util.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class ChatMessageUtils {

    private final PresignerUtils presignerUtils;
    private final UploadUtils uploadUtils;

    public List<ImageUploadResponse> toImageUploadResponses(List<String> imgFiles) {
        checkArgument(imgFiles != null, "imgFiles값은 필수입니다.");

        boolean isNotValid = imgFiles.stream().anyMatch(uploadUtils::isNotImageFile);

        if (isNotValid) {
            throw new IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");
        }

        return imgFiles.stream().map(imgFileName -> {
            String newImageName = createRandomImageName(imgFileName);
            String presignedUrl = presignerUtils.getChatRoomPresignedPutUrl(newImageName);

            return ImageUploadResponse.builder()
                                        .originName(imgFileName)
                                        .imageName(newImageName)
                                        .presignedUrl(presignedUrl)
                                        .build();
        }).collect(toList());
    }

    private String createRandomImageName(String imgFileName) {
        return UUID.randomUUID() + imgFileName;
    }
}
