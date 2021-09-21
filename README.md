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

## **Streams API & Parallel Streams**

### Streams API

* Streams API got introduced in **Java 8**
* Streams API is used to process a collection of Objects
* Streams in Java are created by using the _stream()_ method

#### Sequential/Parallel  Functions  in  Streams API

* Streams API are sequential by default
* _sequential()_ -> Executes the stream in sequential
* _parallel()_ -> Executes the stream in parallel
* Both the functions changes the behavior of the whole pipeline

### ParallelStreams

* This allows your code to run in parallel
* ParallelStreams are designed to solve **Data Parallelism**
* ParallelStreams in Java are created by using the _parallelStream()_ method

#### How it works?

* _parallelStream()_:
  - **Split** the data in to chunks:
    * Data Source is split into small data chunks:
      - Example - **List Collection** split into chunks of elements to **size 1**
    * This is done using **Spliterators**
      - For ArrayList, the **Spliterator** is **ArrayListSpliterator**
  - **Execute** the data chunks
    * Data chunks are applied to the Stream Pipeline and the **Intermediate** operations 
    executed in a **Common ForkJoin Pool**
    * Watch out the [Fork/Join FrameWork](#forkjoin-framework) section
  - **Combine** the result
    * Combine the executed results into a final result
    * Combine phase in Streams API maps to **terminal** operations
    * Uses _collect()_ and _reduce()_ functions  
    * _collect(toList())_

![Parallel Stream](img/parallelStream.PNG "parallelStream working")

#### Spliterators

* Data source is split into multiple chunks by the Spliterator
* Each and every collection has a different Spliterator Implementation
* Performance differ based on the implementation

**Recommendation:** 
Always compare the performance before you use _parallelStream()_

**Final computation result order:**

The order of the collection depends on:
  - Type of Collection  
  - Spliterator Implementation of the collection

Example : _ArrayList_
  - Type of Collection - **Ordered**
  - Spliterator Implementation - Ordered Spliterator Implementation

Example : _Set_
  - Type of Collection - **Unordered**
  - Spliterator Implementation - Unordered Spliterator Implementation

### Collect Vs Reduce

**Reduce** is a "fold" operation, it applies a binary operator
to each element in the stream where the first argument to the
operator is the return value of the previous application and the 
second argument is the current stream element.

**Collect** is an aggregation operation where a "collection" is
created and each element is "added" to that collection. Collections
in different parts of the stream are then added together.

#### Both of them:

* Part of Streams API
* Used as a terminal operation in Streams
API
* Produces a single result 

#### Collect:

* Result is produced in a mutable fashion
* Feature rich and used for many use cases

Example:
* _collect(toList()), collect(toSet())_
* _collect(summingDouble(Double::doubleValue));_

#### Reduce:

* Result is produced in an immutable fashion
* Reduce the computation into a single value

Example
* Sum -> _reduce(0.0, (x , y)->x+y)_
* Multiply -> _reduce(1.0, (x , y)->x * y)_

**Identity in Reduce:**
* Identity gives you the same value when it's used in the
computation:
  - Addition: Identity = 0
    * 0 + 1 => 1
    * 0 + 20 => 20
  - Multiplication : Identity = 1
    * 1 * 1 => 1
    * 1 * 20 => 20

**_reduce()_ is recommended for computations that are associative**

### Poor performance in parallel streams

Boxing and Unboxing operations makes a program to performance poorly

### Common ForkJoin Pool

Parallel Streams has a Common ForkJoin Pool as its execution engine.

* Common ForkJoin Pool is used by:
  - ParallelStreams
  - CompletableFuture
    * Completable Future have options to use a User-defined ThreadPools
  - Common ForkJoin Pool is shared by the whole process

![Common ForkJoin Pool](img/Common_ForkJoin_Pool.PNG "Execution Engine of parallel stream")

![Common ForkJoin Pool Working](img/Common_ForkJoin_Pool_Working.PNG "Common ForkJoin Pool working")

### Modifying  Default parallelism  in  Parallel Streams

We can modify the default pool size for the ForkJoin with:

`System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");`

or

`-Djava.util.concurrent.ForkJoinPool.common.parallelism=100`