package org.lhq.init;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.lhq.config.DirConfigProperties;
import org.lhq.entity.BookInfo;
import org.lhq.service.loader.EntityLoader;
import org.lhq.service.loader.SearchLoader;
import org.lhq.service.perse.HtmlParseProvider;
import org.lhq.service.task.impl.FileListeningTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final HtmlParseProvider<BookInfo> htmlParseProvider;

    private final SearchLoader<BookInfo> searchLoader;

    private final DirConfigProperties dirConfigProperties;

    private final EntityLoader<List<Byte>> imageLoader;

    public AppLifecycleBean(HtmlParseProvider<BookInfo> htmlParseProvider,
                            SearchLoader<BookInfo> searchLoader, DirConfigProperties dirConfigProperties, EntityLoader<List<Byte>> imageLoader) {
        this.htmlParseProvider = htmlParseProvider;
        this.searchLoader = searchLoader;
        this.dirConfigProperties = dirConfigProperties;
        this.imageLoader = imageLoader;
    }

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        // 创建一个ScheduledExecutorService实例

        // 创建一个Runnable任务
        //ScanTaskImpl scanTask = new ScanTaskImpl(searchLoader, htmlParseProvider, dirConfigProperties, imageLoader);
        FileListeningTask fileListeningTask = new FileListeningTask(dirConfigProperties);
        // 安排定时任务
        // 第一个参数是Runnable任务，第二个参数是首次执行的时间（延迟时间），第三个参数是周期时间，第四个参数是时间单位
        executor.schedule(fileListeningTask, 1, TimeUnit.SECONDS);



    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
        // 如果需要关闭定时任务，可以调用shutdown方法
        executor.shutdown();
        LOGGER.info("The executor is stopped.");
    }

}