package org.lhq.service.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public enum ThreadPoolType {


    FIXED_THREAD(2,
            5,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100));


    private final int corePoolSize;
    private final int maximumPoolSize;
    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final BlockingQueue<Runnable> workQueue;

    ThreadPoolType(int corePoolSize,
                   int maximumPoolSize,
                   long keepAliveTime,
                   TimeUnit timeUnit,
                   BlockingQueue<Runnable> workQueue) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.workQueue = workQueue;
    }

    public ThreadPoolExecutor createThreadPool() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workQueue,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
