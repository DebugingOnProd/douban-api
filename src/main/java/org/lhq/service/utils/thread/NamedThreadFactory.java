package org.lhq.service.utils.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory  implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public NamedThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(Runnable task) {
        Thread thread = new Thread(task, namePrefix + "-" + threadNumber.getAndIncrement());
        thread.setPriority(Thread.NORM_PRIORITY);
        return  thread;
    }
}
