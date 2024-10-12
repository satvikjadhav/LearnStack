# Java Multithreading and Concurrency 

## 1. **Thread Creation and Execution**

In Java, threads can be created by:
- Implementing the `Runnable` interface or
- Extending the `Thread` class.

**Runnable Interface vs Thread Class**: 
- When implementing the `Runnable` interface, you pass an instance of `Runnable` to a `Thread` object. This approach is more flexible because the class implementing `Runnable` can extend other classes. 
- Extending the `Thread` class directly ties your class to the thread's lifecycle, which can limit design flexibility (e.g., the class can't extend another class).
- **Lambda expressions** (introduced in Java 8) provide a concise syntax to define the `Runnable` instance without needing an anonymous inner class. It’s ideal for shorter tasks.

**start() vs run()**:
- The `start()` method creates a new thread and calls the `run()` method in that new thread. Calling `run()` directly executes the method in the same thread where it was called.
- Use `start()` when you want concurrent execution.

**join()**:
- The `join()` method allows one thread to wait for the completion of another. It’s useful for scenarios where a thread must complete before another can continue, ensuring proper synchronization.

**Common Use Cases**: 
- Performing background tasks (e.g., file I/O, network requests) without blocking the main thread.
- Allowing multiple tasks to execute concurrently (parallel processing).

**Code Example:**

