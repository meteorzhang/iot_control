package app.iot.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 *
 * @author liuhulu
 */
public class AsyncManager {
    private static AsyncManager me = new AsyncManager();
    /**
     * 操作延迟毫秒
     */
    private final int OPERATE_DELAY_TIME = 0;
    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(200);

    /**
     * 单例模式
     */
    private AsyncManager() {
    }

    public static AsyncManager me() {
        return me;
    }

    /**
     * 执行任务
     *
     * @param runnable 任务
     */
    public void execute(Runnable runnable) {
        executor.schedule(runnable, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 延迟执行任务
     *
     * @param runnable 任务
     * @param delay    延迟时间(毫秒)
     */
    public void execute(Runnable runnable, long delay) {
        executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        Threads.shutdownAndAwaitTermination(executor);
    }
}
