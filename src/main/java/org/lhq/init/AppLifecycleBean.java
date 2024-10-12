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
import org.lhq.service.utils.thread.ThreadPoolType;
import org.lhq.service.utils.thread.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
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
        FileListening fileListeningTask = new BookFileListeningTask(beanUtils,dirConfigProperties);
        FileListening categorizedBookListeningTask = new CategorizedBookListening(beanUtils, dirConfigProperties);
        // 安排定时任务
        // 第一个参数是Runnable任务，第二个参数是首次执行的时间（延迟时间），第三个参数是周期时间，第四个参数是时间单位
        //executor.schedule(fileListeningTask, 5, TimeUnit.SECONDS);
        executor.schedule(categorizedBookListeningTask, 10, TimeUnit.SECONDS);




      /*  executor.scheduleAtFixedRate(()->{
            LOGGER.info("The application is running...");
            ThreadPoolExecutor poolExecutor = ThreadPoolUtil.getExecutor(ThreadPoolType.FILE_RW_THREAD);
            LOGGER.info("The current pool size: {}", poolExecutor.getPoolSize());
            LOGGER.info("The current task queue size: {}", poolExecutor.getQueue().size());
            LOGGER.info("The current active threads: {}", poolExecutor.getActiveCount());
            LOGGER.info("The current completed tasks: {}", poolExecutor.getCompletedTaskCount());
            LOGGER.info("The current largest pool size: {}", poolExecutor.getLargestPoolSize());
            LOGGER.info("The current task count: {}", poolExecutor.getTaskCount());
            LOGGER.info("---------------------------");
        }, 0, 1, TimeUnit.SECONDS);*/



    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
        // 如果需要关闭定时任务，可以调用shutdown方法
        executor.shutdown();
        LOGGER.info("The executor is stopped.");
    }

}