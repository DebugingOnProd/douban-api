package org.lhq.service.task.impl;

import org.lhq.config.DirConfigProperties;
import org.lhq.entity.book.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.task.FileListening;
import org.lhq.service.task.FileProcess;
import org.lhq.service.utils.BeanUtils;
import org.lhq.service.utils.thread.ThreadPoolType;
import org.lhq.service.utils.thread.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BookFileListeningTask extends FileListening {

    private static final Logger log = LoggerFactory.getLogger(BookFileListeningTask.class);

    public BookFileListeningTask(BeanUtils beanUtils,
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
                    ThreadPoolType.FILE_RW_THREAD,
                    fileProcess );
        });
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
        return getDirConfiguration().autoScanDir();
    }


}
