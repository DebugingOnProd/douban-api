package org.lhq.service.utils.thread;

import java.util.concurrent.*;

public enum ThreadPoolType {


    FILE_RW_THREAD(1,
            3,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            new NamedThreadFactory("file-scan-thread")),

    NETWORK_REQUEST_THREAD(2,
            5,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            new NamedThreadFactory("network-request-thread"));


    private final int corePoolSize;
    private final int maximumPoolSize;
    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final BlockingQueue<Runnable> workQueue;
    private final ThreadFactory threadFactory;

    ThreadPoolType(int corePoolSize,
                   int maximumPoolSize,
                   long keepAliveTime,
                   TimeUnit timeUnit,
                   BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
    }

    public ThreadPoolExecutor createThreadPool() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workQueue,
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
