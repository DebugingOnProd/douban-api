package org.lhq.service.task.impl;

import org.lhq.config.DirConfigProperties;
import org.lhq.entity.book.BookInfo;
import org.lhq.entity.book.BookVo;
import org.lhq.service.task.FileListening;
import org.lhq.service.utils.BeanUtils;
import org.lhq.service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategorizedBookListening extends FileListening {
    private static final Logger log = LoggerFactory.getLogger(CategorizedBookListening.class);

    public CategorizedBookListening(BeanUtils beanUtils,
                                       DirConfigProperties dirConfigProperties) {
        super(beanUtils, dirConfigProperties);
    }

    @Override
    public void process(Map<String, File> fileNameMap, BeanUtils beanUtils, DirConfigProperties dirConfigProperties) {
        String bookDir = dirConfigProperties.bookDir();
        log.info("CategorizedBookListening process");
        fileNameMap.forEach((name, value) -> log.info("fileName:{}-------filePath:{}", name, value));
        List<BookVo> bookletList = new ArrayList<>();
        for (Map.Entry<String, File> singleFileEntry : fileNameMap.entrySet()) {
            File file = singleFileEntry.getValue();
            String parent = file.getParent();
            String metadataJsonPath = parent + File.separator + "metadata.json";
            log.info("metadataJsonPath:{}", metadataJsonPath);
            try (FileReader fileReader = new FileReader(metadataJsonPath)){
                BookInfo bookInfo = JsonUtils.fromFileReader(fileReader, BookInfo.class);
                bookInfo = Optional.ofNullable(bookInfo).orElse(new BookInfo());
                BookVo bookVo = bookInfo.toBookVo();
                bookVo.setPath(metadataJsonPath);
                bookletList.add(bookVo);
            } catch (IOException e) {
                log.warn("fileReader error", e);
            }
        }
        try {
            log.info("bookletList: {}", bookletList);
            String jsonStr = JsonUtils.toJson(bookletList);
            jsonStr = Optional.ofNullable(jsonStr).orElse("");
            String filePath = bookDir + File.separator + "bookIndex.json";
            Files.write(Paths.get(filePath), jsonStr.getBytes());
        } catch (IOException e) {
            log.warn("write bookIndex.json error", e);
        }
    }

    @Override
    protected boolean isNeedFileExt(File file) {
        List<String> ebookExtensions = getDirConfiguration().ebookExtensions();
        for (String extension : ebookExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getScanDirs() {
        return getDirConfiguration().bookDir();
    }
}
