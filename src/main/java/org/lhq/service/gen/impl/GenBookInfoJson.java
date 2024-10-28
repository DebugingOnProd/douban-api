package org.lhq.service.gen.impl;

import org.lhq.entity.book.BookInfo;
import org.lhq.service.gen.Gen;
import org.lhq.service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class GenBookInfoJson implements Gen<BookInfo> {
    private static final Logger log = LoggerFactory.getLogger(GenBookInfoJson.class);

    @Override
    public void genFile(BookInfo bookInfo, File taskFile) {
        log.info("开始生成json文件");
        File parentFile = taskFile.getParentFile();
        String path = parentFile.getPath();
        String filePath = path + File.separator + "metadata.json";
        try {
            String jsonStr = Optional.ofNullable(JsonUtils.toJson(bookInfo)).orElse("");
            Files.write(Paths.get(filePath), jsonStr.getBytes());
            log.info("write json success {}", filePath);
        } catch (IOException e) {
            log.error("write json error", e);
        }
    }
}
