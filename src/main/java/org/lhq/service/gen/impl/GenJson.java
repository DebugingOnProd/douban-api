package org.lhq.service.gen.impl;

import org.lhq.entity.book.BookInfo;
import org.lhq.service.gen.Gen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class GenJson implements Gen<BookInfo> {
    private static final Logger log = LoggerFactory.getLogger(GenJson.class);

    @Override
    public void genFile(BookInfo bookInfo, File taskFile) {
        log.info("开始生成json文件");
    }
}
