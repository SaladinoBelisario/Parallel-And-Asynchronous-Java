# `Java Multithreading: Parallelism and Asynchronous programming`

## **Evolution of Concurrency and Parallelism APIs in Java**

![API Evolution in Java for Concurrency and Parallelism](img/API%20evolution.PNG "API evolution")

## **Concurrency vs Parallelism**

### Concurrency

* Concurrency is a concept where two or more task can run simultaneously
* In Java, Concurrency is achieved using **Threads**
* Are the tasks running in interleaved fashion?
* Are the tasks running simultaneously?

![Concurrency model](img/Concurrency.PNG "Concurrency example")

* **Issues:**
  - Race Conditions
  - DeadLocks and more

* **Tools to handle these issues:**
  - Synchronized Statements/Methods
  - Reentrant Locks, Semaphores
  - Concurrent Collections
  - Conditional Objects and More

### Parallelism

* Parallelism is a concept in which tasks are literally going to run in parallel (at the same time)

* Parallelism involves these steps:
  1. Decomposing the tasks in to SubTasks(Forking)
  2. Execute the subtasks in sequential
  3. Joining the results of the tasks(Join)
  
* Whole process is also called **Fork/Join**

![Parallel model](img/Parralelism.PNG "Parallelism example")

## **Threads, Future, ForkJoin, Executor and its limitations**

Let's imagine the following service implemented:

![Service model](img/Services_Example.PNG "Service Example")

### Threads

[Example of services with Threads](src/main/java/com/example/thread)

#### Thread API

* Threads API got introduced in Java 1
* Threads are basically used to offload the blocking tasks as background tasks
* Threads allowed the developers to write asynchronous style of code

#### Thread API limitations

* Requires a lot of code to introduce asynchrony
  - Runnable, Thread:
    * Requires additional properties in Runnable
    * Starts and Join the thread

* Low level
* **Easy to introduce complexity** in to our code

#### Limitations of Thread

* Create the thread
* Start the thread
* Join the thread
* Threads are expensive
* Threads have their own runtime-stack, memory, registers and more

**NOTE: Thread Pool was created specifically to solve these problems**

### Thread pool

* Thread Pool is a group of threads created and readily available
* CPU Intensive Tasks:
  - ThreadPool Size = No of Cores
* I/O task
  - ThreadPool Size > No of Cores
* What are the benefits of thread pool?
  - No need to manually create, start and join the threads
  - Achieving Concurrency in your application

![Thread pool](img/Thread_pool.PNG "Thread pool example")

### ExecutorService

ExecutorService is a JDK API that simplifies running tasks in asynchronous mode. Generally 
speaking, ExecutorService automatically provides a pool of threads and an API for assigning 
tasks to it.

* Released as part of Java 5
* ExecutorService in Java is an **Asynchronous Task Execution Engine**
* It provides a way to asynchronously execute tasks and provides the results in a much 
simpler way compared to threads
* This enabled coarse-grained task based parallelism in Java

The _submit()_ and _invokeAll()_ methods return an object or a collection of objects of type
_Future_, which allows us to get the result of a task's execution or to check the task's status
(is it running).

Simply put, the _Future_ class represents a future result of an **asynchronous computation**. 
This result will eventually appear in the future, after the processing is complete.

![Working of the ExecutorService](img/ExecutorService.PNG "Working of Executor")

#### Limitations of ExecutorService

* Designed to Block the Thread
```java:
ProductInfo productInfo = productInfoFuture.get();
Review review = reviewFuture.get();
```

* No better way to combine futures
```java:
ProductInfo productInfo = productInfoFuture.get();
Review review = reviewFuture.get();
return new Product(productId, productInfo, review);
```

### ForkJoin Framework

* This got introduced as part of Java 7
* This is an extension of ExecutorService
* Fork/Join framework is designed to achieve **Data Parallelism**  
* ExecutorService is designed to achieve **Task-based Parallelism**
```java:
Future<ProductInfo> productInfoFuture = 
                        executorService.submit(() -> productInfoService.retrieveProductInfo(productId));
Future<Review> reviewFuture = 
                        executorService.submit(() -> reviewService.retrieveReviews(productId));
```

**Data Parallelism** is a concept where a given Task is recursively split into SubTasks until 
it reaches it the least possible size and execute those tasks in parallel. Basically it uses 
the divide and conquer approach.

The framework uses the ForkJoin pool to achieve Data Parallelism.

![ForkJoin pool for data parallelism](img/ForkJoin_pool.PNG "ForkJoin pool")