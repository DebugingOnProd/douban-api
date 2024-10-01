package org.lhq.init;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.lhq.config.DirConfigProperties;
import org.lhq.service.task.FileListening;
import org.lhq.service.task.impl.FileListeningTask;
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

        // 创建一个Runnable任务
        FileListening fileListeningTask = new FileListeningTask(beanUtils,dirConfigProperties);
        // 安排定时任务
        // 第一个参数是Runnable任务，第二个参数是首次执行的时间（延迟时间），第三个参数是周期时间，第四个参数是时间单位
        executor.schedule(fileListeningTask, 5, TimeUnit.SECONDS);



    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
        // 如果需要关闭定时任务，可以调用shutdown方法
        executor.shutdown();
        LOGGER.info("The executor is stopped.");
    }

}