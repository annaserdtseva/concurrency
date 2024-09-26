package course.concurrency.m5_streams;

import java.util.concurrent.LinkedBlockingDeque;

public class CustomLifoBlockingQueue<T> extends LinkedBlockingDeque<T> {

    @Override
    public T take() throws InterruptedException {
        return super.takeLast();
    }
}
