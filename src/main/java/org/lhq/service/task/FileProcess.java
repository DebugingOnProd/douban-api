package org.lhq.service.task;

import org.lhq.config.DirConfigProperties;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;

import java.io.File;
import java.util.List;

public abstract class FileProcess<T> implements Runnable {
    private final String fileName;

    private final File taskFile;

    protected final HtmlParseProvider<T> htmlParseProvider;

    protected final EntityLoader<List<Byte>> imageLoader;

    protected final SearchLoader<T> searchLoader;

    protected final DirConfigProperties dirConfigProperties;

    protected FileProcess(String fileName,
                          File taskFile,
                          HtmlParseProvider<T> htmlParseProvider,
                          EntityLoader<List<Byte>> imageLoader,
                          SearchLoader<T> searchLoader,
                          DirConfigProperties dirConfigProperties) {
        this.fileName = fileName;
        this.taskFile = taskFile;
        this.htmlParseProvider = htmlParseProvider;
        this.imageLoader = imageLoader;
        this.searchLoader = searchLoader;
        this.dirConfigProperties = dirConfigProperties;
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        this.process(fileName,taskFile);
    }

    public abstract void process(String fieldName,File taskFile);
}
