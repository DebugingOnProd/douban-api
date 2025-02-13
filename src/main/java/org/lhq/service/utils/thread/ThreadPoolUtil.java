package org.lhq.service.utils.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class ThreadPoolUtil {
    private static final ConcurrentHashMap<ThreadPoolType, ThreadPoolExecutor> executorMap = new ConcurrentHashMap<>();



    private ThreadPoolUtil () {}


    /**
     * 获取线程池实例
     * @param type 线程池类型
     * @return 线程池
     */
    public static ThreadPoolExecutor getExecutor(ThreadPoolType type) {
        return executorMap.computeIfAbsent(type, ThreadPoolType::createThreadPool);
    }

    /**
     * 提交任务到线程池
     * @param type 线程池类型
     * @param task 要执行的任务
     */
    public static void execute(ThreadPoolType type, Runnable task) {
        ThreadPoolExecutor pool = getExecutor(type);
        pool.execute(task);
    }



    public static <T> Future<T> submit(ThreadPoolType type, Callable<T> task){
        ThreadPoolExecutor pool = getExecutor(type);
        return pool.submit(task);
    }

    /**
     * 关闭线程池
     * @param type 线程池类型
     */
    public static void shutdown(ThreadPoolType type) {
        ThreadPoolExecutor pool = getExecutor(type);
        if (pool != null) {
            pool.shutdown();
        }
    }
    /**
     * 立即关闭线程池
     * @param type 线程池类型
     */
    public static void shutdownNow(ThreadPoolType type) {
        ThreadPoolExecutor pool = getExecutor(type);
        if (pool != null) {
            pool.shutdownNow();
        }
    }
}
