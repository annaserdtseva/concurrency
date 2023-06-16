package course.concurrency.m2_async.executors.spring;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncClassTest {

  @Autowired
  public ApplicationContext context;

//    @Autowired
//    @Qualifier("applicationTaskExecutor")
//    private ThreadPoolTaskExecutor executor;

  @Async("applicationTaskExecutor")
  public void runAsyncTask() throws ExecutionException, InterruptedException {
    System.out.println("runAsyncTask: " + Thread.currentThread().getName());
    CompletableFuture<Long> completableFuture =
        context.getAutowireCapableBeanFactory().getBean(AsyncClassTest.class).internalTask();
    CompletableFuture<Long> completableFuture2 =
        context.getAutowireCapableBeanFactory().getBean(AsyncClassTest.class).internalTask();
    while (true) {
      if (completableFuture.isDone() && completableFuture2.isDone()) {
        System.out.println("Result from asynchronous process - " + completableFuture.get());
        System.out.println("Result from asynchronous process - " + completableFuture2.get());
        break;
      }
      System.out.println("Continue doing something else. ");
      Thread.sleep(1000);
    }
  }

  @Async("applicationTaskExecutor2")
  public CompletableFuture<Long> internalTask() throws InterruptedException {
    System.out.println("internalTask: " + Thread.currentThread().getName());
    Thread.sleep(5000);
    if (new Random().nextBoolean()) {
        throw new RuntimeException("Sorry, i'm done");
    }
    return CompletableFuture.completedFuture(System.currentTimeMillis());
  }
}
