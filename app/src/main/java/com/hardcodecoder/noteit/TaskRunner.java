package com.hardcodecoder.noteit;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRunner {

    private static final ThreadPoolExecutor CUSTOM_THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(2, 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void executeAsync(Runnable runnable) {
        CUSTOM_THREAD_POOL_EXECUTOR.execute(runnable);
    }

    public static <V> void executeAsync(Callable<V> callable) {
        CUSTOM_THREAD_POOL_EXECUTOR.execute(() -> {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static <V> void executeAsync(Callable<V> callable, Callback<V> callback) {
        CUSTOM_THREAD_POOL_EXECUTOR.execute(() -> {
            try {
                final V result = callable.call();
                handler.post(() -> callback.onComplete(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public interface Callback<V> {
        void onComplete(V result);
    }
}
