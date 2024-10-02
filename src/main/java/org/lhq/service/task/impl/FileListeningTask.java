package org.lhq.service.task.impl;

import org.lhq.config.DirConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.task.FileListening;
import org.lhq.service.task.FileProcess;
import org.lhq.service.utils.BeanUtils;
import org.lhq.service.utils.ThreadPoolType;
import org.lhq.service.utils.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FileListeningTask extends FileListening {

    private static final Logger log = LoggerFactory.getLogger(FileListeningTask.class);

    public FileListeningTask(BeanUtils beanUtils,
                                DirConfigProperties dirConfigProperties) {
        super(beanUtils, dirConfigProperties);
    }


    @Override
    public void process(Map<String, File> filaNameMap,
                        BeanUtils beanUtils,
                        DirConfigProperties dirConfigProperties) {
        EntityLoader<List<Byte>> imageLoader = beanUtils.getImageLoader();
        HtmlParseProvider<BookInfo> htmlToBookParser = beanUtils.getHtmlToBookParser();
        SearchLoader<BookInfo> searchLoader = beanUtils.getSearchLoader();
        // join the thread pool
        filaNameMap.forEach((fileNameStr,fileValue) -> {
            log.info("fileName:{}",fileNameStr);
            FileProcess<BookInfo> fileProcess = new FileProcessTask(
                    fileNameStr,
                    fileValue,
                    htmlToBookParser,
                    imageLoader,
                    searchLoader,
                    dirConfigProperties);
            ThreadPoolUtil.execute(
                    ThreadPoolType.FIXED_THREAD,
                    fileProcess );
        });
    }

}
