package com.ib.imagebord_test.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class serviceQueue {
    private final LinkedBlockingQueue<Callable<?>> taskQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void init() {
        Thread qthread = new Thread(() -> {
            while (running) {
                try {
                    Callable<?> task = taskQueue.take();
                    executorService.submit(task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        qthread.start();
    }

    public <T> CompletableFuture<T> addTask(Callable<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        taskQueue.offer(() -> {
            try {
                future.complete(task.call());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
            return null;
        });
        return future;
    }

    @PreDestroy
    public void shutdown(){
        running=false;
        executorService.shutdown();
    }
}
