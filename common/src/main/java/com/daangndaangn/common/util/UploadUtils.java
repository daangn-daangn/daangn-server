package com.daangndaangn.common.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class UploadUtils {

    private static final String[] CONTENT_TYPES = {"png", "jpeg", "jpg"};

    public boolean isImageFile(String filename) {
        if (isEmpty(filename)) {
            return false;
        }

        String extension = FilenameUtils.getExtension(filename.toLowerCase());
//        return Arrays.stream(CONTENT_TYPES).anyMatch(t -> t.equals(extension));
        return Arrays.asList(CONTENT_TYPES).contains(extension);
    }

    public boolean isNotImageFile(String filename) {
        return !isImageFile(filename);
    }
}
