package me.peterfei.threaddemo.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @program: thread-demo
 * @description:
 * @author: peterfei
 * @create: 2022-09-27 09:21
 **/
public class TaskProcessUtil {
    //每个任务都有自己独立线程
    private static Map<String, ExecutorService> executors = new ConcurrentHashMap<>();

    //初始化线程池
    private static ExecutorService init(String poolName,int poolSize){
        return new ThreadPoolExecutor(poolSize,poolSize,0L, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryBuilder().setNameFormat("Pool-"+ poolName).setDaemon(false).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
                );
    }

    //获取线程池
    public static ExecutorService getOrInitExecutors(String poolName,int poolSize){
        ExecutorService executorService = executors.get(poolName);
        if (executorService == null) {
            synchronized (TaskProcessUtil.class){
                executorService = executors.get(poolName);
                if (executorService == null) {
                    executorService = init(poolName,poolSize);
                    executors.put(poolName,executorService);
                }
            }
        }
        return executorService;
    }

    //回收线程池
    public static void releaseExecutors(String poolName){
        ExecutorService executorService = executors.remove(poolName);
        if (executorService == null) {
            executorService.shutdown();
        }
    }
}
