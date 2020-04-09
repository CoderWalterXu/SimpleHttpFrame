package com.xlh.study.httplibrary.task;

import android.util.Log;

import com.xlh.study.httplibrary.helper.Constants;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.xlh.study.httplibrary.helper.Constants.TAG;

/**
 * @author: Watler Xu
 * time:2020/4/8
 * description:线程池管理类
 * 构造方法里新建线程池，并维护一个监听任务
 * 网络请求队列：LinkedBlockingDeque
 * version:0.0.1
 */
public class ThreadPoolManager {

    private ThreadPoolExecutor mThreadPoolExecutor;

    // 静态实例，volatile防止JVM优化指令排序
    private volatile static ThreadPoolManager threadPoolManger;

    // 网络请求队列
    private LinkedBlockingDeque<Runnable> mDeque = new LinkedBlockingDeque<>();

    // 失败重试队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    public static ThreadPoolManager getInstance() {

        if (threadPoolManger == null) {
            synchronized (ThreadPoolManager.class) {
                if (threadPoolManger == null) {
                    threadPoolManger = new ThreadPoolManager();
                }
            }
        }
        return threadPoolManger;
    }

    private ThreadPoolManager() {
        // 仿OkHttp线程池的创建
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 6,
                15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("自定义的线程....");
                        // 不是守护线程
                        thread.setDaemon(false);
                        return thread;
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                        addTask(runnable);
                    }
                });
        // 核心线程交给线程池维护
        mThreadPoolExecutor.execute(coreThread);
        mThreadPoolExecutor.execute(delayThread);

    }

    /**
     * 将网络请求任务， 添加到队列中
     *
     * @param runnable
     */
    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mDeque.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将失败的网络请求添加到失败重试队列中
     * @param httpTask
     */
    public void addDelayTask(HttpTask httpTask) {
        if (httpTask != null) {
            // 设置重试时间
            Log.e(TAG, "延迟重试时间: "+httpTask.getRetryDelayTime());
            httpTask.setDelayTime(httpTask.getRetryDelayTime());
            mDelayQueue.offer(httpTask);
        }
    }

    /**
     * 监听网络请求队列的线程
     */
    public Runnable coreThread = new Runnable() {

        Runnable runnable = null;

        @Override
        public void run() {
            // 死循环监听网络请求队列中有没有请求
            while (true) {
                // 取出请求
                try {
                    runnable = mDeque.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 交给线程池处理
                mThreadPoolExecutor.execute(runnable);

            }

        }
    };

    /**
     * 监听失败重试队列的线程
     */
    private Runnable delayThread = new Runnable() {

        HttpTask httpTask = null;

        @Override
        public void run() {
            // 死循环监听失败重试队列中有没有请求
            while (true) {
                try {
                    httpTask = mDelayQueue.take();

                    if (httpTask.getCurRetryCount() < httpTask.getMaxRetryCount()) {
                        // 判断是否需要重试
                        if (!httpTask.isRepeat()) {
                            Log.e(TAG, "=== 重试机制 === 当前请求不需要重试" );
                            return;
                        }

                        // 交给线程池处理
                        mThreadPoolExecutor.execute(httpTask);
                        httpTask.setCurRetryCount(httpTask.getCurRetryCount() + 1);
                        Log.e(TAG, "=== 重试机制 === 当前重试：" + httpTask.getCurRetryCount() + "  " + System.currentTimeMillis());

                    } else {
//                        httpTask.getHttpCallbackListener().onFailure(new RuntimeException("已到达重试最大次数，请求失败"));
                        Log.e(TAG, "=== 重试机制 === 到达重试最大次数，不再重试");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

}
