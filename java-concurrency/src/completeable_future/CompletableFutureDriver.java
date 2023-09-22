package completeable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureDriver {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(()->Thread.currentThread().getName(), executorService);
        System.out.println(task1.thenApply(a->a.concat(": 12345")).get());
        System.exit(1);
    }

}
