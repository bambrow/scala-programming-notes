package concurrent;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * Java Executor test.
 */

class SerialSampleExecutor implements Executor {
    final Queue<Runnable> tasks = new ArrayDeque<>();
    final Executor executor;
    Runnable active;

    public SerialSampleExecutor(Executor executor) {
        this.executor = executor;
    }

    public synchronized void execute(final Runnable r) {
        tasks.offer(() -> {
            try {
                r.run();
            } finally {
                scheduleNext();
            }
        });
        if (null == active) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if (null != (active = tasks.poll())) {
            executor.execute(active);
        }
    }
}

class MyThread3 implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + " is running.");
    }
}

public class ExecutorTestJava {

    public static void main(String[] args) {
        Executor executor = command -> new Thread(command).start();

        SerialSampleExecutor serialExecutor = new SerialSampleExecutor(executor);

        for (int i = 0; i < 10; i++) {
            serialExecutor.execute(new MyThread3());
        }
    }

}
