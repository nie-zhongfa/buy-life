/*
 * Copyright (c) 2015-2020 BiliBili Inc.
 */

package org.buy.life.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class ThreadPoolManager {

    private static Logger log = LoggerFactory.getLogger(ThreadPoolManager.class);

    private static ExecutorService executor;
    private static ItTaskThreadFactory quafalTaskThreadFactory;
    private static Map<String, ExecutorService> POOL = new ConcurrentHashMap<String, ExecutorService>();

    public static String READ_KEY="read_pool";

    public static String WRITE_KEY="write_pool";

    static {
        quafalTaskThreadFactory = new ItTaskThreadFactory();
        int cores = Runtime.getRuntime().availableProcessors();
        cores = Math.max(cores, 1);
        executor = Executors.newFixedThreadPool(cores, quafalTaskThreadFactory);
        log.warn("Task Pool init finished, available core are {}", cores);
    }

    public static ExecutorService getExecutor(){
        return executor;
    }

    /**
     * 执行Function接口
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> async(Callable<T> callable) {
        CompletableFuture<T> cf = new CompletableFuture<>();

        executor.submit(() -> {
            try {
                cf.complete(callable.call());
            } catch (Exception ex) {
                cf.completeExceptionally(ex);
            }
        });

        return cf;
    }

    /**
     * 执行Runnable接口实现
     * @param task
     * @return
     */
    public static void async(Runnable task) {
        executor.submit(task);
    }

    static class ItTaskThreadFactory implements ThreadFactory {
        AtomicInteger cnt = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            int order = cnt.addAndGet(1);
            return new Thread(r, "quaFulTask-Worker-" + order);
        }
    }

    public static <T> void parallel(List<T> list, FutureFunction futureFunction){
        parallel(list,futureFunction,READ_KEY);
    }


    public static <T> void parallel(List<T> list, FutureFunction futureFunction,String threadPollKey){
        ExecutorService ensure = ensure(threadPollKey);
        CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(ensure);
        AtomicInteger atomicInteger=new AtomicInteger(0);
        for (T t:list){
            atomicInteger.incrementAndGet();
            completionService.submit(() -> {
                try {
                    futureFunction.apply(t);
                }catch (Exception e){
                    log.error(ThreadPoolManager.class+".parallel fail",e);
                }
                return Boolean.TRUE;
            });
        }
        for (int i = 0; i < atomicInteger.get(); i++){
            try {
                Future<Boolean> task = completionService.take();
                if (Objects.nonNull(task)) {
                    task.get();
                }
            }catch (Exception e){
                log.error(ThreadPoolManager.class+".parallel fail",e);
                continue;
            }
        }
    }


    public static <T> void asyncParallel(List<T> list, FutureFunction futureFunction){
        asyncParallel(list,futureFunction,WRITE_KEY);
    }

    /**
     * 异步执行
     * @param list
     * @param futureFunction
     * @param <T>
     */
    public static <T> void asyncParallel(List<T> list, FutureFunction futureFunction,String threadPollKey){
        ExecutorService ensure = ensure(threadPollKey);
        CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(ensure);
        for (T t:list){
            completionService.submit(() -> {
                try {
                    futureFunction.apply(t);
                }catch (Exception e){
                    log.error(ThreadPoolManager.class+".parallel fail",e);
                }
                return Boolean.TRUE;
            });
        }
    }


    /**
     * 取得对应的线程池，如果还没有在该k找到线程池，创建一个新的
     *
     * @param k 对应的主键
     * @return  线程池
     */
    private static ExecutorService ensure(String k) {
        log.debug("ensure k: " + k);


        if(!POOL.containsKey(k)) {

            int parallel = 4;

            log.debug("ensure parallel: " + parallel);

            // 此处同步操作
            // 为了防止意外的大流量同时进来，此处用sync操作进行规避，保证只有一个线程实例化ThreadPoolExecutor
            synchronized (ThreadPoolManager.class) {

                if(!POOL.containsKey(k)) {

                    Long DEFAULT_THRESHOLD = 500L;

                    ThreadPoolExecutor suitableExecutor = new ThreadPoolExecutor(parallel,
                        parallel,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        new ThreadPoolExecutor.CallerRunsPolicy());

                    POOL.put(k, suitableExecutor);

                }
            }
        }
        return POOL.get(k);
    }

    public static void main(String[] args) {
        ;
        parallel(Arrays.asList(1, 2, 3), new FutureFunction<Integer>() {
            @Override
            public void apply(Integer o) throws Exception {
                System.out.println(o);
            }
        } ,"a");


    }

}
