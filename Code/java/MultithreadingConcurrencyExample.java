import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultithreadingConcurrencyExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException  {
        // 1. Creating and starting threads
        basicThreadExample();

        // 2. Synchronization
        synchronizationExample();

        // 3. Lock Interface
        lockExample();

        // 4. Atomic Variables
        atomicVariableExample();

        // 5. Executor Framework
        executorExample();

        // 6. Future and Callable
        futureAndCallableExample();

        // 7. Thread Pools
        threadPoolExample();

        // 8. Concurrent Collections
        concurrentCollectionsExample();
    }

    // 1. Basic Thread Creation and Execution
    private static void basicThreadExample() {
        // Using Runnable Interface
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread 1 is running!");
            }
        });

        // Using Lambda expression
        Thread thread2 = new Thread(() -> System.out.println("Thread 2 is running!"));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 2. Synchronization

    private static void synchronizationExample() {
        Counter counter = new Counter();
        
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("The Counter value is: " + counter.getCount());
        
    }

    static class Counter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public int getCount() {
        return count;
        }
    }

    // 3. Lock Interface
    private static void lockExample() {
        LockCounter lockCounter = new LockCounter();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                lockCounter.increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Lock Counter value: " + lockCounter.getCount());
    }

    static class LockCounter {
        private int counter = 0;
        private Lock lock = new ReentrantLock();

        public void increment() {
            lock.lock();
            try {
                counter++;
            } finally {
                lock.unlock();
            }
        }

        public int getCount() {
            return counter;
        }
    }

    // 4. Atomic Variables
    private static void atomicVariableExample() {
        AtomicCounter atomicCounter = new AtomicCounter();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                atomicCounter.increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Atomic Counter value: " + atomicCounter.getCount());
    }

    static class AtomicCounter {
        AtomicInteger counter = new AtomicInteger(0);

        public void increment() {
            counter.incrementAndGet();
        }

        public int getCount() {
            return counter.get();
        }
    }

    // 5. Executor Framework
    private static void executorExample() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.execute(() -> System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName()));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }        
    }

    // 6. Future and Callable
    private static void futureAndCallableExample() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> task = () -> {
            TimeUnit.SECONDS.sleep(2);
            return 42;
        };

        Future<Integer> future = executor.submit(task);

        System.out.println("Future - isDone: " + future.isDone());

        Integer output = future.get(); // This will block until the result is available

        System.out.println("Future result: " + output);
        System.out.println(" Future - isDone: " + future.isDone());

        executor.shutdown();
    }

    // 7. Thread Pools
    private static void threadPoolExample() {
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            fixedPool.execute(() -> System.out.println("Fixed Pool task: " + taskId + " executed by " + Thread.currentThread().getName()));
        }

        scheduledPool.schedule(() -> System.out.println("Scheduled task executed"), 2, TimeUnit.SECONDS);

        fixedPool.shutdown();
        cachedPool.shutdown();
        scheduledPool.shutdown();
    }

    // 8. Concurrent Collections

    private static void concurrentCollectionsExample() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        map.put("Satvik", 1);
        map.put("Jadhav", 2);

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Satvik");
        list.add("Jadhav");

        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        queue.offer("Satvik");
        queue.offer("Jadhav");

        System.out.println("ConcurrentHashMap: " + map);
        System.out.println("CopyOnWriteArrayList: " + list);
        System.out.println("BlockingQueue: " + queue);
    }
}