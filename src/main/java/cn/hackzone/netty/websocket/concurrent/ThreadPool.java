package cn.hackzone.netty.websocket.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 线程池
 *
 * @author maxwell
 * @date 2023/03/10
 */
@Slf4j
public class ThreadPool {

    private final BlockingQueue<Runnable> workQueue;
    private final Set<Worker> workers;

    public ThreadPool(int numThreads, int queueCapacity) {
        workQueue = new ArrayBlockingQueue<>(queueCapacity);
        workers = new HashSet<>();

        for (int i = 0; i < numThreads; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = worker.thread;
            thread.start();
        }
    }

    public void submit(Runnable task) {
        try {
            workQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private final class Worker implements Runnable {
        boolean running;
        final Thread thread;

        public Worker() {
            this.running = true;
            this.thread = new Thread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }

        public void stop() {
            this.running = false;
            Thread t;
            if ((t = thread) != null && !t.isInterrupted()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Worker worker = (Worker) o;
            return running == worker.running && Objects.equals(thread, worker.thread);
        }

        @Override
        public int hashCode() {
            return Objects.hash(running, thread);
        }
    }

    final void runWorker(Worker w) {
        while (w.running && !Thread.currentThread().isInterrupted()) {
            try {
                log.info("taskQueue size {}", workQueue.size());
                Runnable task = workQueue.take();
                task.run();
            } catch (InterruptedException e) {
                w.running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

}