```java
// 1. Basic Thread Creation and Execution
private static void basicThreadExample() {
    // Using Runnable interface
    Thread thread1 = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("Thread 1 is running");
        }
    });

    // Using lambda expression (Java 8+)
    Thread thread2 = new Thread(() -> System.out.println("Thread 2 is running"));

    thread1.start();
    thread2.start();

    try {
        thread1.join();
        thread2.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

- **Why Use Threads?** Threads allow for parallel execution of tasks, making programs more responsive and capable of handling multiple tasks at once.

## 2. **Synchronization**

The `synchronized` keyword is used to control access to a critical section, ensuring only one thread can execute a block of code at a time.

**Preventing Race Conditions**:
- Without synchronization, multiple threads can modify shared data simultaneously, leading to inconsistent states. Race conditions occur when two or more threads try to change the same data at the same time, causing unpredictable results.

**Types of Synchronization**:
- **Method-level synchronization**: Using the `synchronized` keyword in a method ensures that only one thread can execute the method at a time.
- **Block-level synchronization**: More granular control can be achieved by synchronizing only a block of code inside a method. This reduces contention, as only critical sections are locked.

**Intrinsic Locks (Monitor Locks)**:
- Every object in Java has an intrinsic lock, also known as a monitor. When a thread enters a synchronized method or block, it acquires this lock. Other threads trying to access synchronized code must wait until the lock is released.
- **Example**: `synchronized (this) { /* critical section */ }`

**Drawbacks**:
- **Performance Impact**: Locking shared resources can lead to contention, where multiple threads are waiting for the same resource, causing a bottleneck.
- **Deadlocks**: Occur when two or more threads are blocked forever, waiting for each other’s locks. Careful design is needed to avoid this.


**Code Example:**

```java
// 2. Synchronization
private static void synchronizationExample() {
    Counter counter = new Counter();

    Runnable task = () -> {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
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

    System.out.println("Counter value: " + counter.getCount());
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
```

- **Use Cases**: Ensures that shared resources like counters, logs, or files are accessed by only one thread at a time to avoid inconsistent states.

## 3. **Lock Interface**

`Lock` provides more sophisticated thread synchronization than `synchronized`, allowing you to acquire and release locks in a flexible manner.

**Why Lock Interface over synchronized**:
- The `Lock` interface offers more fine-grained control over locking, allowing you to:
- **Try-lock**: Try acquiring a lock without blocking, which is useful to avoid deadlocks.
- **Timed Lock**: Specify a timeout while trying to acquire a lock, after which the thread can proceed with an alternative action.
- **Interruptible Lock Acquisition**: A thread can be interrupted while waiting for a lock.

**ReentrantLock**:
- Allows a thread to acquire the same lock multiple times (re-entrant behavior). The thread must release the lock the same number of times it has acquired it.
- **Fair vs Non-fair locks**: Fair locks guarantee first-come-first-served execution, which may reduce throughput compared to non-fair locks (default in `ReentrantLock`).

**Best Practices**:
- Always release the lock in a `finally` block to ensure it’s released even if an exception occurs, preventing potential deadlocks.

**Code Example:**

```java
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
    private int count = 0;
    private Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}
```

- **When to Use?** Locks offer more control than `synchronized`, especially when dealing with multiple resources or requiring features like try-lock, interruptible locking, or time-limited lock acquisition.

## 4. **Atomic Variables**

Atomic variables like `AtomicInteger` provide thread-safe operations on single variables without using locks.

**CAS Operations (Compare-And-Swap)**:
- Atomic variables such as `AtomicInteger`, `AtomicBoolean`, and `AtomicReference` use hardware-level compare-and-swap (CAS) instructions, which are non-blocking and faster than synchronized methods.
- The CAS operation compares the current value with an expected value and, if they match, replaces the current value with a new one. If they don’t match, it retries the operation until it succeeds.

**Advantages**:
- Since CAS avoids blocking, it reduces thread contention and improves performance in low-contention scenarios (e.g., incrementing counters).

**Use Cases**:
- Simple atomic operations like incrementing counters (`incrementAndGet()`) without the overhead of synchronization.
- Managing state in concurrent environments, such as in-memory caching or implementing safe shared flags.

**Code Example:**

```java
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
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
```

- **Why Use?** Atomic variables avoid synchronization and improve performance in scenarios where thread contention is minimal but correctness is required.

## 5. **Executor Framework**

The Executor framework allows you to decouple task submission from task execution.

**Separation of Task Submission and Execution**:
- With traditional threading (`Thread` and `Runnable`), you manage threads directly, which can be cumbersome. The Executor framework decouples task submission from the management of threads.
- You submit tasks (e.g., `Runnable` or `Callable`) to an `ExecutorService`, which handles thread management internally.

**ExecutorService**:
- Provides a powerful mechanism to execute tasks, including features like shutting down the executor, submitting multiple tasks, and obtaining results from asynchronous tasks.
- The framework supports managing pools of threads, reducing the cost of thread creation and destruction.

**Common Implementations**:
- **Fixed Thread Pool**: A pool with a fixed number of threads. Once all threads are busy, new tasks wait in a queue until a thread becomes available.
- **Cached Thread Pool**: Dynamically creates new threads as needed and reuses previously constructed threads when available.
- **Scheduled Thread Pool**: Allows tasks to be scheduled with delays or periodic execution, useful for repeated tasks like polling or maintenance jobs.

**Code Example:**

```java
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
```

- **Benefits**: It simplifies thread management, especially in large-scale systems where task scheduling and resource management are crucial.

## 6. **Future and Callable**

`Future` and `Callable` provide a way to get results from tasks that run asynchronously.

**Callable vs Runnable**:
- Unlike `Runnable`, `Callable` can return a value and throw checked exceptions, making it more suitable for tasks that produce results or may fail.

**Future**:
- Represents the result of an asynchronous computation. It provides methods to check if the computation is complete (`isDone()`), wait for its completion (`get()`), and cancel the task if necessary.
- `Future.get()` blocks the calling thread until the task is completed, making it useful when waiting for a result at some point in the future.

**Real-World Use Cases**:
- Running background tasks that return results, such as web service calls or file downloads, while performing other tasks concurrently.

**Code Example:**

```java
// 6. Future and Callable
private static void futureAndCallableExample() throws InterruptedException, ExecutionException {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    Callable<Integer> task = () -> {
        TimeUnit.SECONDS.sleep(2);
        return 42;
    };

    Future<Integer> future = executor.submit(task);

    System.out.println("Future isDone: " + future.isDone());

    Integer result = future.get(); // This will block until the result is available

    System.out.println("Future result: " + result);
    System.out.println("Future isDone: " + future.isDone());

    executor.shutdown();
}
```

- **Use Case**: Ideal for tasks that take a long time to complete and whose results you want to retrieve later.

## 7. **Thread Pools**

Thread pools manage multiple threads to execute tasks concurrently, reducing the overhead of thread creation.

**Why Use Thread Pools**:
- Thread pools allow you to reuse a limited number of threads to execute a large number of tasks, reducing the overhead of creating and destroying threads repeatedly.
- Helps control the number of concurrent threads, which can improve resource management (CPU, memory).

**Types of Thread Pools**:
- **Fixed Thread Pool**: Best for scenarios with a known number of tasks, where creating more threads could lead to resource contention.
- **Cached Thread Pool**: Suitable for tasks that require fast execution and don’t need long-term threading. The pool expands as needed and shrinks when threads are idle.
- **Scheduled Thread Pool**: Ideal for running tasks periodically, such as checking system health, data polling, or retrying failed operations.

**Best Practices**:
- Always use `shutdown()` or `shutdownNow()` to properly terminate an `ExecutorService`. Without this, your application may not terminate properly.

**Code Example:**

```java
// 7. Thread Pools
private static void threadPoolExample() {
    ExecutorService fixedPool = Executors.newFixedThreadPool(3);
    ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);

    for (int i = 0; i < 5; i++) {
        final int taskId = i;
        fixedPool.execute(() -> System.out.println("Fixed pool task " + taskId + " executed by " + Thread.currentThread().getName()));
    }

    scheduledPool.schedule(() -> System.out.println("Scheduled task executed"), 2, TimeUnit.SECONDS);

    fixedPool.shutdown();
    scheduledPool.shutdown();
}
```

- **Why Use Thread Pools?** They optimize system resource usage by reusing threads for multiple tasks instead of creating and destroying them.

## 8. **Concurrent Collections**

Java provides thread-safe collections that work efficiently in multithreaded environments.

**ConcurrentHashMap**:
- Unlike `HashMap`, which must be synchronized manually for use in concurrent contexts, `ConcurrentHashMap` allows concurrent reads and writes without locking the entire map. It divides the map into segments, locking only a segment when writing, allowing high concurrency.

**CopyOnWriteArrayList**:
- This collection is ideal when the list is more frequently read than written. During write operations (add, remove), a new copy of the array is created. This ensures thread safety without locking, but comes with performance costs when frequent modifications are made.

**BlockingQueue**:
- A queue that supports thread-safe put and take operations. When the queue is empty, `take()` blocks until an item is available. When the queue is full, `put()` blocks until space becomes available. 
- Useful in producer-consumer scenarios, where producers produce data and consumers process it, often at different rates.

**Code Example:**

```java
// 8. Concurrent Collections
private static void concurrentCollectionsExample() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("key1", 1);
    map.put("key2", 2);

    CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
    list.add("item1");
    list.add("item2");

    BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    queue.offer("task1");
    queue.offer("task2");

    System.out.println("ConcurrentHashMap: " + map);
    System.out.println("CopyOnWriteArrayList: " + list

);
    System.out.println("BlockingQueue: " + queue);
}
```

- **Use Case**: Ideal for applications requiring safe concurrent operations on collections like maps, queues, and lists.
