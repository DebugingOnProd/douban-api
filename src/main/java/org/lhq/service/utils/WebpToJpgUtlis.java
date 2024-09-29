package org.lhq.service.utils;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class WebpToJpgUtlis {


    private static final Logger log = LoggerFactory.getLogger(WebpToJpgUtlis.class);

    public static byte[] loadWebpData(String inputPath) {
        try {
            return ByteStreams.toByteArray(new FileInputStream(inputPath));
        } catch (IOException e) {
            log.error("load webp data error", e);
            return new byte[] {};
        }
    }

}
