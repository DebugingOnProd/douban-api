package org.lhq.service.utils.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory  implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public NamedThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    /**
     * Constructs a new unstarted {@code Thread} to run the given runnable.
     *
     * @param task a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     * @see <a href="../../lang/Thread.html#inheritance">Inheritance when
     * creating threads</a>
     */
    @Override
    public Thread newThread(Runnable task) {
        Thread thread = new Thread(task, namePrefix + "-" + threadNumber.getAndIncrement());
        thread.setPriority(Thread.NORM_PRIORITY);
        return  thread;
    }
}
