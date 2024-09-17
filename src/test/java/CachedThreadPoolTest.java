import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CachedThreadPoolTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> System.out.println(
            IntStream.range(0, 10000000).average().getAsDouble()
        )).get();
        executor.shutdown();
    }
}
