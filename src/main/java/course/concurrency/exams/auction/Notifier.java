package course.concurrency.exams.auction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Notifier {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public void sendOutdatedMessage(Bid bid) {
        CompletableFuture.runAsync(this::imitateSending, executor);
    }

    private void imitateSending() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
    }

    public void shutdown() {}
}
