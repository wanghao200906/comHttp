package http.wh.comhttp.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DefaultThreadPool {
    static ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
    private static DefaultThreadPool instance = null;
    public static synchronized DefaultThreadPool getInstance() {
        if (DefaultThreadPool.instance == null) {
            DefaultThreadPool.instance = new DefaultThreadPool();
        }
        return DefaultThreadPool.instance;
    }
    /**
     * 关闭，并等待任务执行完成，不接受新任务
     */
    public static void shutdown() {
        if (newCachedThreadPool != null) {
            newCachedThreadPool.shutdown();
        }
    }
    /**
     * 关闭，立即关闭，并挂起所有正在执行的线程，不接受新任务
     */
    public static void shutdownRightnow() {
        if (newCachedThreadPool != null) {
            newCachedThreadPool.shutdownNow();
            try {
                // 设置超时极短，强制关闭所有任务
                newCachedThreadPool.awaitTermination(1,
                        TimeUnit.MICROSECONDS);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 执行任务
     *
     * @param r
     */
    public void execute(final Runnable r) {
        if (r != null) {
            try {
                LogUtils.e("执行任务");
                newCachedThreadPool.execute(r);
            } catch (final Exception e) {
                LogUtils.e("异常了" + e.toString());
                e.printStackTrace();
            }
        } else {
            LogUtils.e("r null");
        }
    }
}
