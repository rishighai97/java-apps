package singleton;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SingletonTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
//        sequentialAccess();
        parallelAccess();
        System.out.println("Execution time : "+(System.currentTimeMillis() - startTime)+" ms");
        System.exit(0);
    }

    private static void parallelAccess() throws ExecutionException, InterruptedException {
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        Callable<Singleton> callable = Singleton::getInstance3;

        ArrayList<Future<Singleton>> futures = new ArrayList<>();
        for(int i = 1 ; i <= threadCount ; i++) {
            futures.add(executorService.submit(callable));
        }

        Singleton instance = futures.get(0).get();
        for (int i = 2 ; i <= threadCount ; i++) {
            System.out.println("Comparing thread output during instantiation "+i+" : "+futures.get(i-1).get().equals(instance));
        }

        for (int i = 1 ; i <= threadCount ; i++) {
            System.out.println("Comparing thread output after instantiation "+i+" : "+futures.get(i-1).get().equals(instance));
        }

    }

    private static void sequentialAccess() throws InterruptedException {
        Singleton instance1 = Singleton.getInstance1();
        Singleton instance2 = Singleton.getInstance1();
        System.out.println("SequentialAccess: "+instance1.equals(instance2));
    }
}

class Singleton {
    private static Singleton instance;

    private Singleton() {

    }

    // 3. this would not block a thread if instance already present
    public static Singleton getInstance3() throws InterruptedException {
        if (instance != null) return instance;
        synchronized (Singleton.class) { // passing instance object here would throw NPE
            if (instance == null) {
                System.out.println("INSTANTIATING SINGLETON OBJECT");
                Thread.sleep(1000);
                instance = new Singleton();
            }
        }
        return instance;
    }

    // 2. this would make the app single threaded. time - 2000ms
    public static synchronized Singleton getInstance2() throws InterruptedException {
        Thread.sleep(1000);
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }


    // 1. this would give same instance when 2 threads are run parallely
    public static  Singleton getInstance1() throws InterruptedException {
        Thread.sleep(1000);
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
