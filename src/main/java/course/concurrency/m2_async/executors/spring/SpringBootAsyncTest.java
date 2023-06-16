package course.concurrency.m2_async.executors.spring;

import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringBootAsyncTest {

    @Autowired
    private AsyncClassTest testClass;

    // this method executes after application start
    @EventListener(ApplicationReadyEvent.class)
    public void actionAfterStartup() throws ExecutionException, InterruptedException {
        testClass.runAsyncTask();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAsyncTest.class, args);
    }
}
