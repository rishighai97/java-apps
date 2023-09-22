package synchronized_blocking;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ObjectVsClassLevelBlockingTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        testObjectLevelLock1();
        testObjectLevelLock2();
//        testClassLevelLock();
    }

    private static void testClassLevelLock() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        SomeClass object1 = new SomeClass(1);
        SomeClass object2 = new SomeClass(2);
        Future<Integer> future1 = executorService.submit(object1::classLevelLock1);
        Future<Integer> future2 = executorService.submit(object1::classLevelLock1);
        Future<Integer> future3 = executorService.submit(object2::classLevelLock1);

        future1.get();
        future2.get();
        future3.get();
        System.exit(0);
    }

    private static void testObjectLevelLock1() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        SomeClass object1 = new SomeClass(1);
        SomeClass object2 = new SomeClass(2);
        Future<Integer> future1 = executorService.submit(object1::objectLevelLock1);
        Future<Integer> future2 = executorService.submit(object1::objectLevelLock1);
        Future<Integer> future3 = executorService.submit(object2::objectLevelLock1);

        future1.get();
        future2.get();
        future3.get();
        System.exit(0);
    }

    private static void testObjectLevelLock2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        SomeClass object1 = new SomeClass(1);
        SomeClass object2 = new SomeClass(1);
        Future<Integer> future1 = executorService.submit(object1::objectLevelLock2);
        Future<Integer> future2 = executorService.submit(object1::objectLevelLock2);
        Future<Integer> future3 = executorService.submit(object2::objectLevelLock2);

        future1.get();
        future2.get();
        future3.get();
        System.exit(0);
    }
}

class SomeClass {
    private int counter;
    final Integer someVariable;
    private final Object lock;

    public SomeClass(int someVariable) {
        this.someVariable = someVariable;
        this.lock = new Object();
    }

    public Integer classLevelLock1() throws InterruptedException {
        long startTime = System.currentTimeMillis(); // thread safe. Because stored in thread stack. Shared variable (instance variable) would not be thread safe
        synchronized (SomeClass.class) {
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName()+" Class : "+this.someVariable+" execution time - "+(System.currentTimeMillis() - startTime));
        }
        return someVariable;
    }

    public Integer objectLevelLock1() throws InterruptedException {
        long startTime = System.currentTimeMillis(); // thread safe. Because stored in thread stack. Shared variable (instance variable) would not be thread safe
        synchronized (this) { // this / someVariable - synchronizes same object. Hence, object level lock
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName()+" Class : "+this.someVariable+" execution time - "+(System.currentTimeMillis() - startTime));
        }
        return someVariable;
    }

    public Integer objectLevelLock2() throws InterruptedException {
        long startTime = System.currentTimeMillis(); // thread safe. Because stored in thread stack. Shared variable (instance variable) would not be thread safe
        synchronized (lock) { // locks for same object
            Thread.sleep(2000);
            System.out.println("Object level lock 2: "+Thread.currentThread().getName()+" Class : "+this.someVariable+" execution time - "+(System.currentTimeMillis() - startTime));
        }
        return someVariable;
    }
}
