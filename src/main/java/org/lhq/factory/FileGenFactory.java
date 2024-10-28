package org.lhq.factory;

import org.lhq.entity.book.BookInfo;
import org.lhq.service.gen.Gen;
import org.lhq.service.gen.impl.GenBookInfoJson;
import org.lhq.service.gen.impl.GenBookInfoXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileGenFactory {


    private FileGenFactory(){
        throw new AssertionError("工具类不能实例化");
    }


    private static final Logger log = LoggerFactory.getLogger(FileGenFactory.class);

    public static Gen<BookInfo> getFileGen(String fileType) {
        return switch (fileType.toLowerCase()) {
            case "json" -> new GenBookInfoJson();
            case "xml" -> new GenBookInfoXml();
            default -> (dataItem, taskFile) -> log.warn("请实现文件写入生成器:{}", dataItem);
        };
    }
}
