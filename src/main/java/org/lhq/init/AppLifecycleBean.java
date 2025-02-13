package org.lhq.init;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.lhq.config.DirConfigProperties;
import org.lhq.service.task.FileListening;
import org.lhq.service.task.impl.BookFileListeningTask;
import org.lhq.service.task.impl.CategorizedBookListening;
import org.lhq.service.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppLifecycleBean.class);

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


    private final DirConfigProperties dirConfigProperties;


    private final BeanUtils beanUtils;

    public AppLifecycleBean(
            DirConfigProperties dirConfigProperties,
            BeanUtils beanUtils) {
        this.dirConfigProperties = dirConfigProperties;
        this.beanUtils = beanUtils;
    }

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        // 创建一个ScheduledExecutorService实例
        Integer interval = dirConfigProperties.autoScanInterval();
        if (interval == null || interval <= 0) {
            interval = 30;
        }
        if (dirConfigProperties.autoScanEnabled()) {
            LOGGER.info("The auto scan interval is {} second", interval);
            // 创建一个Runnable任务
            FileListening fileListeningTask = new BookFileListeningTask(beanUtils, dirConfigProperties);
            FileListening categorizedBookListeningTask = new CategorizedBookListening(beanUtils, dirConfigProperties);
            // 安排定时任务
            // 第一个参数是Runnable任务，第二个参数是首次执行的时间（延迟时间），第三个参数是周期时间，第四个参数是时间单位
            executor.scheduleAtFixedRate(fileListeningTask, 5, interval, TimeUnit.SECONDS);
            //这个任务定时扫描已经分类的书籍文件夹汇总成一个文件
            executor.scheduleAtFixedRate(categorizedBookListeningTask, 10, 10, TimeUnit.SECONDS);
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
        // 如果需要关闭定时任务，可以调用shutdown方法
        executor.shutdown();
        LOGGER.info("The executor is stopped.");
    }

}