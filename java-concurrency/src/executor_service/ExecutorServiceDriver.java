package executor_service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceDriver {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<String> callable = () -> {
            try {
                Thread.sleep(5000);
                System.out.println(Thread.currentThread().getName()+" is done waiting");
                return Thread.currentThread().getName();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Future<String> result1 = executorService.submit(callable);
        Future<String> result2 =executorService.submit(callable);
        Future<String> result3 =executorService.submit(callable);
        Future<String> result4 =executorService.submit(callable);
        Future<?> future = executorService.submit(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+" is done waiting");
            return 12345;
        });
        Thread.sleep(5500);
        System.out.println(future.cancel(false));
        System.out.println(future.get());

        String first = result1.get();
        String second = result2.get();
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.println(executionTime+" "+first+" "+second);
//        System.out.println(executionTime);
//        System.exit(0);
    }

}
