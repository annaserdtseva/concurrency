package course.concurrency.m2_async.executors.spring;

import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class AsyncClassTest {

  @Autowired
  public ApplicationContext context;

  @Autowired
  @Qualifier("applicationTaskExecutor2")
  private ThreadPoolTaskExecutor executor;

  @Async("applicationTaskExecutor")
  public void runAsyncTask() {
    System.out.println("runAsyncTask: " + Thread.currentThread().getName());
    CompletableFuture.runAsync(internalTask(), executor);
  }

  public Runnable internalTask() {
    return () ->
        System.out.println("internalTask: " + Thread.currentThread().getName());
  }
}
