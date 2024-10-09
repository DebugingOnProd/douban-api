package org.lhq.service.task.impl;

import org.lhq.entity.BookInfo;
import org.lhq.service.task.Gen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class GenXml implements Gen<BookInfo> {
    private static final Logger log = LoggerFactory.getLogger(GenXml.class);

    @Override
    public void genFile(BookInfo bookInfo, File taskFile) {
        log.info("gen xml file");
    }
}
